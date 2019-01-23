package sss.exceptions.input;

/**
 * @author Vania
 * @date 25/10/2016
 */
public class InvalidArgumentException extends Exception {

    public InvalidArgumentException(String message) {
        super(message +  " See README.md for usage instructions.");
    }

}
