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
 * @date 13/09/2016
 */
public class WM_BestCandidate extends AbstractWeightedMajority {

    /*
    * Constructors
    */

    public WM_BestCandidate() {
    }

    public WM_BestCandidate(LuceneManager lm, List<ReferenceTA> references, int corpusSize,
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

        double newReward = 0.0;

        if(candidates.size() == 0)
            return 0;

        String bestCandidateAnswer = candidates.get(0).getAnswer();
        double bestScore = 0.0, candidateScore;

        for (QA c : candidates) {

            candidateScore = c.getScores().get(expert_idx);

            if (candidateScore > bestScore) {
                bestScore = candidateScore;
                bestCandidateAnswer = c.getAnswer();
            }
        }

        newReward = rewardFunction(reference, bestCandidateAnswer);

        Utils.printDebug("r " + (expert_idx + 1) + ": " + newReward);

        return ((double) Math.round(newReward * Math.pow(10, decimalPlaces))) / Math.pow(10, decimalPlaces);
    }


    protected double updateWeight(double reward) {
        return Math.exp(zeta * reward);
    }


    /*
    * Model functions
     */
    private double rewardFunction(String reference, String bestCandidate) {

        List<String> tokenizedCandidate = Arrays.asList(bestCandidate.split("\\s+"));
        List<String> tokenizedReference = Arrays.asList(reference.split("\\s+"));

        double similarity = similarityMeasure.distance(tokenizedCandidate, tokenizedReference);

        double reward = Math.min(similarity + EPSILON, 1.0);

        return reward;
    }
}
