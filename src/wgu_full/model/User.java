package wgu_full.model;

import javafx.collections.ObservableList;

import static wgu_full.DAO.UserDao.getAllUsers;

public class User {
    private int user_id;
    private String user_name;
    private String password;

    /**
     * Constructor
     *
     * @param id
     * @param name
     * @param password
     */
    public User(int id, String name, String password){
        this.user_id = id;
        this.user_name = name;
        this.password = password;
    }

    /**
     * @return the user id
     */
    public int getId(){ return this.user_id; }

    /**
     * @param id the id to set
     */
    public void setId(int id){ this.user_id = id; }

    /**
     * @return the username
     */
    public String getUserName(){ return this.user_name; }

    /**
     * @param name the username to set
     */
    public void setUserName(String name){ this.user_name = name; }

    /**
     * @return the password
     */
    public String getPassword(){ return this.password; }

    /**
     * @param password the password to set
     */
    public void setPassword(String password){ this.password = password; }

    public static ObservableList<User> getUsers(){
        return getAllUsers();
    }

    /**
     * Overrides the toString method to customize display in comboBox
     * @return
     */
    @Override
    public String toString(){ return (Integer.toString(this.user_id) + " - " + this.user_name); }
}
