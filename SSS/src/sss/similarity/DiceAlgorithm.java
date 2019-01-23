package sss.similarity;

/*
import org.annolab.tt4j.*;
import static java.util.Arrays.asList;
import java.io.IOException;
*/
import l2f.evaluator.distance.algorithms.set.intersection.SetIntersection;

import java.util.List;

public class DiceAlgorithm implements SimilarityMeasure {
    private SetIntersection setIntersection;

    public DiceAlgorithm(SetIntersection setIntersection) {
        this.setIntersection = setIntersection;
    }

    public double distance(List<String> wordSetA, List<String> wordSetB) {
/*                System.setProperty("treetagger.home", "/Applications/tree-tagger");
                TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
                try {
//                System.out.println(wordSetA);
//                System.out.println(wordSetB);
                        tt.setModel("/Applications/tree-tagger/lib/pt.par:iso8859-1");
                        tt.setHandler(new TokenHandler<String>() {
                                public void token(String token, String pos, String lemma) {
                                        System.out.println(token + "\t" + pos + "\t" + lemma);
                                }
                        });
                 }catch(IOException ioe){}
                        try {
                        //tt.process(asList(new String[] { "Gostava", "de", "te", "matar", "." }));} catch(Exception ioe1){}
                        tt.process(wordSetA);
                        tt.process(wordSetB);} catch(Exception ioe1){}
                        //try {tt.process(asList(new String[]) wordSetA);} catch(Exception ioe1){}
                finally {
                        tt.destroy();
                }*/
        double aCount = wordSetA.size();
        double bCount = wordSetB.size();
        return 2.0D * this.setIntersection.intersection(wordSetA, wordSetB) / (aCount + bCount);
   }
}