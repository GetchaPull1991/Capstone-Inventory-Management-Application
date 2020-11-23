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

        connect();
        //Initialize user
        User user = null;

        try (Statement statement = connection.createStatement()) {

            //Query to retrieve user
            String userQuery = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "';";

            //Get result
            ResultSet userResultSet = statement.executeQuery(userQuery);

            //Create user
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
        disconnect();
        return user;
    }

    public static ObservableList<User> getAllUsers() {

        connect();
        //Initialize user
        ObservableList<User> users = FXCollections.observableArrayList();

        try (Statement statement = connection.createStatement()) {

            //Get result
            ResultSet userResultSet = statement.executeQuery("SELECT * FROM users;");

            //Create user
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
        disconnect();
        return users;
    }

    public static boolean usernameInUse(String username) {

        //Connect to the database
        connect();

        //Store boolean reault
        boolean usernameIsInUse = false;

        try (Statement statement = connection.createStatement()) {

            //Query to retrieve user
            String userQuery = "SELECT * FROM users WHERE username = '" + username + "';";

            //Get result
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

    public static void addNewUser(User user){

        connect();

        try(Statement statement = connection.createStatement()){

            String insertQuery = "INSERT INTO users(user_Id, username, password, created_date, created_by, last_modified_date, last_modified_by, privileges) " +
                                "VALUES('" + user.getUserId() + "', '" + user.getUsername() +
                                "', '" + user.getPassword() + "', '" + user.getCreatedDate() + "', '" + user.getCreatedBy() + "', '" +
                                user.getModifiedDate() +"', '" + user.getModifiedBy() + "', '" + user.getPrivileges() + "');";

            statement.executeUpdate(insertQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void deleteUser(User user){

        connect();

        try(Statement statement = connection.createStatement()) {
            String deleteQuery = "DELETE FROM users WHERE user_id = '" + user.getUserId() + "';";
            statement.executeUpdate(deleteQuery);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static User getUserById(int id) {

        connect();
        //Initialize user
        User user = null;

        try (Statement statement = connection.createStatement()) {

            //Query to retrieve user
            String userQuery = "SELECT * FROM users WHERE user_id = '" + id + "';";

            //Get result
            ResultSet userResultSet = statement.executeQuery(userQuery);

            //Create user
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
        disconnect();
        return user;
    }

    public static void updateUserPassword(User user){

        connect();

        try (Statement statement = connection.createStatement()){
            String updateQuery = "UPDATE users SET password = '" + user.getPassword() +
                                 "', last_modified_by = '" + user.getUsername() +
                                 "', last_modified_date = '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                                 "' WHERE user_id = '" + user.getUserId() + "';";

            statement.executeUpdate(updateQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        disconnect();
    }

    public static void updateUserPrivileges(User user){

        connect();

        try (Statement statement = connection.createStatement()){
            String updateQuery = "UPDATE users SET privileges = '" + user.getPrivileges() +
                    "', last_modified_by = '" + MainBorderPaneController.currentUser.getUsername() +
                    "', last_modified_date = '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                    "' WHERE user_id = '" + user.getUserId() + "';";

            statement.executeUpdate(updateQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        disconnect();
    }
}
