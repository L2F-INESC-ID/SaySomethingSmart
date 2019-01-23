package sss.lucene;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.xml.sax.SAXException;
import ptstemmer.exceptions.PTStemmerException;
import sss.dialog.BasicQA;
import sss.dialog.Conversation;
import sss.dialog.QA;
import sss.dialog.SimpleQA;
import sss.dialog.evaluator.Evaluator;
import sss.dialog.evaluator.qascorer.QaScorerFactory;
import sss.similarity.SimilarityMeasureFactory;
import sss.texttools.normalizer.Normalizer;
import sss.texttools.normalizer.NormalizerFactory;
import sss.utils.Configs;
import sss.utils.Utils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class LuceneManager {

    public static final String ANALYZER_PROPERTIES = "tokenize, ssplit, pos, lemma";
    public static final String DB4OFILENAME = Paths.get("").toAbsolutePath().toString()
        + Configs.getInstance().getLuceneConfigs().getDbPath();
    public static ObjectContainer db;

    private LuceneAlgorithm luceneAlgorithm;
    private List<Normalizer> normalizers;
    private List<Evaluator> evaluators;
    public static Conversation CONVERSATION;

    public LuceneManager() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException, PTStemmerException {

        String language = Configs.getInstance().getLanguage();
        String pathOfIndex = Configs.getInstance().getLuceneConfigs().getIndexPath() + language;

        this.normalizers = (new NormalizerFactory()).createNormalizers(Configs.getInstance().getNormalizers());

        this.evaluators = (new QaScorerFactory().createQaScorers(Configs.getInstance().getCriteria(),
                Configs.getInstance().getStopWordsPath(),
                this.normalizers,
                new SimilarityMeasureFactory().createSimilarityMeasure(Configs.getInstance().getSimilarityMeasure())));

        if (Configs.getInstance().getLuceneConfigs().isUsePreviouslyCreatedIndex()) {
            luceneAlgorithm = new LuceneAlgorithm(pathOfIndex, language, normalizers);
        } else {
            String pathOfCorpus = Configs.getInstance().getCorpusPath();
            luceneAlgorithm = new LuceneAlgorithm(pathOfIndex, pathOfCorpus, language, normalizers);
        }

        this.db = Db4oEmbedded.openFile(LuceneManager.DB4OFILENAME);
        this.CONVERSATION = new Conversation();
    }

    public LuceneManager(String corpusPath) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException, PTStemmerException {

        String language = Configs.getInstance().getLanguage();
        String pathOfIndex = Configs.getInstance().getLuceneConfigs().getIndexPath() + language;

        this.normalizers = (new NormalizerFactory()).createNormalizers(Configs.getInstance().getNormalizers());

        if (Configs.getInstance().getLuceneConfigs().isUsePreviouslyCreatedIndex()) {
            luceneAlgorithm = new LuceneAlgorithm(pathOfIndex, language, normalizers);
        }
        else {
            luceneAlgorithm = new LuceneAlgorithm(pathOfIndex, corpusPath, language, normalizers);
        }

        this.db = Db4oEmbedded.openFile(LuceneManager.DB4OFILENAME);

    }

    public void closeDB() {
        this.db.close();
    }

    public List<QA> getCandidates(String normalizedQuestion) {

        List<QA> searchedResults = new ArrayList<>();

        try {
            List<Document> luceneDocs = this.luceneAlgorithm.search(normalizedQuestion,
                    Configs.getInstance().getLuceneConfigs().getHitsPerQuery());
            searchedResults = loadLuceneResults(luceneDocs);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return searchedResults;
    }

    public String getAnswer(String question) throws IOException, ParseException {
        if (question == null || question.trim().isEmpty()) {
            return getNoReplyMessage();
        }
        String normalizedQuestion = Normalizer.applyNormalizations(question, this.normalizers);
        Utils.printDebug("Normalized question: " + normalizedQuestion);

        if (!CONVERSATION.isEmpty()) {
            BasicQA basicQA = CONVERSATION.getLastQA();
            storeDialogue(question, normalizedQuestion, basicQA.getAnswer(), basicQA.getNormalizedAnswer());
        }

        Utils.printDebug("Retrieving Lucene results...");
        List<Document> luceneDocs = this.luceneAlgorithm.search(normalizedQuestion,
                Configs.getInstance().getLuceneConfigs().getHitsPerQuery());

        Utils.printDebug("Retrieving QA's from database...");

        List<QA> searchedResults = loadLuceneResults(luceneDocs);

        Utils.printDebug("Scoring the QA's...");
        List<QA> scoredQas = scoreLuceneResults(normalizedQuestion, searchedResults);

        QA answer = getBestAnswer(question, scoredQas, normalizedQuestion);
        storeDialogue(answer.getAnswer(), answer.getNormalizedAnswer(), question, normalizedQuestion);

        Utils.printDebug("Best answer score: " + answer.getScore());
        return answer.getAnswer();
    }

    private List<QA> loadLuceneResults(List<Document> docList) {
        List<QA> qas = new ArrayList<>();
        for (Document d : docList) {
            String qaId = d.get("answer");
            SimpleQA simpleQA = getSimpleQA(Long.parseLong(qaId));
            QA qa = new QA(simpleQA.getPreviousQA(),
                    simpleQA.getQuestion(), simpleQA.getAnswer(),
                    simpleQA.getNormalizedQuestion(), simpleQA.getNormalizedAnswer(),
                    simpleQA.getDiff());
            qas.add(qa);
        }
        return qas;
    }

    public static SimpleQA getSimpleQA(long qaId) {
        SimpleQA simpleQA = db.ext().getByID(qaId);
        db.activate(simpleQA, 1);
        return simpleQA;
    }


    private List<QA> scoreLuceneResults(String question, List<QA> searchedResults) throws IOException {
        for (Evaluator qaScorer : this.evaluators) {
            qaScorer.score(question, searchedResults, CONVERSATION);
        }
        return searchedResults;
    }

    private QA getBestAnswer(String question, List<QA> scoredQas, String normalizedQuestion) throws IOException {

        if (scoredQas.size() == 0 || scoredQas == null) {
            return new QA(0, question, getNoReplyMessage(), null, null, 0);
        }
        Collections.sort(scoredQas);

        for (int i = 0; i < scoredQas.size(); i++) {

            QA qa = scoredQas.get(i);
            Utils.printDebug("" + (i + 1));
            Utils.printDebug("T - " + qa.getQuestion());
            Utils.printDebug("A - " + qa.getAnswer());
            Utils.printDebug(qa.formatScores());
            Utils.printDebug("Final Score - " + qa.getScore());
            Utils.printDebug("");
        }
        return scoredQas.get(0);
    }

    private void storeDialogue(String answer, String normalizedAnswer, String question, String normalizedQuestion) {
        CONVERSATION.addQA(new BasicQA(question, answer, normalizedQuestion, normalizedAnswer));
        try {
            FileWriter x = new FileWriter(Configs.getInstance().getLogPath(), true);
            String localDateTime = new Date().toString();
            x.write("I - " + question + "\n" +
                    "R - " + answer + "\n" +
                    "T - " + localDateTime + "\n\n");
            x.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNoReplyMessage() {
        List<String> strings = Configs.getInstance().getNoAnswerFoundMessages();
        return strings.get(new Random().nextInt(strings.size()));
    }

}