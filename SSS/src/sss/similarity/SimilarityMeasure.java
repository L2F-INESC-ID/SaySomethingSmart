package sss.similarity;

import java.io.IOException;
import java.util.List;

public interface SimilarityMeasure {

    double distance(List<String> paramList1, List<String> paramList2);
}