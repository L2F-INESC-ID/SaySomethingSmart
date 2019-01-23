package sss.texttools;

import ptstemmer.exceptions.PTStemmerException;
import sss.similarity.JaccardAlgorithm4Cleaner;
import sss.texttools.classifiers.Ner;
import sss.texttools.classifiers.PoST;
import sss.texttools.normalizer.Normalizer;
import sss.texttools.normalizer.NormalizerFactory;
import sss.utils.Configs;
import sss.utils.NLPTools;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by murphy on 08-11-2016.
 */
public class Cleaner {

    public Cleaner(){
    }

    public String clean(String str) {

        Pattern replace = Pattern.compile("(\\sdoctor[\\.,\\s])");
        Pattern replace1 = Pattern.compile("(\\sare[\\.,\\s])");
        Pattern replace3 = Pattern.compile("(\\sis[\\.,\\s])");
        Pattern replace4 = Pattern.compile("(\\sam[\\.,\\s])");
        Pattern replace5 = Pattern.compile("(\\swill[\\.,\\s])");
        Pattern replace6 = Pattern.compile("(\\swould[\\.,\\s])");
        Pattern replace7 = Pattern.compile("(\\shave[\\.,\\s])");
        Pattern replace8 = Pattern.compile("(\\shad[\\.,\\s])");
        Pattern replace2 = Pattern.compile("\\s[a-zA-z&&[^()]]+(\\s)not[\\.,\\s]");

        Matcher matcher2 = replace.matcher(str);
        String newString = matcher2.replaceAll("dr. ");

        Matcher matcher5 = replace1.matcher(newString);
        String newString3 = matcher5.replaceAll("re ");

        Matcher matcher1 = replace4.matcher(newString3);
        String newString4 = matcher1.replaceAll("m ");

        Matcher matcher6 = replace3.matcher(newString4);
        String newString5 = matcher6.replaceAll("s ");

        Matcher matcher7 = replace5.matcher(newString5);
        String newString6 = matcher7.replaceAll("ll ");

        Matcher matcher8 = replace6.matcher(newString6);
        String newString7 = matcher8.replaceAll("d ");

        Matcher matcher9 = replace7.matcher(newString7);
        String newString8 = matcher9.replaceAll("ve ");

        Matcher matcher10 = replace8.matcher(newString8);
        String newString9 = matcher10.replaceAll("d ");

        Matcher matcher30 = replace2.matcher(newString9);
        String newString20 = newString9;
        while(matcher30.find()){
            newString20 = matcher30.replaceAll(matcher30.group().replaceAll(" not ", "")+"nt ");
        }
        return newString20.trim();

    }

