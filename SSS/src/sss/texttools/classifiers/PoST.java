package sss.texttools.classifiers;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import sss.utils.NLPTools;

/**
 * Created by murphy on 10-11-2016.
 */
public class PoST {

    MaxentTagger tagger;

    public PoST(){
        this.tagger = NLPTools.getInstance().getPost();
    }

    public String post(String string){
        String ner = tagger.tagString(string);
        return ner;
    }
}
