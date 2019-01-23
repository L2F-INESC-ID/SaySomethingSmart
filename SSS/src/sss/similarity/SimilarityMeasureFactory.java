package sss.similarity;

import l2f.evaluator.distance.algorithms.set.intersection.RegularSetIntersection;

public class SimilarityMeasureFactory {

    public SimilarityMeasure createSimilarityMeasure(String distanceAlgorithm) {
        String[] split = distanceAlgorithm.split(",");
        switch (split[0]) {
            case "Dice":
                return new DiceAlgorithm(new RegularSetIntersection());
            case "Jaccard":
                return new JaccardAlgorithm(new RegularSetIntersection());
            case "Overlap":
                return new OverlapAlgorithm(new RegularSetIntersection());
            case "JaccardOverlap":
                return new JaccardOverlapAlgorithm(new RegularSetIntersection(), Double.parseDouble(split[1]));
            case "JaccardOverlapBigram":
                return new JaccardOverlapBigramAlgorithm(new RegularSetIntersection(), Double.parseDouble(split[1]));
            case "Luisa":
                return new luisa(new RegularSetIntersection());
            case "L2FASSIN":
                return new L2FAssinSimilarity();
            case "WUP":
                return new WUPSimilarity();
            default:
                throw new RuntimeException("You have defined a similarity measure that does not exist");
        }
    }
}