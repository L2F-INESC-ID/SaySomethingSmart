package sss.texttools;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;
import sss.lucene.LuceneManager;
import sss.utils.Configs;
import sss.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Marco on 10-11-2016.
 */
public class WordNetHypernym {
    private IDictionary dict;

    public WordNetHypernym() throws IOException {
        String wordNetDirectory = Configs.getInstance().getWordnetDictPath();
        String path = wordNetDirectory + File.separator + "dict";
        URL url = new URL("file", null, path);


        //construct the Dictionary object and open it
        dict = new Dictionary(url);
        dict.open();

    }

    public List<String> Hypernyms (String str) throws IOException, NullPointerException{

        List<String> hypernym = new ArrayList<>();

        IIndexWord idxWord = dict.getIndexWord(str, POS.NOUN);
        try{
            if (idxWord == null) return hypernym;
            int i = idxWord.getWordIDs().size();
            for (int j = 0; j < i; j++) {
                IWordID wordID = idxWord.getWordIDs().get(j); // 1 st meaning

                IWord word = dict.getWord(wordID);
                ISynset synset = word.getSynset();

// get the hypernyms
                List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
                List<ISynsetID> domains = synset.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE);
// print out each h y p e r n y m s id and synonyms
                List<IWord> words;
                if(domains.size() > 0) {
                    words = dict.getSynset(domains.get(0)).getWords();
                    hypernym.add(words.get(0).getLemma());
                    continue;
                }
                if(hypernyms.size() > 0) {
                    words = dict.getSynset(hypernyms.get(0)).getWords();
                    hypernym.add(words.get(0).getLemma());
                } else continue;
            }
            return hypernym;
        } catch (NullPointerException e) {
            Utils.printDebug("Caught NullPointerException: " + e.getMessage());
            return hypernym;
        }
    }

    public List<String> DefinitionsHyper (String str){
        List<String> def = new ArrayList<>();

        IIndexWord idxWord = dict.getIndexWord(str, POS.NOUN);
        try{
            if (idxWord == null) return def;
            int i = idxWord.getWordIDs().size();
            for (int j = 0; j < i; j++) {
                IWordID wordID = idxWord.getWordIDs().get(j); // 1 st meaning
                IWord word = dict.getWord(wordID);
                ISynset synset = word.getSynset();

// get the hypernyms
                List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
                List<ISynsetID> domains = synset.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE);
// print out each h y p e r n y m s id and synonyms
                List<IWord> words;
                if(domains.size() > 0) {
                    words = dict.getSynset(domains.get(0)).getWords();
                    def.add(words.get(0).getSynset().getGloss());
                    continue;
                }
                if(hypernyms.size() > 0) {
                    words = dict.getSynset(hypernyms.get(0)).getWords();
                    def.add(words.get(0).getSynset().getGloss());
                } else continue;
            }
            return def;
        } catch (NullPointerException e) {
            Utils.printDebug("Caught NullPointerException: " + e.getMessage());
            return def;
        }
    }

    public List<String> Definitions (String str){
        List<String> def = new ArrayList<>();

        IIndexWord idxWord = dict.getIndexWord(str, POS.NOUN);
        try{
            if (idxWord == null) return def;
            int i = idxWord.getWordIDs().size();
            for (int j = 0; j < i; j++) {
                IWordID wordID = idxWord.getWordIDs().get(j); // 1 st meaning
                IWord word = dict.getWord(wordID);
                ISynset synset = word.getSynset();

                def.add(synset.getGloss());
            }
            return def;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return def;
        }
    }

}
