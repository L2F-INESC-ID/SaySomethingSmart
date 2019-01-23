package sss.mode.evaluation;

import org.xml.sax.SAXException;
import ptstemmer.exceptions.PTStemmerException;
import sss.dialog.ReferenceTA;
import sss.mode.dialogue.AbstractAnswerSelection;
import sss.mode.dialogue.HighestWeightedScoreSelection;
import sss.mode.dialogue.MostVotedSelection;
import sss.resources.ReferenceCorpusParser;
import sss.utils.Configs;
import sss.utils.Utils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vania
 * @date 31/10/2016
 */
public class Evaluation {

    private AbstractAnswerSelection answerSelection;
    private List<ReferenceTA> references;
    private int inputSize;

    private List<Integer> weights;
    private float accuracy;


    public void setup() throws ParserConfigurationException, PTStemmerException, SAXException, XPathExpressionException, IOException {

        // Reference parsing
        String interactionsPath = Configs.getInstance().getReferencesConfigs().getFolderPath()
                + Configs.getInstance().getReferencesConfigs().getInteractionsFileName();

        String linesPath = Configs.getInstance().getReferencesConfigs().getFolderPath()
                + Configs.getInstance().getReferencesConfigs().getLinesFileName();

        this.inputSize = Configs.getInstance().getReferencesConfigs().getInputSize();

        ReferenceCorpusParser rcp = new ReferenceCorpusParser(interactionsPath, linesPath, inputSize);
        this.references = rcp.parse();

        answerSelection = new MostVotedSelection();
        answerSelection.setup(true, false);

    }

    public void test(List<String> criteriaNames, List<Integer> criteriaWeights, int nPrevious) {

        weights = new ArrayList<>();
        ArrayList<String> criteria = new ArrayList<>();
        int i = 0;

        for (String criterion : criteriaNames) {
            if(criteriaWeights.get(i) > 0) {
                if(criterion.compareTo("SimpleConversationContext") == 0)
                    criteria.add(criterion + "," + criteriaWeights.get(i) + "," + nPrevious);
                else criteria.add(criterion + "," + criteriaWeights.get(i));
            }
            weights.add(criteriaWeights.get(i));
            i++;
        }

        answerSelection.updateCriteria(criteria);
        accuracy = 0.0f;

        for (ReferenceTA u : references) {

            String bestAnswer = answerSelection.provideAnswer(u.getTrigger());

            Utils.printDebug("Reference: " + u.getAnswer());
            Utils.printDebug("bestAnswer: " + bestAnswer);

            if(bestAnswer.trim().compareTo(u.getAnswer().trim()) == 0) {
                accuracy++;
                Utils.printDebug("acc: " + accuracy);
            }
        }

        accuracy /= references.size();
        accuracy *= 100.0f;
    }

    public void printAccuracy() {

        for(Integer w : weights) {
            System.out.print(w + ",");
        }
        System.out.println(accuracy);
        Utils.printDebug("----------------------------");

    }

}
