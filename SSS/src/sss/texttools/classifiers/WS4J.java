package sss.texttools.classifiers;

import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.lexical_db.data.Concept;
import edu.cmu.lti.ws4j.Relatedness;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

import java.util.List;

/**
 * Created by murphy on 29-11-2016.
 */
public class WS4J {
    private static ILexicalDatabase dbws4j = new NictWordNet();
    // Can be Lesk
    private static RelatednessCalculator[] wup = { new WuPalmer(dbws4j) };

    public WS4J(){}

    public double[][] Mesure(String[] word1, String[] word2) {

        WS4JConfiguration.getInstance().setMFS(true);
        double[][] s = new double[word1.length][word2.length];
        for (RelatednessCalculator rc : wup) {
            s = rc.getNormalizedSimilarityMatrix(word1, word2);
        }
        return s;
    }

    public double Mesure2 (String word1, String word2){
        RelatednessCalculator wup = new WuPalmer(dbws4j);

        List<POS[]> posPairs = wup.getPOSPairs();
        String p1 = null;
        String p2= null;

        double maxScore = -1D;
        for(POS[] posPair: posPairs) {
            List<Concept> synsets1 = (List<Concept>)dbws4j.getAllConcepts(word1, posPair[0].toString());
            List<Concept> synsets2 = (List<Concept>)dbws4j.getAllConcepts(word2, posPair[1].toString());

            for(Concept ss1: synsets1)
            {
                for (Concept ss2: synsets2) {

                    Relatedness relatedness = wup.calcRelatednessOfSynset(ss1, ss2);
                    double score = relatedness.getScore();
                    if (score > maxScore) {
                        maxScore = score;
                    }
                    p1=ss1.getPos().toString();
                    p2=ss2.getPos().toString();
                }
            }} if (maxScore == -1D) {
            maxScore = 0.0;}
        return maxScore;
    }
}
