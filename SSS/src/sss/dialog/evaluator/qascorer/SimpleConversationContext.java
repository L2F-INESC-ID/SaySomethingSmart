package sss.dialog.evaluator.qascorer;

import sss.dialog.BasicQA;
import sss.dialog.Conversation;
import sss.dialog.QA;
import sss.similarity.SimilarityMeasure;
import sss.exceptions.dialog.NoPreviousQAException;

import java.util.Arrays;
import java.util.List;

public class SimpleConversationContext extends QaScorer {
    private int nPreviousQAs;

    public SimpleConversationContext(double weight, int nPreviousQAs, SimilarityMeasure similarityMeasure) {
        super(weight, similarityMeasure);
        this.nPreviousQAs = nPreviousQAs;
    }

    @Override
    public void score(String userQuestion, List<QA> qas, Conversation conversation) {

        double score;

//        Utils.printDebug("-------- [Context] --------");

        for (QA qa : qas) {

//            Utils.printDebug("------------------------");
//            Utils.printDebug("User question: " + userQuestion);
//            Utils.printDebug("Candidate: " + qa.getQuestion());

            double totalScore = 0.0;
            QA currentQA;

            try {

                currentQA = qa.getPreviousQA();

                for (int i = 0; i < nPreviousQAs; i++) {

                    try {

                    BasicQA basicQA = conversation.getNFromLastQA(i);
                    currentQA = currentQA.getPreviousQA().getPreviousQA();

//                    Utils.printDebug("---- Previous " + i + "----");
//                    Utils.printDebug("Conversation Q: " + basicQA.getQuestion());
//                    Utils.printDebug("Subtle Q: " + currentQA.getQuestion());
//                    Utils.printDebug("Conversation A: " + basicQA.getAnswer());
//                    Utils.printDebug("Subtle A: " + currentQA.getAnswer());

                    List<String> tokenizedQuestion = Arrays.asList(basicQA.getNormalizedQuestion().split("\\s+"));
                    List<String> tokenizedAnswer = Arrays.asList(basicQA.getNormalizedAnswer().split("\\s+"));

                    totalScore += getSimilarityMeasure().distance(tokenizedQuestion, currentQA.getQuestionListNormalized())
                            + getSimilarityMeasure().distance(tokenizedAnswer, currentQA.getAnswerListNormalized());

                    } catch (ArrayIndexOutOfBoundsException | NoPreviousQAException ex) {
                        break;
                    }
                }

            } catch (NoPreviousQAException ex) {
                super.scoreQA(qa, 0);
                continue;
            }

            score = totalScore / (2 * nPreviousQAs);

//            Utils.printDebug("Score: " + score);

            super.scoreQA(qa, score);
        }
    }
}
