package sss.dialog.evaluator.qascorer;

import sss.dialog.Conversation;
import sss.dialog.QA;
import sss.similarity.SimilarityMeasure;
import sss.texttools.StopWords;
import sss.texttools.normalizer.Normalizer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AnswerSimilarityToUserQuestion extends QaScorer {
    private String stopWordsLocation;
    private List<Normalizer> normalizers;

    public AnswerSimilarityToUserQuestion(double weight, String stopWordsLocation, List<Normalizer> normalizers, SimilarityMeasure similarityMeasure) {
        super(weight, similarityMeasure);
        this.stopWordsLocation = stopWordsLocation;
        this.normalizers = normalizers;
    }

    @Override
    public void score(String userQuestion, List<QA> qas, Conversation conversation) {
        StopWords stopWords = null;
        try {
            stopWords = new StopWords(this.stopWordsLocation, this.normalizers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> tokenizedQuestion = Arrays.asList(userQuestion.split("\\s+"));
        List<String> tokenizedQuestionWithoutStopWords = stopWords.getStringListWithoutStopWords(tokenizedQuestion);
        if (!tokenizedQuestionWithoutStopWords.isEmpty()) {
            this.score(tokenizedQuestion, tokenizedQuestionWithoutStopWords, qas, stopWords);
        } else {
            this.score(tokenizedQuestion, qas);
        }
    }

    private void score(List<String> tokenizedQuestion, List<QA> qas) {
        for (QA qa : qas) {
            double score = getSimilarityMeasure().distance(tokenizedQuestion, qa.getAnswerListNormalized());
            if (score >= 0.9 && score <= 1.1) { //1.1 because of doubles
                score = 0;
            }
            else if (score >= 0.4) {
                score /= 2;
            }
            super.scoreQA(qa, score);
        }
    }

    private void score(List<String> tokenizedQuestion, List<String> tokenizedQuestionWithoutStopWords, List<QA> qas, StopWords stopWords) {
        for (QA qa : qas) {

            double scoreUntokenized = getSimilarityMeasure().distance(tokenizedQuestion, qa.getAnswerListNormalized());

            double score = getSimilarityMeasure().distance(tokenizedQuestionWithoutStopWords,
                    stopWords.getStringListWithoutStopWords(qa.getAnswerListNormalized()));

            if (scoreUntokenized >= 0.9 && scoreUntokenized <= 1.1) { //1.1 because of doubles
                score = 0;

            }
            else if (scoreUntokenized >= 0.4) {
                score /= 2;
            }

            super.scoreQA(qa, score);
        }
    }
}
