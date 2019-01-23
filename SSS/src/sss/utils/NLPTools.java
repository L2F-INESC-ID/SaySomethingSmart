package sss.utils;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import sss.texttools.WordNetHypernym;

/**
 * @author Vania
 * @date 19/02/2017
 */
public class NLPTools {
    private static NLPTools ourInstance = new NLPTools();

    public static NLPTools getInstance() {
        return ourInstance;
    }

    private AbstractSequenceClassifier<CoreLabel> ner;
    private MaxentTagger post;
    private WordNetHypernym wordnet;

    private NLPTools() {
    }

    public AbstractSequenceClassifier<CoreLabel> getNer() {
        return ner;
    }

    public void setNer(AbstractSequenceClassifier<CoreLabel> ner) {
        this.ner = ner;
    }

    public MaxentTagger getPost() {
        return post;
    }

    public void setPost(MaxentTagger post) {
        this.post = post;
    }

    public WordNetHypernym getWordnet() {
        return wordnet;
    }

    public void setWordnet(WordNetHypernym wordnet) {
        this.wordnet = wordnet;
    }
}
