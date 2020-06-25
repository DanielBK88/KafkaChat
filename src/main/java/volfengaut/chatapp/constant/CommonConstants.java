package volfengaut.chatapp.constant;

/**
 * Common constants used by the server and client application
 **/
public class CommonConstants {

    /**
     * Chat server address
     **/
    public static final String SERVER_ADDRESS = "localhost";

    /**
     * Chat server port
     **/
    public static final int SERVER_PORT = 8882;

    /**
     * The delimiter of data send by a server to the client
     * (f.e. existing room names, existing chatter names etc.)
     **/
    public static final String DATA_DELIMITER = ", ";

    /**
     * Minimal length of user names / passwords / room names
     **/
    public static final int MIN_LENGTH = 5;
    
    /**
     * Maximal length of user names / passwords / room names
     **/
    public static final int MAX_LENGTH = 20;
    
    /**
     * The name of the chatter role in the database
     **/
    public static final String CHATTER_ROLE_NAME = "CHATTER";

    /**
     * The name of the admin role in the database
     **/
    public static final String ADMIN_ROLE_NAME = "ADMIN";
    
}
