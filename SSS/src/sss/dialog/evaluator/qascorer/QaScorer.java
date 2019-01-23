package sss.dialog.evaluator.qascorer;

import sss.dialog.Conversation;
import sss.dialog.QA;
import sss.dialog.evaluator.Evaluator;
import sss.similarity.SimilarityMeasure;

import java.util.List;

public abstract class QaScorer implements Evaluator {
    private double weight; //has to be between 0 and 100
    private SimilarityMeasure similarityMeasure;

    protected QaScorer(double weight, SimilarityMeasure similarityMeasure) {
        this.weight = weight;
        this.similarityMeasure = similarityMeasure;
    }

    public abstract void score(String userQuestion, List<QA> qas, Conversation conversation);

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void scoreQA(QA qa, double qaScore) {
        qa.addScore(qaScore, getWeight());
    }

    protected SimilarityMeasure getSimilarityMeasure() {
        return similarityMeasure;
    }
}
