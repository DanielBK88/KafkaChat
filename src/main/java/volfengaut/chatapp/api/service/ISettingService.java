package volfengaut.chatapp.api.service;

/**
 * Used to manage settings
 **/
public interface ISettingService {

    /**
     * Change the full name setting
     **/
    void setFullName(String fullName);

    /**
     * Change the mail address setting
     **/
    void setMailAddress(String mailAddress);

    /**
     * Change the memory setting
     **/
    void setMemory(int memory);

    /**
     * Change the operating system name setting
     **/
    void setOSName(String osName);

    /**
     * Retrieve the value of the full name setting
     **/
    String getFullName();

    /**
     * Retrieve the value of the mail address setting
     **/
    String getMailAddress();

    /**
     * Retrieve the value of the memory setting
     **/
    int getMemory();

    /**
     * Retrieve the value of the operating system setting
     **/
    String getOSName();
}
