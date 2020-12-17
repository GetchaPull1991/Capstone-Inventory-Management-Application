package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Database {

    public static Connection connection = null;

    /**Connect to the database*/
    public static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://sdev-capstone-db.cocnamndjjhc.us-east-2.rds.amazonaws.com:3306/capstone_inventory_application",
                    "admin", "adminwgu");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**Disconnect from the database*/
    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