    public String cleanNER(String str) {

        Pattern replace = Pattern.compile("([a-zA-Z\\.,\\-]+(\\/LOCATION)[\\s])+");
        Pattern replace23 = Pattern.compile("([a-zA-Z\\.,\\-]+(\\/LOCATION)[('s)])");
        Pattern replace1 = Pattern.compile("([a-zA-Z\\.,\\-]+(\\/LOCATION),)");
        Pattern replace2 = Pattern.compile("([a-zA-Z\\.,\\-]+(\\/LOCATION)\\.)");
        Pattern replace3 = Pattern.compile("([a-zA-Z\\.,\\-]+(\\/PERSON)[\\s])+");
        Pattern replace4 = Pattern.compile("(/O)");
        Pattern replace5 = Pattern.compile("([a-zA-Z\\.,\\-]+(\\/PERSON)[\\s]?)+");
        Pattern replace24 = Pattern.compile("([a-zA-Z\\.,\\-]+(\\/PERSON)[('s)])");
        Pattern replace6 = Pattern.compile("([a-zA-Z\\.,\\-]+(\\/PERSON),)+");
        Pattern replace7 = Pattern.compile("([a-zA-Z\\.,\\-]+(\\/PERSON)\\.)+");
        Pattern replace8 = Pattern.compile("([a-zA-Z\\.,\\-]+(\\/ORGANIZATION)[\\s]?)+");
        Pattern replace25 = Pattern.compile("([a-zA-Z\\.,\\-]+(\\/ORGANIZATION)[('s)])");
        Pattern replace9 = Pattern.compile("([a-zA-Z\\.,\\-]+(\\/ORGANIZATION),)+");
        Pattern replace10 = Pattern.compile("([a-zA-Z\\.,\\-]+(\\/ORGANIZATION)\\.)+");
        Pattern replace11 = Pattern.compile("([a-zA-Z0-9$€\\.,(/O)]+(\\/MONEY)[\\s]?)+");
        Pattern replace12 = Pattern.compile("([a-zA-Z0-9$€\\.,(/O)]+(\\/MONEY),)+");
        Pattern replace13 = Pattern.compile("([a-zA-Z0-9$€\\.,(/O)]+(\\/MONEY)\\.)+");
        Pattern replace14 = Pattern.compile("([a-zA-Z0-9\\.,%]+(\\/PERCENT)[\\s]?)+");
        Pattern replace15 = Pattern.compile("([a-zA-Z0-9\\.,%]+(\\/PERCENT),)+");
        Pattern replace16 = Pattern.compile("([a-zA-Z0-9\\.,%]+(\\/PERCENT)\\.)+");
        Pattern replace17 = Pattern.compile("([a-zA-Z0-9\\-:\\.,]+(\\/DATE)[\\s]?)+");
        Pattern replace18 = Pattern.compile("([a-zA-Z0-9\\-:\\.,]+(\\/DATE),)+");
        Pattern replace19 = Pattern.compile("([a-zA-Z0-9\\-:\\.,]+(\\/DATE)\\.)+");
        Pattern replace20 = Pattern.compile("([a-zA-Z0-9\\-:\\.]+(\\/TIME)[\\s]?)+");
        Pattern replace21 = Pattern.compile("([a-zA-Z0-9\\-:\\.]+(\\/TIME),)+");
        Pattern replace22 = Pattern.compile("([a-zA-Z0-9\\-:\\.]+(\\/TIME)\\.)+");

        Matcher matcher = replace.matcher(str);
        String newString = matcher.replaceAll("LOCATION ");

        Matcher matcher24 = replace23.matcher(newString);
        String newString24 = matcher24.replaceAll("LOCATION");

        Matcher matcher2 = replace1.matcher(newString24);
        String newString2 = matcher2.replaceAll("LOCATION,");

        Matcher matcher3 = replace2.matcher(newString2);
        String newString3 = matcher3.replaceAll("LOCATION.");

        Matcher matcher4 = replace3.matcher(newString3);
        List<String> newString4 = new ArrayList<>();
        String temp = newString3;
        int i = 0;
        while (matcher4.find()) {
            newString4.add(matcher4.group().replaceAll("/PERSON", ""));
            if (evaluatePerson(newString4.get(i))){
                String newString90 = temp.replaceAll(matcher4.group(), newString4.get(i));
                temp = newString90;
                //Main.printDebug(temp + " " + i + " " + matcher4.group() +" " + newString4.get(i));
            }
            i++;
        }

        Matcher matcher21 = replace5.matcher(temp);
        String newString21 = matcher21.replaceAll("PERSON ");

        Matcher matcher25 = replace24.matcher(newString21);
        String newString25 = matcher25.replaceAll("PERSON");

        Matcher matcher22 = replace6.matcher(newString25);
        String newString22 = matcher22.replaceAll("PERSON,");

        Matcher matcher23 = replace7.matcher(newString22);
        String newString23 = matcher23.replaceAll("PERSON.");

        Matcher matcher6 = replace8.matcher(newString23);
        String newString6 = matcher6.replaceAll("ORGANIZATION ");

        Matcher matcher26 = replace25.matcher(newString6);
        String newString26 = matcher26.replaceAll("ORGANIZATION");

        Matcher matcher7 = replace9.matcher(newString26);
        String newString7 = matcher7.replaceAll("ORGANIZATION,");

        Matcher matcher8 = replace10.matcher(newString7);
        String newString8 = matcher8.replaceAll("ORGANIZATION.");

        Matcher matcher9 = replace11.matcher(newString8);
        String newString9 = matcher9.replaceAll("MONEY ");

        Matcher matcher10 = replace12.matcher(newString9);
        String newString10 = matcher10.replaceAll("MONEY,");

        Matcher matcher11 = replace13.matcher(newString10);
        String newString11 = matcher11.replaceAll("MONEY.");

        Matcher matcher12 = replace14.matcher(newString11);
        String newString12 = matcher12.replaceAll("PERCENT ");

        Matcher matcher13 = replace15.matcher(newString12);
        String newString13 = matcher13.replaceAll("PERCENT,");

        Matcher matcher14 = replace16.matcher(newString13);
        String newString14 = matcher14.replaceAll("PERCENT.");

        Matcher matcher15 = replace17.matcher(newString14);
        String newString15 = matcher15.replaceAll("DATE ");

        Matcher matcher16 = replace18.matcher(newString15);
        String newString16 = matcher16.replaceAll("DATE,");

        Matcher matcher17 = replace19.matcher(newString16);
        String newString17 = matcher17.replaceAll("DATE.");

        Matcher matcher18 = replace20.matcher(newString17);
        String newString18 = matcher18.replaceAll("TIME ");

        Matcher matcher19 = replace21.matcher(newString18);
        String newString19 = matcher19.replaceAll("TIME,");

        Matcher matcher20 = replace22.matcher(newString19);
        String newString20 = matcher20.replaceAll("TIME.");

        Matcher matcher5 = replace4.matcher(newString20);
        String newString5 = matcher5.replaceAll("");

        return newString5.trim();

    }

