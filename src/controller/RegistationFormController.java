package controller;

import comp.CMPE343.Database.DatabaseConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class RegistationFormController  {
    public TextField txtfullName;
    public TextField txtMobile;
    public PasswordField txtConfirmPassword;
    public AnchorPane pane;
    public PasswordField txtPassword;
    public Label lblpassword1;
    public Label lblpassword2;
    public Label lblAutoID;


    public void initialize() {
        lblpassword1.setVisible(false);
        lblpassword2.setVisible(false);

        try {

            AutoID();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void AutoID() throws SQLException {
        Connection connection = DatabaseConnector.instance.connection;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select id from user order by id desc limit 1");
        boolean a = resultSet.next();

        if(a)
        {
            String uid = resultSet.getString(1);
            uid = uid.substring(1,4);
            int intID = Integer.parseInt(uid);

            intID++;


            if(intID < 10)
            {


                lblAutoID.setText("U00" + intID);
            }
            else if(intID < 100)
            {


                lblAutoID.setText("U0" + intID);
            }
            else
            {

                lblAutoID.setText("U" + intID);
            }
        }

        else
        {
            lblAutoID.setText("U001");

        }

    }


    public void btnSignup(ActionEvent actionEvent) throws IOException, SQLException {


        register();

        DatabaseConnector object = DatabaseConnector.instance;
        System.out.println(object);




    }

    public void txtConfirmPasswordOnAction(ActionEvent actionEvent) throws SQLException {
        register();

    }


    public void register() throws SQLException {
        String newPassword = txtPassword.getText();
        String ConfirmPassword = txtConfirmPassword.getText();

        if(newPassword.equals(ConfirmPassword))
        {
            setBorderColor("transparent");
            lblpassword1.setVisible(false);
            lblpassword2.setVisible(false);
            txtPassword.requestFocus();

            try {
                Connection connection = DatabaseConnector.instance.connection;


                String id = lblAutoID.getText();

                String name = txtfullName.getText();

                String mobile = txtMobile.getText();
                String cpass = txtConfirmPassword.getText();

                PreparedStatement preparedStatement = connection.prepareStatement("insert into user(id,username,email,password)values(?,?,?,?) ");
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, mobile);
                preparedStatement.setString(4, cpass);

                int i = preparedStatement.executeUpdate();

                if (i != 0) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Success");
                    alert.showAndWait();

                    Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LoginForm.fxml"));
                    Scene scene = new Scene(parent);

                    Stage primarystage = (Stage) pane.getScene().getWindow();

                    primarystage.setScene(scene);
                    primarystage.setTitle("Login Form");
                    primarystage.centerOnScreen();


                }

            }
            catch (SQLException | IOException ex)
            {
                ex.printStackTrace();
            }




        }
        else
        {
            setBorderColor("red");
            lblpassword1.setVisible(true);
            lblpassword2.setVisible(true);
            txtPassword.requestFocus();
        }

    }

    public void setBorderColor(String color)
    {
        txtPassword.setStyle(" -fx-border-color: " + color);
        txtConfirmPassword.setStyle(" -fx-border-color: " + color);
    }



}