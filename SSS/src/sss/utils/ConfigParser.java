package sss.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import sss.utils.structures.LuceneConfigs;
import sss.utils.structures.ReferencesConfigs;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * @author Vania
 * @date 31/10/2016
 */
public class ConfigParser {

    private String configPath;

    public ConfigParser() {
    }

    public ConfigParser(String configPath) {
        this.configPath = configPath;
    }

    public void parseConfigurations(String mode) {

        try {
            File xml = new File(this.configPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();

            // Language
            XPathExpression expr = xpath.compile("//config/language");
            Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
            Configs.getInstance().setLanguage(node.getTextContent());

            // Lucene
            expr = xpath.compile("//config/lucene/dbPath");
            node = (Node) expr.evaluate(doc, XPathConstants.NODE);
            String dbPath = node.getTextContent();

            expr = xpath.compile("//config/lucene/indexPath");
            node = (Node) expr.evaluate(doc, XPathConstants.NODE);
            String indexPath = node.getTextContent();

            expr = xpath.compile("//config/lucene/usePreviouslyCreatedIndex");
            node = (Node) expr.evaluate(doc, XPathConstants.NODE);
            boolean usePreviousIndex = Boolean.parseBoolean(node.getTextContent());

            expr = xpath.compile("//config/lucene/hitsPerQuery");
            node = (Node) expr.evaluate(doc, XPathConstants.NODE);

            Configs.getInstance().setLuceneConfigs(
                    new LuceneConfigs(dbPath, indexPath, usePreviousIndex, Integer.parseInt(node.getTextContent())));

            // Subtitles corpus path
            expr = xpath.compile("//config/paths/corpus");
            node = (Node) expr.evaluate(doc, XPathConstants.NODE);
            Configs.getInstance().setCorpusPath(node.getTextContent());

            // Stop words path
            expr = xpath.compile("//config/paths/stopwords");
            node = (Node) expr.evaluate(doc, XPathConstants.NODE);
            Configs.getInstance().setStopWordsPath(node.getTextContent());

            // Important Persons path
            expr = xpath.compile("//config/paths/importantPersons");
            node = (Node) expr.evaluate(doc, XPathConstants.NODE);
            Configs.getInstance().setImportantPersonsPath(node.getTextContent());

            // NER path
            expr = xpath.compile("//config/paths/ner");
            node = (Node) expr.evaluate(doc, XPathConstants.NODE);
            Configs.getInstance().setNerPath(node.getTextContent());

            // Tagger path
            expr = xpath.compile("//config/paths/tagger");
            node = (Node) expr.evaluate(doc, XPathConstants.NODE);
            Configs.getInstance().setTaggerPath(node.getTextContent());

            // WordNet Dictionary path
            expr = xpath.compile("//config/paths/wordnetDict");
            node = (Node) expr.evaluate(doc, XPathConstants.NODE);
            Configs.getInstance().setWordnetDictPath(node.getTextContent());

            // Log path
            expr = xpath.compile("//config/paths/log");
            node = (Node) expr.evaluate(doc, XPathConstants.NODE);
            Configs.getInstance().setLogPath(node.getTextContent());

            // Normalizations
            expr = xpath.compile("//config/normalizers");
            node = (Node) expr.evaluate(doc, XPathConstants.NODE);
            Configs.getInstance().setNormalizers(
                    Arrays.asList(node.getTextContent().split("(,)(\\s)*")));

            // Similarity measure
            expr = xpath.compile("//config/similarityMeasure");
            node = (Node) expr.evaluate(doc, XPathConstants.NODE);
            Configs.getInstance().setSimilarityMeasure(((Element)node).getAttribute("name"));
            // TODO account for other jaccard variations

            // Criteria + weights
            expr = xpath.compile("//config/criteria");
            node = (Node) expr.evaluate(doc, XPathConstants.NODE);

            List<String> criteria = new ArrayList<>();
            Node item;
            String name;
            int weight, nPrevious = 0;

            for (int i = 0; i < node.getChildNodes().getLength(); i++) {

                item = node.getChildNodes().item(i);

                if (item.getNodeType() == Node.ELEMENT_NODE) {

                    weight = Integer.parseInt(((Element) item).getAttribute("weight"));

                    if(weight == 0)
                        continue;

                    name = ((Element) item).getAttribute("name");

                    if(name.compareTo("SimpleConversationContext") == 0) {
                        nPrevious = Integer.parseInt(((Element) item).getAttribute("nPreviousInteractions"));
                        criteria.add(name + "," + weight + "," + nPrevious);
                    }
                    else criteria.add(name + "," + weight);
                }
            }

            Configs.getInstance().setCriteria(criteria);
            Configs.getInstance().setContextNpreviousInteractions(nPrevious);

            if (mode.compareTo("learning") == 0 || mode.compareTo("evaluation") == 0) {

                // References
                expr = xpath.compile("//config/referenceCorpus/folder");
                node = (Node) expr.evaluate(doc, XPathConstants.NODE);
                String folderPath = node.getTextContent();

                expr = xpath.compile("//config/referenceCorpus/interactions");
                node = (Node) expr.evaluate(doc, XPathConstants.NODE);
                String interactionsFileName = node.getTextContent();

                expr = xpath.compile("//config/referenceCorpus/lines");
                node = (Node) expr.evaluate(doc, XPathConstants.NODE);
                String linesFileName = node.getTextContent();

                expr = xpath.compile("//config/referenceCorpus/inputSize");
                node = (Node) expr.evaluate(doc, XPathConstants.NODE);

                Configs.getInstance().setReferencesConfigs(
                        new ReferencesConfigs(folderPath, interactionsFileName, linesFileName,
                                Integer.parseInt(node.getTextContent())));
            }
            if (mode.compareTo("dialogue") == 0 || mode.compareTo("evaluation") == 0) {

                // No answer found
                expr = xpath.compile("//config/noAnswerFound");
                node = (Node) expr.evaluate(doc, XPathConstants.NODE);

                List<String> messages = new ArrayList<>();

                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    item = node.getChildNodes().item(i);
                    if (item.getNodeType() == Node.ELEMENT_NODE) {
                        messages.add(item.getTextContent());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
