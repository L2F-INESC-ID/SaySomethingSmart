package sss;

import org.apache.lucene.queryparser.classic.ParseException;
import org.xml.sax.SAXException;
import ptstemmer.exceptions.PTStemmerException;
import sss.exceptions.config.InvalidConfigurationException;
import sss.exceptions.input.InvalidArgumentException;
import sss.mode.dialogue.AbstractAnswerSelection;
import sss.mode.dialogue.HighestWeightedScoreSelection;
import sss.mode.dialogue.MostVotedSelection;
import sss.mode.evaluation.Evaluation;
import sss.mode.learning.SequentialLearning;
import sss.utils.ConfigParser;
import sss.utils.NLPToolsBuilder;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaySomethingSmart {



    public static void main(String[] args) throws IOException, ParserConfigurationException, XPathExpressionException, SAXException, ParseException, ClassNotFoundException, PTStemmerException, InvalidConfigurationException, InvalidArgumentException {

        ConfigParser configParser = new ConfigParser("./resources/config/config.xml");
        NLPToolsBuilder nlpBuilder = new NLPToolsBuilder();

        // Mode = learning
        if (args.length > 1 && args[0].compareTo("mode=learning") == 0) {

            configParser.parseConfigurations("learning");
            nlpBuilder.createTools();

            SequentialLearning sq = new SequentialLearning();
            sq.setup();

            if(args.length == 2) {

                File params_file = new File(args[1]);
                FileReader reader = new FileReader(params_file);
                BufferedReader inputStream = new BufferedReader(reader);
                String line;

                while (true) {

                    line = inputStream.readLine();

                    if (line == null || line.isEmpty()) {
                        break;
                    }

                    String[] params = line.split(",");

                    String strategy = params[0];
                    int etaFactor = Integer.parseInt(params[1]);
                    int decimalPlaces = Integer.parseInt(params[2]);
                    int minIterations = Integer.parseInt(params[3]);
                    int minConvergence = Integer.parseInt(params[4]);

                    sq.train(strategy, etaFactor, decimalPlaces, minIterations, minConvergence);
                    sq.printResults();
                }

                sq.closeLucene();
            }
            else {
                throw new InvalidArgumentException("The arguments given are not valid.");
            }
        }

        // Mode = evaluation
        else if (args.length > 1 && args[0].compareTo("mode=evaluation") == 0) {

            configParser.parseConfigurations("evaluation");
            nlpBuilder.createTools();

            Evaluation evaluation = new Evaluation();
            evaluation.setup();
            if(args.length == 2) {

                File params_file = new File(args[1]);
                FileReader reader = new FileReader(params_file);
                BufferedReader inputStream = new BufferedReader(reader);
                String line;

                while (true) {

                    line = inputStream.readLine();

                    if (line == null || line.isEmpty()) {
                        break;
                    }

                    List<String> criteriaNames = new ArrayList<>();
                    List<Integer> criteriaWeights = new ArrayList<>();
                    int nPrevious = 0;

                    String[] items = line.split("\\|");

                    for(String item : items) {

                        String[] subitems = item.split(" ");

                        criteriaNames.add(subitems[0]);
                        criteriaWeights.add(Integer.parseInt(subitems[1]));
                        if(subitems[0].compareTo("SimpleConversationContext") == 0)
                            nPrevious = Integer.parseInt(subitems[2]);
                    }

                    evaluation.test(criteriaNames, criteriaWeights, nPrevious);
                    evaluation.printAccuracy();
                }
            }
            else {
                throw new InvalidArgumentException("The arguments given are not valid.");
            }
        }
        else { // Mode = dialogue

            configParser.parseConfigurations("dialogue");
            nlpBuilder.createTools();

            AbstractAnswerSelection answerSelection = new HighestWeightedScoreSelection();
            answerSelection.setup(true, false); // TODO configuration

            if(args.length == 0 || (args.length == 1 && args[0].compareTo("mode=dialogue") == 0)) {

                while (true) {

                    System.out.println("Say something: ");
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String query = br.readLine();
                    if (query.isEmpty()) {
                        continue;
                    }

                    String answer = answerSelection.provideAnswer(query);
                    System.out.println("Question: " + query);
                    System.out.println("Answer: " + answer);
                    System.out.println("");
                }
            } else if (args.length >= 1) {

                String inputPath;

                if (args.length > 1 && args[0].compareTo("mode=dialogue") == 0)
                    inputPath = args[1];
                else if(args.length == 1)
                    inputPath = args[0];
                else throw new InvalidArgumentException("The arguments given are not valid.");

                BufferedReader br = new BufferedReader(new FileReader("./" + inputPath));
                String line;

                while ((line = br.readLine()) != null) {
                    String answer = answerSelection.provideAnswer(line);
                    System.out.println("User Input: " + line);
                    System.out.println("Answer: " + answer);
                    System.out.println();
                }
            }

            answerSelection.closeLucene();
        }
    }

}



