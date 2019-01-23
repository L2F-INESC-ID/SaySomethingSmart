package sss.exceptions.resources;

/**
 * @author Vania
 * @date 19/07/2016
 */
public class EmptyCorpusException extends Exception {
    public EmptyCorpusException() {
        super("The corpus provided is empty.");
    }
}