    public String cleanerPoST(String str, String original){
        JaccardAlgorithm4Cleaner jac = new JaccardAlgorithm4Cleaner();

        Pattern replace = Pattern.compile("(_JJ\\s)|(_NNP\\s)|(_PRP\\s)|(_RB\\s)|(_VB\\s)|(_WP\\s)");
        Pattern replace1 = Pattern.compile("(_CC)|(_CD)|(_DT)|(_EX)|(_FW)|(_IN)|(_JJR)|(_JJS)|(_LS)|(_MD)|(_NNPS)|(_PDT)|(_POS)|(_PRP\\$)|(_RBR)|(_RBS)|(_RP)|(_SYM)|(_TO)|(_UH)|(_VBD)|(_VBG)|(_VBN)|(_VBP)|(_VBZ)|(_WDT)|(_WP\\$)|(_WRB)|(_,)|(_\\.)");
        Pattern replace2 = Pattern.compile("([a-zA-Z0-9]+(_NNS))");
        Pattern replace3 = Pattern.compile("([a-zA-Z0-9]+(_NN))");
        Pattern replace4 = Pattern.compile("(_)");


        Matcher matcher = replace.matcher(str);
        String newString = matcher.replaceAll(" ");

        Matcher matcher1 = replace1.matcher(newString);
        String newString1 = matcher1.replaceAll("");

        Matcher matcher2 = replace2.matcher(newString1);
        List<String> newString4 = new ArrayList<>();
        String temp = newString1;
        int i = 0;
        while (matcher2.find()) {
            List<String> hypers = new ArrayList<>();
            List<String> defs = new ArrayList<>();
            newString4.add(matcher2.group().replaceAll("_NNS", ""));
            try {
                hypers = NLPTools.getInstance().getWordnet().Hypernyms(newString4.get(i));
                defs = NLPTools.getInstance().getWordnet().Definitions(newString4.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(defs != null) {
                double value;
                double max = 0.0;
                int countaux = 0;
                int count = 0;
                for (String s: defs) {
                    value = jac.distance(Arrays.asList(original.split("\\s+")),Arrays.asList(s.split("\\s+")));
                    if (value > max){
                        max = value;
                        count = countaux;
                    }
                    countaux ++;
                }
                if(max > 0.0) {
                    String newString90 = temp.replaceAll(matcher2.group(), hypers.get(count));
                    temp = newString90;
                } else {
                    String newString90 = temp.replaceAll(matcher2.group(), matcher2.group().replaceAll("_NNS", ""));
                    temp = newString90;
                }
            } else {
                String newString90 = temp.replaceAll(matcher2.group(), matcher2.group().replaceAll("_NNS", ""));
                temp = newString90;
            }
            i++;
        }

        Matcher matcher3 = replace3.matcher(temp);
        List<String> newString5 = new ArrayList<>();
        String temp1 = temp;
        int j = 0;
        while (matcher3.find()) {
            List<String> hypers = new ArrayList<>();
            List<String> defs = new ArrayList<>();
            newString5.add(matcher3.group().replaceAll("_NN", ""));
            try {
                hypers = NLPTools.getInstance().getWordnet().Hypernyms(newString5.get(j));
                defs = NLPTools.getInstance().getWordnet().Definitions(newString5.get(j));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(defs != null) {
                double value;
                double max = 0.0;
                int countaux = 0;
                int count = 0;
                for (String s: defs) {
                    value = jac.distance(Arrays.asList(original.split("\\s+")),Arrays.asList(s.split("\\s+")));
                    if (value > max){
                        max = value;
                        count = countaux;
                    }
                    countaux ++;
                }
                if(max > 0.0) {
                    String newString90 = temp1.replaceAll(matcher3.group(), hypers.get(count));
                    temp1 = newString90;
                } else {
                    String newString90 = temp1.replaceAll(matcher3.group(), matcher3.group().replaceAll("_NN", ""));
                    temp1 = newString90;
                }
            } else {
                String newString90 = temp1.replaceAll(matcher3.group(), matcher3.group().replaceAll("_NN", ""));
                temp1 = newString90;
            }
            j++;
        }

        Matcher matcher4 = replace4.matcher(temp1);
        String newString6 = matcher4.replaceAll(" ");

        return newString6;
    }

    public Boolean evaluatePerson (String name){
        String line;
        InputStream fis = null;
        try {
            fis = new FileInputStream(Configs.getInstance().getImportantPersonsPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);
        try {
            while ((line = br.readLine()) != null) {
                if(name.trim().equalsIgnoreCase(line.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String normalizeAndCleanAll(String str){
        String normalizedAndCleaned = "";
        if(Configs.getInstance().getLanguage().equalsIgnoreCase("english")) {
            PoST post = new PoST();
            Ner ner = new Ner();


            try {
                normalizedAndCleaned = clean(cleanNER(ner.ner(cleanerPoST(post.post(str), str))));
                List<Normalizer> normalizers = (new NormalizerFactory()).createNormalizers(Configs.getInstance().getNormalizers());
                normalizedAndCleaned = Normalizer.applyNormalizations(normalizedAndCleaned, normalizers);
                return normalizedAndCleaned;
            } catch (IOException | PTStemmerException | ClassNotFoundException e) {
                e.printStackTrace();
                return normalizedAndCleaned;
            }
        } else {
            normalizedAndCleaned = cleanAndNormalize(str);
            return normalizedAndCleaned;
        }
    }

    public String cleanAndNormalize (String str){

        String normalizedAndCleaned;
        normalizedAndCleaned = clean(str);

        try {
            List<Normalizer> normalizers = (new NormalizerFactory()).createNormalizers(Configs.getInstance().getNormalizers());
            normalizedAndCleaned = Normalizer.applyNormalizations(normalizedAndCleaned, normalizers);
        } catch (PTStemmerException e) {
            e.printStackTrace();
        }

        return normalizedAndCleaned;
    }
}
