package sss.mode.dialogue;

import sss.dialog.QA;
import sss.dialog.evaluator.Evaluator;
import sss.utils.Utils;

import java.util.Collections;
import java.util.List;

/**
 * @author Vania
 * @date 05/11/2016
 */
public class HighestWeightedScoreSelection extends AbstractAnswerSelection {

    public HighestWeightedScoreSelection() {
    }


    @Override
    public String chooseAnswer(String normalizedQuestion, List<QA> candidates) {

        for (Evaluator qaScorer : this.evaluators) {
            qaScorer.score(normalizedQuestion, candidates, this.conversation);
        }

        Collections.sort(candidates);

        // DEBUG PRINTS
        for (int i = 0; i < candidates.size(); i++) {

            QA qa = candidates.get(i);
            Utils.printDebug("Candidate #" + (i + 1));
            Utils.printDebug("T: " + qa.getQuestion());
            Utils.printDebug("A: " + qa.getAnswer());
            Utils.printDebug(qa.formatScores());
            Utils.printDebug("Final Score: " + qa.getScore() + "\n");
        }

        return candidates.get(0).getAnswer();
    }
}
