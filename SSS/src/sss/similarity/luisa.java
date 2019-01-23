package sss.similarity;

import pos.tagger.processor.POSProcessor;
import l2f.evaluator.distance.algorithms.set.intersection.SetIntersection;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class luisa implements SimilarityMeasure {
    private SetIntersection setInterstion;
    private POSProcessor posProcessor;

    public luisa(SetIntersection setInterstion) {
        this.setInterstion = setInterstion;
    }

    public double distance(List<String> wordSetA, List<String> wordSetB) {
        this.posProcessor = new POSProcessor("/Applications/tree-tagger/", "/Applications/tree-tagger/lib/pt.par");

 /*       List<String> myList = new ArrayList<String>();  
         Add items to List  
        String newString = "";  
        for (Iterator<String> it = wordSetA.iterator() ; it.hasNext() ; ){  
             newString += it.next();  
             if (it.hasNext()) {  
                 newString += ", ";   
            }
        }   
        //String ola= StringUtils.join(wordSetA, ',');
        String ola=listToString(wordSetA);
        List<String> ola1 = getPossibleAnswers(newString);*/
        
        double aCount = wordSetA.size();
        double bCount = wordSetB.size();

        double cCount = this.setInterstion.intersection(wordSetA, wordSetB);
        return cCount / (aCount + bCount - cCount);
    }
    
    public List<String> getPossibleAnswers(String utterance) {
		String possibilitiesStr = posProcessor.process(utterance);
		if(possibilitiesStr.equals(""))
			return new ArrayList<String>();
		System.out.println("Possibilities " + possibilitiesStr);
		String[] possibilitiesArray = possibilitiesStr.split(" ");
		return Arrays.asList(possibilitiesArray);
	}

}
