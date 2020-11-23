package Database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class Database {

    public static Connection connection = null;

    /**Connect to the database*/
    public static void connect() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("C:\\Users\\jsmit\\IdeaProjects\\InventoryApplicationCapstone\\src\\Resources\\config.properties"));
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/capstone_inventory_application",
                                                    properties.getProperty("localDatabaseUser"),
                                                    properties.getProperty("localDatabasePassword"));
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
