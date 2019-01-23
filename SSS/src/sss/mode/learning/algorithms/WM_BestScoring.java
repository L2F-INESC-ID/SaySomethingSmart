package sss.mode.learning.algorithms;

import sss.dialog.QA;
import sss.dialog.ReferenceTA;
import sss.dialog.evaluator.Evaluator;
import sss.similarity.SimilarityMeasure;
import sss.lucene.LuceneManager;
import sss.texttools.normalizer.Normalizer;
import sss.utils.Utils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Vania
 * @date 11/09/2016
 */
public class WM_BestScoring extends AbstractWeightedMajority {

    /*
    * Constructors
    */

    public WM_BestScoring() {
    }

    public WM_BestScoring(LuceneManager lm, List<ReferenceTA> references, int corpusSize,
                          List<Evaluator> experts, List<Normalizer> normalizers,
                          SimilarityMeasure similarityMeasure,
                          int minIterations, int minConvergence,
                          int etaFactor, int decimalPlaces) {

        super(lm, references, corpusSize, experts, normalizers, similarityMeasure,
                minIterations, minConvergence, etaFactor, decimalPlaces);
    }


    /*
    * Specific operations
    */

    protected double computeReward(List<QA> candidates, String reference, int expert_idx) {

        double candidateScores;
        double newReward = 0.0;

        for(QA c : candidates) {

            candidateScores = c.getScores().get(expert_idx);
            newReward += rewardFunction(reference, c.getAnswer(), candidateScores);
        }

        Utils.printDebug("sum(r_i) " + (expert_idx + 1) + ": " + newReward);

        newReward /= Math.max(candidates.size(), 1.0); // in case candidates are zero
        Utils.printDebug("r " + (expert_idx + 1) + ": " + newReward);

        return ((double) Math.round(newReward * Math.pow(10, decimalPlaces))) / Math.pow(10, decimalPlaces);
    }


    protected double updateWeight(double reward) {
        return Math.exp(zeta * reward);
    }


    /*
    * Model functions
     */
    private double rewardFunction(String reference, String candidate, double score) {

        List<String> tokenizedCandidate = Arrays.asList(candidate.split("\\s+"));
        List<String> tokenizedReference = Arrays.asList(reference.split("\\s+"));

        double similarity = similarityMeasure.distance(tokenizedCandidate, tokenizedReference);

        double reward = 1 - Math.abs(similarity - score) + EPSILON;

        return reward;
    }

}
