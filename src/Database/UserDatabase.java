package Database;

import Model.User;
import Controller.MainBorderPaneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Class for managing all database operations involving Users */
public class UserDatabase extends Database {


    /**
     * Return the User from the database if present
     *
     * @param username the username of the User
     * @param password the password of the User
     * @return the User
     */
    public static User getUser(String username, String password) {

        //Connect to the database
        connect();

        //Create the user
        User user = null;

        //Get the user that matches the credentials provided
        try (Statement statement = connection.createStatement()) {
            String userQuery = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "';";
            ResultSet userResultSet = statement.executeQuery(userQuery);

            //Set user to result
            while (userResultSet.next()) {
                user = new User(userResultSet.getInt("user_Id"),
                        userResultSet.getString("username"),
                        userResultSet.getString("password"),
                        userResultSet.getString("privileges"),
                        userResultSet.getDate("created_date").toLocalDate(),
                        userResultSet.getString("created_by"),
                        userResultSet.getDate("last_modified_date").toLocalDate(),
                        userResultSet.getString("last_modified_by"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from database
        disconnect();

        //Return the user
        return user;
    }

    /**
     * Get a list of all users from the database
     * @return the list of users
     */
    public static ObservableList<User> getAllUsers() {

        //Connect to the database
        connect();

        //Create the list of users
        ObservableList<User> users = FXCollections.observableArrayList();

        //Get all users from the database
        try (Statement statement = connection.createStatement()) {
            ResultSet userResultSet = statement.executeQuery("SELECT * FROM users;");

            //Add users to list
            while (userResultSet.next()) {
                users.add(new User(userResultSet.getInt("user_Id"),
                        userResultSet.getString("username"),
                        userResultSet.getString("password"),
                        userResultSet.getString("privileges"),
                        userResultSet.getDate("created_date").toLocalDate(),
                        userResultSet.getString("created_by"),
                        userResultSet.getDate("last_modified_date").toLocalDate(),
                        userResultSet.getString("last_modified_by")));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from database
        disconnect();

        //Return list of users
        return users;
    }

    /**
     * Get users that match the search criteria provided
     * @param searchCriteria the search criteria to search with
     * @return the list of users
     */
    public static ObservableList<User> searchUsers(String searchCriteria) {

        //Connect to the database
        connect();

        //Create user list
        ObservableList<User> users = FXCollections.observableArrayList();

        //Get users matching search criteria
        try (Statement statement = connection.createStatement()) {
            String searchQuery = "SELECT * " +
                                 "FROM users " +
                                 "WHERE " +
                                 "user_id LIKE '%" + searchCriteria + "%' OR " +
                                 "username LIKE '%" + searchCriteria + "%' OR " +
                                 "created_date LIKE '%" + searchCriteria + "%' OR " +
                                 "created_by LIKE '%" + searchCriteria + "%' OR " +
                                 "last_modified_date LIKE CONCAT('%', DATE_FORMAT('" + searchCriteria + "', '%Y-%m-%d' ), '%') OR " +
                                 "last_modified_by LIKE CONCAT('%', DATE_FORMAT('" + searchCriteria + "', '%Y-%m-%d' ), '%') OR " +
                                 "privileges LIKE '%" + searchCriteria + "%';";
            //Get result
            ResultSet userResultSet = statement.executeQuery(searchQuery);

            //Add users to list
            while (userResultSet.next()) {
                users.add(new User(userResultSet.getInt("user_Id"),
                        userResultSet.getString("username"),
                        userResultSet.getString("password"),
                        userResultSet.getString("privileges"),
                        userResultSet.getDate("created_date").toLocalDate(),
                        userResultSet.getString("created_by"),
                        userResultSet.getDate("last_modified_date").toLocalDate(),
                        userResultSet.getString("last_modified_by")));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();

        //Return the list of users
        return users;
    }

    /**
     * Get user by id
     * @param userId the user id to search for
     * @return the user
     */
    public static User getUserById(int userId) {

        //Connect to the database
        connect();

        //Create new user
        User user = null;

        //Get the user from the database
        try (Statement statement = connection.createStatement()) {
            String userQuery = "SELECT * FROM users WHERE user_id = '" + userId + "';";
            ResultSet userResultSet = statement.executeQuery(userQuery);

            //Set the user to the result
            while (userResultSet.next()) {
                user = new User(userResultSet.getInt("user_Id"),
                        userResultSet.getString("username"),
                        userResultSet.getString("password"),
                        userResultSet.getString("privileges"),
                        userResultSet.getDate("created_date").toLocalDate(),
                        userResultSet.getString("created_by"),
                        userResultSet.getDate("last_modified_date").toLocalDate(),
                        userResultSet.getString("last_modified_by"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();

        //Return the user
        return user;
    }

    /**
     * Check if username is in use
     * @param username the username to check
     * @return the boolean result of the check
     */
    public static boolean usernameInUse(String username) {

        //Connect to the database
        connect();

        //Store boolean result
        boolean usernameIsInUse = false;

        //Retrive the user
        try (Statement statement = connection.createStatement()) {
            String userQuery = "SELECT * FROM users WHERE username = '" + username + "';";
            ResultSet userResultSet = statement.executeQuery(userQuery);

            //If the query returns a result, the username is in use
            while (userResultSet.next()) {
                usernameIsInUse = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();

        //Return the result
        return usernameIsInUse;
    }

    /**
     * Add new user to the database
     * @param user the user to add
     */
    public static void addNewUser(User user){

        //Connect to the database
        connect();

        //Insert the new user into the database
        try(Statement statement = connection.createStatement()){
            String insertQuery = "INSERT INTO users(user_Id, username, password, created_date, created_by, last_modified_date, last_modified_by, privileges) " +
                                 "VALUES('" +
                                 user.getUserId() + "', '" +
                                 user.getUsername() + "', '" +
                                 user.getPassword() + "', '" +
                                 user.getCreatedDate() + "', '" +
                                 user.getCreatedBy() + "', '" +
                                 user.getModifiedDate() +"', '" +
                                 user.getModifiedBy() + "', '" +
                                 user.getPrivileges() + "');";
            statement.executeUpdate(insertQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();
    }

    /**
     * Delete user from database
     * @param user the user to delete
     */
    public static void deleteUser(User user){

        //Connect to the database
        connect();

        //Delete the user from the database
        try(Statement statement = connection.createStatement()) {
            String deleteQuery = "DELETE FROM users WHERE user_id = '" + user.getUserId() + "';";
            statement.executeUpdate(deleteQuery);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();
    }

    /**
     * Update the users password
     * @param user the user to update
     */
    public static void updateUserPassword(User user){

        //Connect to the datbase
        connect();

        //Update the users password
        try (Statement statement = connection.createStatement()){
            String updateQuery = "UPDATE users SET password = '" + user.getPassword() +
                                 "', last_modified_by = '" + user.getUsername() +
                                 "', last_modified_date = '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                                 "' WHERE user_id = '" + user.getUserId() + "';";

            statement.executeUpdate(updateQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();
    }

    /**
     * Update user privileges
     * @param user the user to update
     */
    public static void updateUserPrivileges(User user){

        //Connect to the database
        connect();

        //Update the users privileges
        try (Statement statement = connection.createStatement()){
            String updateQuery = "UPDATE users SET privileges = '" + user.getPrivileges() +
                                 "', last_modified_by = '" + MainBorderPaneController.currentUser.getUsername() +
                                 "', last_modified_date = '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                                 "' WHERE user_id = '" + user.getUserId() + "';";
            statement.executeUpdate(updateQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();
    }
}
