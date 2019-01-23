package sss.resources;

import sss.dialog.ReferenceTA;
import sss.exceptions.resources.EmptyCorpusException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vania
 * @date 05/07/2016
 */
public class ReferenceCorpusParser {

    private String corpusDialoguesPath;
    private String corpusLinesPath;
    private int corpusSize;

    /*
    /* Constructors
    */
    public ReferenceCorpusParser() {
    }

    public ReferenceCorpusParser(String corpusDialoguesPath, String corpusLinesPath, int corpusSize) {
        this.corpusDialoguesPath = corpusDialoguesPath;
        this.corpusSize = corpusSize;
        this.corpusLinesPath = corpusLinesPath;
    }

    /*
    /* Available methods
    */
    public List<ReferenceTA> parse() {

        List<ReferenceTA> references = new ArrayList<>();

        String line;
        File file;
        FileReader reader;
        BufferedReader inputStream;

        try {

            file = new File(corpusDialoguesPath);
            reader = new FileReader(file);
            inputStream = new BufferedReader(reader);

            int id=0;
            while(id < corpusSize) {

                line = inputStream.readLine();

                if(line == null || line.isEmpty()) {
                    break;
                }

                String[] dialog_items = line.split("\\+\\+\\+\\$\\+\\+\\+");

                String[] dialog_lines = dialog_items[3].split("(\',|\\[\'|\'\\])");

                // zero index is empty
                String interaction = getInteractionById(dialog_lines[1]);

                String[] interaction_lines = interaction.split("\\n");

                String[] t_items = interaction_lines[0].split("\\+\\+\\+\\$\\+\\+\\+");
                String[] a_items = interaction_lines[1].split("\\+\\+\\+\\$\\+\\+\\+");

                String t = t_items[4];
                String a = a_items[4];

                ReferenceTA rta = new ReferenceTA(id, t, a);

                id++;

                references.add(rta);
            }


        } catch(IOException e) {
            e.printStackTrace();
        } catch (EmptyCorpusException e) {
            e.printStackTrace();
        }

        return references;
    }

    /*
    /* Helper methods
    */
    private String getInteractionById(String id) throws EmptyCorpusException {

        String line, prev_line = "";
        File file;
        FileReader reader;
        BufferedReader inputStream;

        try {

            file = new File(corpusLinesPath);
            reader = new FileReader(file);
            inputStream = new BufferedReader(reader);

            while (true) {

                line = inputStream.readLine();

                if(line == null || line.isEmpty()) {
                    throw new EmptyCorpusException();
                }

                if(line.matches(id + ".*")) {
                    return line + "\n" + prev_line; // lines are in reverse order
                }

                prev_line = line;

            }

        } catch(IOException e1) {
            e1.printStackTrace();
        }

        return "";

    }

    /*
    /* Getters and setters
    */

    public void setCorpusDialoguesPath(String corpusDialoguesPath) {
        this.corpusDialoguesPath = corpusDialoguesPath;
    }

    public void setCorpusLinesPath(String corpusLinesPath) {
        this.corpusLinesPath = corpusLinesPath;
    }

    public void setCorpusSize(int corpusSize) {
        this.corpusSize = corpusSize;
    }
}
