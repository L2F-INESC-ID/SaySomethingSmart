package sss.similarity;

import l2f.evaluator.distance.algorithms.set.intersection.RegularSetIntersection;
import l2f.evaluator.distance.algorithms.set.intersection.SetIntersection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marco on 30-11-2016.
 */
public class JaccardAlgorithm4Cleaner {
    private SetIntersection setInterstion = new RegularSetIntersection();

    public JaccardAlgorithm4Cleaner() {}

    public double distance(List<String> wordSetA, List<String> wordSetB) {
        ArrayList<String> listOfWordsA = new ArrayList<>();
        ArrayList<String> listOfWordsB = new ArrayList<>();

        wordSetA.stream().filter(a -> !listOfWordsA.contains(a)).forEach(listOfWordsA::add);

        wordSetB.stream().filter(b -> !listOfWordsB.contains(b)).forEach(listOfWordsB::add);

        double aCount = listOfWordsA.size();
        double bCount = listOfWordsB.size();

        double cCount = this.setInterstion.intersection(wordSetA, wordSetB);
        return cCount / (aCount + bCount - cCount);
    }
}

