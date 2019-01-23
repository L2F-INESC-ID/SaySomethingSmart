package sss.texttools.classifiers;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import sss.utils.NLPTools;

import java.io.IOException;


/**
 * Created by Marco on 10-11-2016.
 */
public class Ner {

    AbstractSequenceClassifier<CoreLabel> classifier;

    public Ner() {
        this.classifier = NLPTools.getInstance().getNer();
    }

    public String ner(String str) throws IOException, ClassNotFoundException {
        String taggedStr = classifier.classifyToString(str);
        return taggedStr;
    }
}
