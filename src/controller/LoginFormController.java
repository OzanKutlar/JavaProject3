package controller;

import comp.CMPE343.Database.DatabaseConnector;
import comp.CMPE343.Main;
import comp.CMPE343.UserInterface.Buyer.ProductPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFormController {
    public TextField txtUserName;
    public PasswordField txtPassword;
    public AnchorPane root;


    public static Scene getScene() {
        try{
            Parent parent = FXMLLoader.load(LoginFormController.class.getResource("../view/loginform.fxml"));
            Scene scene = new Scene(parent);
            return scene;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }



    public void btnSignIn(ActionEvent actionEvent) {

        try {
            String Username =  txtUserName.getText();
            String Password =  txtPassword.getText();

            Connection connection = DatabaseConnector.instance.connection;
            PreparedStatement preparedStatement = connection.prepareStatement("select * from user where username =? and password = ?");
            preparedStatement.setString(1,Username);
            preparedStatement.setString(2,Password);

            ResultSet resultSet = preparedStatement.executeQuery();


            if(resultSet.next())
            {
                Main.userID = resultSet.getInt("id");
                ProductPage productPage = new ProductPage();
                Scene scene = productPage.scene;

                Stage primarystage = (Stage) root.getScene().getWindow();

                primarystage.setScene(scene);
                primarystage.centerOnScreen();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "UserName or Password Do not Match");
                alert.showAndWait();
                txtUserName.clear();
                txtPassword.clear();
                txtUserName.requestFocus();

            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public void btnSignup(ActionEvent actionEvent) throws IOException {

        Scene scene = RegistationFormController.getScene();

        Stage primaryStage = (Stage) root.getScene().getWindow();

        primaryStage.setScene(scene);

        primaryStage.setTitle("Register Form");

        primaryStage.centerOnScreen();


    }
    public void btnOwnerLogin(ActionEvent actionEvent) throws IOException {

        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/ownerloginform.fxml"));
        Scene scene = new Scene(parent);

        Stage primaryStage = (Stage) root.getScene().getWindow();

        primaryStage.setScene(scene);

        primaryStage.setTitle("Owner Form");

        primaryStage.centerOnScreen();


    }

    public void btnCarrierLogin(ActionEvent actionEvent) throws IOException {

        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/carrierloginform.fxml"));
        Scene scene = new Scene(parent);

        Stage primaryStage = (Stage) root.getScene().getWindow();

        primaryStage.setScene(scene);

        primaryStage.setTitle("Carrier Form");

        primaryStage.centerOnScreen();


    }
}
