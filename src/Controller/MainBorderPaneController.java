package Controller;

import Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainBorderPaneController implements Initializable {
    @FXML
    public Button dashboardButton;
    @FXML
    public Button customersButton;
    @FXML
    public Button ordersButton;
    @FXML
    public Button productsButton;
    @FXML
    public Button partsButton;
    @FXML
    public Button usersButton;
    @FXML
    public Button aboutButton;
    @FXML
    public VBox sideMenuVbox;
    @FXML
    public BorderPane mainUIBorderPane;
    @FXML
    public Button reportsButton;
    @FXML
    public BorderPane contentBorderPane;
    @FXML
    public Label headerLabel;
    @FXML
    public Button logoutButton;
    @FXML
    public Button changePasswordButton;

    Alert aboutDialog = new Alert(Alert.AlertType.INFORMATION);
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);

    public static User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setSideMenu();
        setSceneEventHandlers();
        checkUserPrivileges();
    }

    /** Set the icons and format for the side menu */
    private void setSideMenu(){

        //Set the icons and sizes for the menu
        customersButton.setContentDisplay(ContentDisplay.TOP);
        ImageView customerIcon = new ImageView(new Image(this.getClass().getResource("/Resources/Icons/customersIcon1.png").toExternalForm(), 90, 90, false, false));
        customerIcon.setFitHeight(40);
        customerIcon.setFitWidth(40);
        customersButton.setGraphic(customerIcon);

        ordersButton.setContentDisplay(ContentDisplay.TOP);
        ImageView ordersIcon = new ImageView(new Image(this.getClass().getResource("/Resources/Icons/orderIcon1.png").toExternalForm(), 100, 100, false, false));
        ordersIcon.setFitHeight(40);
        ordersIcon.setFitWidth(40);
        ordersButton.setGraphic(ordersIcon);

        productsButton.setContentDisplay(ContentDisplay.TOP);
        ImageView productsIcon = new ImageView(new Image(this.getClass().getResource("/Resources/Icons/productsIcon1.png").toExternalForm(), 100, 100, false, false));
        productsIcon.setFitHeight(40);
        productsIcon.setFitWidth(40);
        productsButton.setGraphic(productsIcon);

        partsButton.setContentDisplay(ContentDisplay.TOP);
        ImageView partsIcon = new ImageView(new Image(this.getClass().getResource("/Resources/Icons/partsIcon1.png").toExternalForm(), 100, 100, false, false));
        partsIcon.setFitHeight(40);
        partsIcon.setFitWidth(40);
        partsButton.setGraphic(partsIcon);

        usersButton.setContentDisplay(ContentDisplay.TOP);
        ImageView usersIcon = new ImageView(new Image(this.getClass().getResource("/Resources/Icons/userIcon1.png").toExternalForm(), 96, 96, false, false));
        usersIcon.setFitHeight(40);
        usersIcon.setFitWidth(40);
        usersButton.setGraphic(usersIcon);

        reportsButton.setContentDisplay(ContentDisplay.TOP);
        ImageView reportsIcon = new ImageView(new Image(this.getClass().getResource("/Resources/Icons/reportsIcon1.png").toExternalForm(), 100, 100, false, false));
        reportsIcon.setFitHeight(40);
        reportsIcon.setFitWidth(40);
        reportsButton.setGraphic(reportsIcon);

        aboutButton.setContentDisplay(ContentDisplay.TOP);
        ImageView aboutIcon = new ImageView(new Image(this.getClass().getResource("/Resources/Icons/infoIcon1.png").toExternalForm(), 128, 128, false, false));
        aboutIcon.setFitHeight(40);
        aboutIcon.setFitWidth(40);
        aboutButton.setGraphic(aboutIcon);

        logoutButton.setContentDisplay(ContentDisplay.TOP);
        ImageView logoutIcon = new ImageView(new Image(this.getClass().getResource("/Resources/Icons/logoutIcon.png").toExternalForm(), 90, 90, false, false));
        logoutIcon.setFitHeight(40);
        logoutIcon.setFitWidth(40);
        logoutButton.setGraphic(logoutIcon);

        changePasswordButton.setContentDisplay(ContentDisplay.TOP);
        ImageView changePasswordIcon = new ImageView(new Image(this.getClass().getResource("/Resources/Icons/changePasswordIcon.png").toExternalForm(), 80, 80, false, false));
        changePasswordIcon.setFitHeight(40);
        changePasswordIcon.setFitWidth(40);
        changePasswordButton.setGraphic(changePasswordIcon);

        //Bind the width of the icons to the width of the side menu vbox
        customersButton.prefWidthProperty().bind(sideMenuVbox.widthProperty());
        ordersButton.prefWidthProperty().bind(sideMenuVbox.widthProperty());
        productsButton.prefWidthProperty().bind(sideMenuVbox.widthProperty());
        partsButton.prefWidthProperty().bind(sideMenuVbox.widthProperty());
        usersButton.prefWidthProperty().bind(sideMenuVbox.widthProperty());
        aboutButton.prefWidthProperty().bind(sideMenuVbox.widthProperty());
        reportsButton.prefWidthProperty().bind(sideMenuVbox.widthProperty());
        logoutButton.prefWidthProperty().bind(sideMenuVbox.widthProperty());
        changePasswordButton.prefWidthProperty().bind(sideMenuVbox.widthProperty());

    }

    private void checkUserPrivileges(){
        if (!currentUser.getPrivileges().equals("ADMIN")){
            sideMenuVbox.getChildren().remove(usersButton);
        }
    }

    private void logout(){

        confirmationAlert.setTitle("Logout");
        confirmationAlert.setHeaderText("Logout");
        confirmationAlert.setContentText("Are you sure you want to logout?");
        confirmationAlert.showAndWait().ifPresent(response -> {
            if(response.equals(ButtonType.OK)){
                try{
                    Parent root = FXMLLoader.load(getClass().getResource("/View/LoginDialog.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root, 400, 225);
                    scene.getStylesheets().add(Main.stylesheetUrl.toExternalForm());
                    stage.setScene(scene);
                    stage.setTitle("Capstone Inventory Management Login");

                    //Get current stage and close
                    Stage currentStage = (Stage) logoutButton.getScene().getWindow();
                    currentStage.close();

                    //Show login form
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });


    }

    private void showChangePasswordDialog(){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/View/ChangePasswordDialog.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root, 400, 390);
            scene.getStylesheets().add(Main.stylesheetUrl.toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Change Password");

            //Show login form
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /** Set the event handlers to handle scene changes */
    private void setSceneEventHandlers(){

        //Open the customers form
        customersButton.setOnAction(e ->{
            try {
                headerLabel.setText("Customers");
                contentBorderPane.setCenter(FXMLLoader.load(getClass().getResource("/View/Customers.fxml")));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //Open the parts form
        partsButton.setOnAction(e ->{
            try {
                headerLabel.setText("Parts");
                contentBorderPane.setCenter(FXMLLoader.load(getClass().getResource("/View/Parts.fxml")));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //Open the orders form
        ordersButton.setOnAction(e ->{
            try {
                headerLabel.setText("Orders");
                contentBorderPane.setCenter(FXMLLoader.load(getClass().getResource("/View/Orders.fxml")));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //Open the products form
        productsButton.setOnAction(e ->{
            try {
                headerLabel.setText("Products");
                contentBorderPane.setCenter(FXMLLoader.load(getClass().getResource("/View/Products.fxml")));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //Open the About dialog
        aboutButton.setOnAction(e -> {
            aboutDialog.setTitle("About");
            aboutDialog.setHeaderText("About");
            aboutDialog.setContentText("Author: Joseph Smith\nJDK Version: 11.0.8\nJavaFX Version: 11.0.2\nIcons Source: All icons sourced from https://icons8.com/");
            aboutDialog.show();
        });

        usersButton.setOnAction( e -> {
            try {
                headerLabel.setText("Users");
                contentBorderPane.setCenter(FXMLLoader.load(getClass().getResource("/View/Users.fxml")));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        logoutButton.setOnAction(e -> logout());

        changePasswordButton.setOnAction(e -> showChangePasswordDialog());

    }


}
