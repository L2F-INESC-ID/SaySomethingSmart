package sss.similarity;

import l2f.evaluator.distance.algorithms.set.intersection.SetIntersection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JaccardAlgorithm implements SimilarityMeasure {
    private SetIntersection setIntersection;

    public JaccardAlgorithm(SetIntersection setInterstion) {
        this.setIntersection = setInterstion;
    }

    public double distance(List<String> wordListA, List<String> wordListB) {

        Set<String> wordSetA = new HashSet<>(wordListA);
        Set<String> wordSetB = new HashSet<>(wordListB);

        double aCount = wordSetA.size();
        double bCount = wordSetB.size();
        double cCount = this.setIntersection.intersection(wordListA, wordListB);

        double jaccard = cCount / (aCount + bCount - cCount);

//        Utils.printDebug("-------- [Jaccard] --------");
//        Utils.printDebug("LA: " + wordListA.size() + " LB: " + wordListB.size()
//                + " SA: " + aCount + " SB: " + bCount
//                + " Intersection: " + cCount + " Jaccard: " + jaccard);

        return jaccard;
    }
}
