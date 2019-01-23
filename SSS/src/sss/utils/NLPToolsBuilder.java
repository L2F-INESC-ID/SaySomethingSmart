package sss.utils;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import sss.texttools.WordNetHypernym;

import java.io.IOException;

/**
 * @author Vania
 * @date 19/02/2017
 */
public class NLPToolsBuilder {

    public void createTools() throws IOException, ClassNotFoundException {

        if(Configs.getInstance().getLanguage().equalsIgnoreCase("English")) {

            NLPTools.getInstance().setNer(CRFClassifier.getClassifierNoExceptions(Configs.getInstance().getNerPath()));
            NLPTools.getInstance().setPost(new MaxentTagger(Configs.getInstance().getTaggerPath()));
            NLPTools.getInstance().setWordnet(new WordNetHypernym());
        }

    }

}
