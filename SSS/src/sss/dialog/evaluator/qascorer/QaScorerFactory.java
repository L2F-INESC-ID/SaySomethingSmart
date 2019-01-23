package sss.dialog.evaluator.qascorer;

import sss.dialog.evaluator.Evaluator;
import sss.similarity.SimilarityMeasure;
import sss.texttools.normalizer.Normalizer;

import java.util.ArrayList;
import java.util.List;

public class QaScorerFactory {

    public List<Evaluator> createQaScorers(List<String> qaScorerStrings, String stopWordsLocation, List<Normalizer> normalizers, SimilarityMeasure similarityMeasure) {
        List<Evaluator> qaScorers = new ArrayList<>();
        for (String s : qaScorerStrings) {
            String[] strings = s.split(",");
            switch (strings[0]) {
                case "AnswerFrequency":
                    qaScorers.add(new AnswerFrequency(Integer.parseInt(strings[1]) / 100f, similarityMeasure));
                    break;
                case "AnswerSimilarityToUserQuestion":
                    qaScorers.add(new AnswerSimilarityToUserQuestion(Integer.parseInt(strings[1]) / 100f, stopWordsLocation, normalizers, similarityMeasure));
                    break;
                case "QuestionSimilarityToUserQuestion":
                    qaScorers.add(new QuestionSimilarityToUserQuestion(Integer.parseInt(strings[1]) / 100f, similarityMeasure));
                    break;
                case "SimpleTimeDifference":
                    qaScorers.add(new SimpleTimeDifference(Integer.parseInt(strings[1]) / 100f, similarityMeasure));
                    break;
                case "SimpleConversationContext":
                    qaScorers.add(new SimpleConversationContext(Integer.parseInt(strings[1]) / 100f, Integer.parseInt(strings[2]), similarityMeasure));
                    break;
                default:
                    throw new RuntimeException("You have inserted a QaScorer that does not exist");
            }
        }

        return qaScorers;
    }
}
