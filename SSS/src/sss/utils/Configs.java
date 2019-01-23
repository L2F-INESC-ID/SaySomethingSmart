package sss.utils;

import sss.utils.structures.LuceneConfigs;
import sss.utils.structures.ReferencesConfigs;

import java.util.List;
import java.util.TreeMap;

/**
 * @author Vania
 * @date 23/10/2016
 */
public class Configs {

    /* Instance */
    private static Configs configInstance = new Configs();

    /* Attributes */
    private String language;
    private LuceneConfigs luceneConfigs;
    private String corpusPath;
    private String stopWordsPath;
    private String importantPersonsPath;
    private String nerPath;
    private String taggerPath;
    private String wordnetDictPath;
    private String logPath;

    private List<String> normalizers;
    private String similarityMeasure;

    private List<String> criteria;
    private int contextNpreviousInteractions;

    private ReferencesConfigs referencesConfigs;

    private List<String> noAnswerFoundMessages;


    public static Configs getInstance() {
        return configInstance;
    }

    private Configs() {
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCorpusPath() {
        return corpusPath;
    }

    public void setCorpusPath(String corpusPath) {
        this.corpusPath = corpusPath;
    }

    public String getStopWordsPath() {
        return stopWordsPath;
    }

    public void setStopWordsPath(String stopWordsPath) {
        this.stopWordsPath = stopWordsPath;
    }

    public String getImportantPersonsPath() {
        return importantPersonsPath;
    }

    public void setImportantPersonsPath(String importantPersonsPath) {
        this.importantPersonsPath = importantPersonsPath;
    }

    public String getNerPath() {
        return nerPath;
    }

    public void setNerPath(String nerPath) {
        this.nerPath = nerPath;
    }

    public String getTaggerPath() {
        return taggerPath;
    }

    public void setTaggerPath(String taggerPath) {
        this.taggerPath = taggerPath;
    }

    public String getWordnetDictPath() {
        return wordnetDictPath;
    }

    public void setWordnetDictPath(String wordnetDictPath) {
        this.wordnetDictPath = wordnetDictPath;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public List<String> getNormalizers() {
        return normalizers;
    }

    public void setNormalizers(List<String> normalizers) {
        this.normalizers = normalizers;
    }

    public String getSimilarityMeasure() {
        return similarityMeasure;
    }

    public void setSimilarityMeasure(String similarityMeasure) {
        this.similarityMeasure = similarityMeasure;
    }

    public List<String> getNoAnswerFoundMessages() {
        return noAnswerFoundMessages;
    }

    public void setNoAnswerFoundMessages(List<String> noAnswerFoundMessages) {
        this.noAnswerFoundMessages = noAnswerFoundMessages;
    }

    public List<String> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<String> criteria) {
        this.criteria = criteria;
    }

    public int getContextNpreviousInteractions() {
        return contextNpreviousInteractions;
    }

    public void setContextNpreviousInteractions(int contextNpreviousInteractions) {
        this.contextNpreviousInteractions = contextNpreviousInteractions;
    }

    public LuceneConfigs getLuceneConfigs() {
        return luceneConfigs;
    }

    public void setLuceneConfigs(LuceneConfigs luceneConfigs) {
        this.luceneConfigs = luceneConfigs;
    }

    public ReferencesConfigs getReferencesConfigs() {
        return referencesConfigs;
    }

    public void setReferencesConfigs(ReferencesConfigs referencesConfigs) {
        this.referencesConfigs = referencesConfigs;
    }

}
