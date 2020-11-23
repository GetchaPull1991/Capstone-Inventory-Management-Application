package Model;

import java.time.LocalDate;

/** Class for creating and managing Users */
public class User {

    private Integer userId;
    private String username;
    private String password;
    private String privileges;
    private final LocalDate createdDate;
    private final String createdBy;
    private LocalDate modifiedDate;
    private String modifiedBy;

    /**
     * Create new User
     * @param username the username to set
     * @param password the password to set
     * @param privileges the privileges to set
     */
    public User(int userId, String username, String password, String privileges, LocalDate createdDate, String createdBy, LocalDate modifiedDate, String modifiedBy){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.privileges = privileges;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.modifiedDate = modifiedDate;
        this.modifiedBy = modifiedBy;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the privileges
     */
    public String getPrivileges() {
        return privileges;
    }

    /**
     * @param privileges the privileges to set
     */
    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    /**
     * @return the user id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the user id to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the date the user was created
     */
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    /**
     * @return the date the user was modified
     */
    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    /**
     * @return the user that created this user
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @return the user that modified this user
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy the user that modified this user to set
     */
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * @param modifiedDate the date this user was modified
     */
    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
