package sss.mode.learning;

import org.xml.sax.SAXException;
import ptstemmer.exceptions.PTStemmerException;
import sss.dialog.ReferenceTA;
import sss.dialog.evaluator.Evaluator;
import sss.dialog.evaluator.qascorer.QaScorerFactory;
import sss.similarity.SimilarityMeasure;
import sss.similarity.SimilarityMeasureFactory;
import sss.exceptions.config.InvalidConfigurationException;
import sss.mode.learning.algorithms.AbstractWeightedMajority;
import sss.mode.learning.algorithms.WM_BestCandidate;
import sss.mode.learning.algorithms.WM_BestScoring;
import sss.lucene.LuceneManager;
import sss.resources.ReferenceCorpusParser;
import sss.texttools.normalizer.Normalizer;
import sss.texttools.normalizer.NormalizerFactory;
import sss.utils.Configs;
import sss.utils.Utils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

/**
 * @author Vania
 * @date 23/09/2016
 */
public class SequentialLearning {

    private LuceneManager luceneManager;
    private List<Normalizer> normalizers;
    private SimilarityMeasure algorithm;
    private List<Evaluator> evaluators;
    private List<ReferenceTA> references;
    private int inputSize;

    private AbstractWeightedMajority wm;

    private String strategy;
    private int etaFactor;
    private int decimalPlaces;
    private int minIterations;
    private int minConvergence;

    public SequentialLearning() {

    }

    public void setup() throws PTStemmerException, ParserConfigurationException, SAXException, XPathExpressionException, IOException {

        // Lucene init
        String corpusPath = Configs.getInstance().getCorpusPath();
        luceneManager = new LuceneManager(corpusPath);

        // Normalizers
        normalizers = (new NormalizerFactory()).createNormalizers(Configs.getInstance().getNormalizers());

        // Similarity Measure
        algorithm = new SimilarityMeasureFactory().createSimilarityMeasure(Configs.getInstance().getSimilarityMeasure());

        // Evaluators
        evaluators = new QaScorerFactory().createQaScorers(Configs.getInstance().getCriteria(), Configs.getInstance().getStopWordsPath(), normalizers, algorithm);

        // Reference parsing
        String interactionsPath = Configs.getInstance().getReferencesConfigs().getFolderPath()
                + Configs.getInstance().getReferencesConfigs().getInteractionsFileName();

        String linesPath = Configs.getInstance().getReferencesConfigs().getFolderPath()
                + Configs.getInstance().getReferencesConfigs().getLinesFileName();

        this.inputSize = Configs.getInstance().getReferencesConfigs().getInputSize();

        ReferenceCorpusParser rcp = new ReferenceCorpusParser(interactionsPath, linesPath, inputSize);
        this.references = rcp.parse();
    }

    public void train(String strategy, int etaFactor, int decimalPlaces,
                      int minIterations, int minConvergence) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException, PTStemmerException, InvalidConfigurationException {

        this.strategy = strategy;
        this.etaFactor = etaFactor;
        this.decimalPlaces = decimalPlaces;
        this.minIterations = minIterations;
        this.minConvergence = minConvergence;

        if (strategy.compareTo("BestScoring") == 0) {
            wm = new WM_BestScoring(luceneManager, this.references, inputSize, evaluators, normalizers, algorithm,
                    minIterations, minConvergence, etaFactor, decimalPlaces);
        }
        else if (strategy.compareTo("BestCandidate") == 0) {
            wm = new WM_BestCandidate(luceneManager, this.references, inputSize, evaluators, normalizers, algorithm,
                    minIterations, minConvergence, etaFactor, decimalPlaces);
        }
        else {
            throw new InvalidConfigurationException(strategy + " is not a valid strategy value.");
        }

        wm.learnWeights();
    }

    public void closeLucene() {
        luceneManager.closeDB();
    }

    public void printResults() {

        Utils.printDebug("------[ FINAL WEIGHTS ]-----");

        System.out.print(strategy + "," + etaFactor+ "," + decimalPlaces +
                "," + minIterations + "," + minConvergence);
        wm.printWeights();
        System.out.println();

        Utils.printDebug("----------------------------");
    }

}
