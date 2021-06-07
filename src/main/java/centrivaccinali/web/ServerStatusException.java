package centrivaccinali.web;

/**
 * The type Server status exception.
 */
public class ServerStatusException extends RuntimeException {

    /**
     * Instantiates a new Server status exception.
     *
     * @param message the message
     */
    public ServerStatusException(String message) {
        super("Status code "+message);
    }

}
