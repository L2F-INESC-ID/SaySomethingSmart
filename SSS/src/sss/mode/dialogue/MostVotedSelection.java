package sss.mode.dialogue;

import sss.dialog.QA;
import sss.dialog.evaluator.Evaluator;
import sss.dialog.evaluator.qascorer.QaScorer;

import java.util.*;

/**
 * @author Vania
 * @date 07/11/2016
 */
public class MostVotedSelection extends AbstractAnswerSelection {


    @Override
    public String chooseAnswer(String normalizedQuestion, List<QA> candidates) {

        TreeMap<String, RankedCriterion> mostVoted = new TreeMap<>();

        for (Evaluator qaScorer : this.evaluators) {

            // Scoring
            qaScorer.score(normalizedQuestion, candidates, this.conversation);

            // Best for that expert
            String bestCandidateAnswer = candidates.get(0).getAnswer();
            double bestScore = 0.0, candidateScore;
            int expert_idx = evaluators.indexOf(qaScorer);

            for (QA c : candidates) {

                candidateScore = c.getScores().get(expert_idx);

                if (candidateScore > bestScore) {
                    bestScore = candidateScore;
                    bestCandidateAnswer = c.getAnswer();
                }
            }

            double weightedVote = ((QaScorer) qaScorer).getWeight();

            if(mostVoted.containsKey(bestCandidateAnswer))
                weightedVote += mostVoted.get(bestCandidateAnswer).getWeightedVote();

            mostVoted.put(bestCandidateAnswer, new RankedCriterion(bestCandidateAnswer, weightedVote));

        }

        RankedCriterion mostVotedCriterion = (RankedCriterion) Collections.max(
                mostVoted.values(), new RankedCriterionComparator());

        return mostVotedCriterion.getCriterion();

    }

    private class RankedCriterion {

        private String criterion;
        private double weightedVote;

        public RankedCriterion(String criterion, double weightedVote) {
            this.criterion = criterion;
            this.weightedVote = weightedVote;
        }

        String getCriterion() {
            return criterion;
        }

        double getWeightedVote() {
            return weightedVote;
        }
    }

    private class RankedCriterionComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {

            RankedCriterion rc1 = (RankedCriterion) o1;
            RankedCriterion rc2 = (RankedCriterion) o2;

            if(rc1.getWeightedVote() > rc2.getWeightedVote())
                return 1;
            else if(rc1.getWeightedVote() < rc2.getWeightedVote())
                return -1;
            return 0;
        }
    }
}
