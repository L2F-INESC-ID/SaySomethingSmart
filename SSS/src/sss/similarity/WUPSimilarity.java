package sss.similarity;

import sss.texttools.classifiers.WS4J;

import java.util.List;

/**
 * @author Vania
 * @date 19/02/2017
 */
public class WUPSimilarity implements SimilarityMeasure {


    @Override
    public double distance(List<String> wordList1, List<String> wordList2) {

        WS4J ws4j = new WS4J();

        String s1 = list2String(wordList1);
        String s2 = list2String(wordList2);

        String[] strarray = s1.split("\\s+");
        String[] strarray1 = s2.split("\\s+");

        double[][] score = ws4j.Mesure(strarray, strarray1);
        double total = 0.0;
        for (double[] d: score) {
            for (double dd: d) {
                total = total + dd;
            }

        }

        total = total / ((strarray.length)*(strarray1.length));

        return total;
    }

    private String list2String(List<String> list) {

        StringBuilder sb = new StringBuilder();

        for (String s : list) {
            sb.append(s);
            sb.append(" ");
        }

        return sb.toString();
    }
}
