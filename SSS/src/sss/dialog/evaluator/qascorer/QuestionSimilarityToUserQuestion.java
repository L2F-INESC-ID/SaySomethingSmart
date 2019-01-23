package sss.dialog.evaluator.qascorer;

import sss.dialog.Conversation;
import sss.dialog.QA;
import sss.similarity.SimilarityMeasure;

import java.util.Arrays;
import java.util.List;

public class QuestionSimilarityToUserQuestion extends QaScorer {

    public QuestionSimilarityToUserQuestion(double weight, SimilarityMeasure similarityMeasure) {
        super(weight, similarityMeasure);
    }

    @Override
    public void score(String userQuestion, List<QA> qas, Conversation conversation) {

//        Utils.printDebug("-------- [Q-Q similarity] --------");

        List<String> tokenizedQuestion = Arrays.asList(userQuestion.split("\\s+"));

        for (QA qa : qas) {

//            Utils.printDebug("User question: " + userQuestion);
//            Utils.printDebug("Candidate question: " + qa.getQuestion());

            double score = getSimilarityMeasure().distance(tokenizedQuestion, qa.getQuestionListNormalized());
            super.scoreQA(qa, score);
        }
    }
}