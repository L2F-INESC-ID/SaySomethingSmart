package sss.mode.dialogue;

import org.xml.sax.SAXException;
import ptstemmer.exceptions.PTStemmerException;
import sss.dialog.BasicQA;
import sss.dialog.Conversation;
import sss.dialog.QA;
import sss.dialog.evaluator.Evaluator;
import sss.dialog.evaluator.qascorer.QaScorerFactory;
import sss.similarity.SimilarityMeasure;
import sss.similarity.SimilarityMeasureFactory;
import sss.lucene.LuceneManager;
import sss.texttools.Cleaner;
import sss.texttools.normalizer.Normalizer;
import sss.texttools.normalizer.NormalizerFactory;
import sss.utils.Configs;
import sss.utils.Utils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vania
 * @date 05/11/2016
 */
public abstract class AbstractAnswerSelection {

    private LuceneManager luceneManager;
    private boolean exactMatch;

    protected List<Normalizer> normalizers;
    protected Cleaner cleaner;
    protected SimilarityMeasure algorithm;
    protected List<Evaluator> evaluators;
    protected Conversation conversation;


    public AbstractAnswerSelection() {
    }


    public void setup(boolean useLucene, boolean exactMatch) throws ParserConfigurationException, PTStemmerException, SAXException, XPathExpressionException, IOException {

        // Lucene init
        if(useLucene) {
            String corpusPath = Configs.getInstance().getCorpusPath();
            this.luceneManager = new LuceneManager(corpusPath);
        }

        // Use Exact Match First
        this.exactMatch = exactMatch;

        // Normalizers
        this.normalizers = (new NormalizerFactory()).createNormalizers(Configs.getInstance().getNormalizers());

        // Cleaner
        this.cleaner = new Cleaner();

        // Similarity Measure
        this.algorithm = new SimilarityMeasureFactory().createSimilarityMeasure(Configs.getInstance().getSimilarityMeasure());

        // Criteria
        this.evaluators = (new QaScorerFactory().createQaScorers(Configs.getInstance().getCriteria(),
                Configs.getInstance().getStopWordsPath(),
                this.normalizers,
                new SimilarityMeasureFactory().createSimilarityMeasure(Configs.getInstance().getSimilarityMeasure())));

        // Conversation
        this.conversation = new Conversation();

    }


    public String provideAnswer(String trigger) {

        String nt = trigger;
//        String nt = cleaner.clean(trigger); // Marco
        nt = Normalizer.applyNormalizations(nt, this.normalizers);
        List<QA> candidates = luceneManager.getCandidates(nt);

        return provideAnswer(trigger, nt, candidates);
    }

    public String provideAnswer(String trigger, String normalizedTrigger,  List<QA> candidates) {

        if(candidates.size() == 0)
            return ""; // TODO Use Not found answer

        else {

            String answer;
            List<QA> exactMatches = null;

            if(exactMatch)
                exactMatches = findExactMatches(normalizedTrigger, candidates);

            if (!exactMatch || exactMatches.size() == 0) {

                Utils.printDebug("404 Exact Match Not Found");
                answer = chooseAnswer(normalizedTrigger, candidates);
            }
            else if(exactMatches.size() == 1) {
                Utils.printDebug("One!!!1! Exact Match Found");
                answer =  exactMatches.get(0).getAnswer();
            }
            else {
                Utils.printDebug("Several Exact Matches Found");
                answer = chooseAnswer(normalizedTrigger, exactMatches);
            }

            String na = Normalizer.applyNormalizations(answer, this.normalizers);
            this.conversation.addQA(new BasicQA(trigger, answer, normalizedTrigger, na));
            return answer;
        }
    }

    public abstract String chooseAnswer(String normalizedQuestion, List<QA> candidates);

    protected List<QA> findExactMatches(String normalizedTrigger, List<QA> candidates) {

        List<QA> exactMatches = new ArrayList<>();

        for(QA c : candidates) {

            String normalizedCandidate = Normalizer.applyNormalizations(c.getQuestion(), this.normalizers);

            if(normalizedTrigger.compareTo(normalizedCandidate) == 0)
                exactMatches.add(c);
        }

        return exactMatches;
    }

    public void closeLucene() {
        this.luceneManager.closeDB();
    }

    public void updateCriteria(List<String> criteria) {
        this.evaluators = (new QaScorerFactory().createQaScorers(criteria,
                Configs.getInstance().getStopWordsPath(),
                this.normalizers,
                new SimilarityMeasureFactory().createSimilarityMeasure(Configs.getInstance().getSimilarityMeasure())));
    }


    /*
    * Getters
     */
    public Conversation getConversation() {
        return this.conversation;
    }
}
