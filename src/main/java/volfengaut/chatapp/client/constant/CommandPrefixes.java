package volfengaut.chatapp.client.constant;

/**
 * Prefixes of console commands in the client application
 **/
public class CommandPrefixes {

    /**
     * Prefix of a command to send the login name to the server
     **/
    public static final String LOGIN_NAME_CMD_PREFIX = "-login";

    /**
     * Prefix of a command to send the password to the server
     **/
    public static final String PASSWORD_CMD_PREFIX = "-pw";

    /**
     * Prefix of a command to select a name to sign up
     **/
    public static final String SIGN_UP_NAME_CMD_PREFIX = "-sign_up_name";

    /**
     * Prefix of a command to select a password to sign up
     **/
    public static final String SIGN_UP_PW_CMD_PREFIX = "-sign_up_pw";

    /**
     * Prefix of a command to select a password to sign up
     **/
    public static final String SIGN_UP_PW_CONFIRM_CMD_PREFIX = "-sign_up_pw_confirm";

    /**
     * Prefix of a command to select a room
     **/
    public static final String SELECT_ROOM_CMD_PREFIX = "-room_select";

    /**
     * Prefix of a command to open a room
     **/
    public static final String OPEN_ROOM_CMD_PREFIX = "-room_open";

    /**
     * Prefix of a public message command
     **/
    public static final String PUBLIC_MSG_CMD_PREFIX = "-pub";

    /**
     * Prefix and recipient attribute of a private message command
     * (example: '-priv <some_message> -to <some_player_name>')
     **/
    public static final String PRIVATE_MSG_CMD_PREFIX = "-priv";
    public static final String PRIVATE_MSG_CMD_RECIPIENT_ATTR = "-to";

    /**
     * Prefix and reason attribute of a ban message command
     * (example: '-ban <name_of_user_to_ban> -r <reason>')
     **/
    public static final String BAN_MSG_CMD_PREFIX = "-ban";
    public static final String BAN_MSG_CMD_REASON_ATTR = "-r";

    /**
     * Prefix of a command to leave the current room
     **/
    public static final String LEAVE_ROOM_CMD_PREFIX = "-leave_room";

    /**
     * Prefix of a command to disconnect
     **/
    public static final String DISCONNECT_CMD_PREFIX = "-exit";

}
