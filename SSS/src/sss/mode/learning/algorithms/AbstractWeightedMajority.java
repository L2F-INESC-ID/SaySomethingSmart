package sss.mode.learning.algorithms;

import org.xml.sax.SAXException;
import ptstemmer.exceptions.PTStemmerException;
import sss.dialog.QA;
import sss.dialog.ReferenceTA;
import sss.dialog.evaluator.Evaluator;
import sss.dialog.evaluator.qascorer.QaScorer;
import sss.similarity.SimilarityMeasure;
import sss.lucene.LuceneManager;
import sss.mode.dialogue.AbstractAnswerSelection;
import sss.mode.dialogue.MostVotedSelection;
import sss.texttools.normalizer.Normalizer;
import sss.utils.Utils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

/**
 * @author Vania
 * @date 05/07/2016
 */
public abstract class AbstractWeightedMajority {

    private LuceneManager lm;

    protected List<ReferenceTA> references;
    protected List<Evaluator> experts;
    protected List<Normalizer> normalizers;
    protected SimilarityMeasure similarityMeasure;
    private AbstractAnswerSelection answerSelection;

    protected double zeta;
    protected int decimalPlaces;
    private int minIterations;
    private int minConvergence;

    private double[] finalWeights;
    private int lastIteration;

    protected static final float EPSILON = 0.001f; // to avoid zero scores


    /*
    * Constructors
    */
    public AbstractWeightedMajority() {

    }

    public AbstractWeightedMajority(LuceneManager lm, List<ReferenceTA> references, int corpusSize,
                                    List<Evaluator> experts, List<Normalizer> normalizers,
                                    SimilarityMeasure similarityMeasure,
                                    int minIterations, int minConvergence,
                                    int etaFactor, int decimalPlaces) {
        this.lm = lm;
        this.references = references;
        this.experts = experts;
        this.normalizers = normalizers;
        this.similarityMeasure = similarityMeasure;
        this.zeta = Math.sqrt((etaFactor * Math.log(experts.size())) / corpusSize);
        this.decimalPlaces = decimalPlaces;
        this.minIterations = minIterations;
        this.minConvergence = minConvergence;
    }

    /*
    * Specific operations
    */

    /**
     * Computes the reward for an expert in a round
     * @param candidates
     * @param reference
     * @param expert_idx
     * @return reward
     */
    abstract protected double computeReward(List<QA> candidates, String reference, int expert_idx);

    /**
     * Returns the updated weight of an expert given the reward
     * @param reward
     * @return Updated weight
     */
    abstract protected double updateWeight(double reward);

    /*
    * Main method
    */

    /**
     *
     * @return The set of weights for all experts
     */
    public void learnWeights() throws ParserConfigurationException, PTStemmerException, SAXException, XPathExpressionException, IOException {

        double[] weights = new double[experts.size()];
        double totalWeight;

        double[] rewards = new double[experts.size()];

        boolean done;
        int convergenceCount = 0;

        answerSelection = new MostVotedSelection();
        answerSelection.setup(false, false);

        for(Evaluator exp : experts) {
            ((QaScorer) exp).setWeight(1.0 / experts.size());
            Utils.printDebug("weight " + experts.indexOf(exp) + ": " + ((QaScorer) exp).getWeight());
        }

        int t = 1;
        int j = 0;
        for (ReferenceTA u : references) {

            Utils.printDebug("------------ ITERATION " + t + " ------------");
            Utils.printDebug("Reference Trigger: " + u.getTrigger());
            Utils.printDebug("Reference Answer: " + u.getAnswer());

            String nt = Normalizer.applyNormalizations(u.getTrigger(), this.normalizers);

            // Scoring
            List<QA> candidates = lm.getCandidates(nt);


            if(candidates.size() == 0) {
                Utils.printDebug("No candidates: " + ++j);
                continue;
            }

//            String answer = answerSelection.chooseAnswer(nt, candidates);

            String answer = answerSelection.provideAnswer(u.getTrigger(), nt, candidates);

            totalWeight = 0.0;
            for(int i = 0; i < experts.size(); i++) {

//                QaScorer qaScorer = (QaScorer) experts.get(i);
//                qaScorer.score(nt, candidates, null);

                // Accumulate reward
                Utils.printDebug("---");
                rewards[i] += computeReward(candidates, u.getAnswer(), i);
                Utils.printDebug("R(1, ..., t)" + (i + 1) + ": " + rewards[i]);

                // Update weights
                weights[i] = updateWeight(rewards[i]);
                Utils.printDebug("w_ " + (i + 1) + ": " + weights[i]);
                Utils.printDebug("---");

                totalWeight += weights[i];
            }

            // distinct for loops because totalWeight is still being updated in the first loop
//            done = true;
            for(int i = 0; i < experts.size(); i++) {

                // normalize weight to be between 0 and 100
                weights[i] = (weights[i] * 100) / totalWeight;

                // Convergence test
//                if(Math.round(((QaScorer) experts.get(i)).getWeight()) != Math.round(weights[i])) {
//                    done = false;
//                    convergenceCount = 0;
//                }

                ((QaScorer) experts.get(i)).setWeight(weights[i]);

                Utils.printDebug("M" + (i + 1) + ": " + weights[i]);
            }
            Utils.printDebug("------------------------------");

            finalWeights = weights;
            lastIteration = t;

            // If weights are the same from the previous interaction, stop
//            convergenceCount++;
//            if (done && (convergenceCount > minConvergence) && (t > minIterations)) {
//                break;
//            }

            // Intermediate Weights
            if(t == 1 || t % 300 == 0) {
                printWeights();
                System.out.println();
            }

            t++;
        }

    }

    public void printWeights() {

        System.out.print("," + lastIteration);
        for (int i = 0; i < finalWeights.length; i++) {
            System.out.print("," + finalWeights[i]);
        }
    }

}
