package sss.exceptions.config;

/**
 * @author Vania
 * @date 23/09/2016
 */
public class InvalidConfigurationException extends Exception {

    public InvalidConfigurationException(String message) {
        super(message + " See README.md for configuration instructions.");
    }
}
