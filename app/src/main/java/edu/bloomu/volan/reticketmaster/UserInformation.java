package edu.bloomu.volan.reticketmaster;

/**
 * This is a class is to get and se the users information
 * Created by Volan on 11/21/17.
 */

public class UserInformation {
    //attributes
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private int phoneNumber;

    /**
     * This method sets the user's first name
     * @param firstName
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * This gets the user's first name
     * @return
     */
    public String getFirstName()
    {
        return this.firstName;
    }

    /**
     * This method sets the user's last name
     * @param lastName
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * This gets the user's last name
     * @return
     */
    public String getLastName()
    {
        return this.lastName;
    }

    /**
     * This method sets the user's email
     * @param email
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * This gets the user's email
     * @return
     */
    public String getEmail()
    {
        return this.email;
    }

    /**
     * This method sets the user's username
     * @param username
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * This gets the user's username
     * @return
     */
    public String getUsername()
    {
        return this.username;
    }

    /**
     * This method sets the user's password
     * @param password
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * This gets the user's password
     * @return
     */
    public String getPassword()
    {
        return this.password;
    }

    /**
     * This method sets the user's phone number
     * @param phoneNumber
     */
    public void setPhoneNumber(int phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    /**
     * This gets the user's phone number
     * @return
     */
    public int getPhoneNumber()
    {
        return this.phoneNumber;
    }


}
