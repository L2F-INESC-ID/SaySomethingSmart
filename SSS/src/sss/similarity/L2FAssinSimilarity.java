package sss.similarity;

import sss.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * @author Vania
 * @date 11/12/2016
 */
public class L2FAssinSimilarity implements SimilarityMeasure {

    @Override
    public double distance(List<String> sentenceWordList1, List<String> sentenceWordList2) {

        String s1 = list2String(sentenceWordList1);
        String s2 = list2String(sentenceWordList2);

        // TODO put url on config.xml
        String url = "http://127.0.0.1:21000/";
        String[] command = {"curl", "-d" , "sentence1=" + s1, "-d", "sentence2=" + s2, url};

        ProcessBuilder builder = new ProcessBuilder(command);
        Map<String, String> environ = builder.environment();
        final Process process;

        Double value = 0.0;
        String line;

        try {
            process = builder.start();

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while ((line = br.readLine()) != null) {

                if (line.split("value").length > 1) {

                    String [] tok = line.split("\"");
                    value = Double.parseDouble(tok[3]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        value = value / 4.0;
        Utils.printDebug("Value: " + value);

        return value;

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
