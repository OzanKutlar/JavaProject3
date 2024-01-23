package comp.CMPE343.UserInterface;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class LoginScene {
    private Scene scene;
    private TextField username;
    private PasswordField password;
    private Button loginButton;
    private Button registrationButton;
    private Label loadingText;

    public LoginScene() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
//        grid.setPadding(new Insets(25,25,25,25));
        grid.setAlignment(Pos.CENTER);

        // Username and password inputs and text
        Label textUsername = new Label("Username : ");
        username = new TextField();
        GridPane.setConstraints(textUsername, 0, 0);
        GridPane.setConstraints(username, 1, 0);
        GridPane.setHalignment(textUsername, HPos.CENTER);
        GridPane.setHalignment(username, HPos.CENTER);

        Label textPassword = new Label("Password : ");
        password = new PasswordField();
        GridPane.setConstraints(textPassword, 0, 1);
        GridPane.setConstraints(password, 1, 1);
        GridPane.setHalignment(textPassword, HPos.CENTER);
        GridPane.setHalignment(password, HPos.CENTER);

        loadingText = new Label("");

        GridPane.setConstraints(loadingText, 0, 2);
        GridPane.setHalignment(loadingText, HPos.CENTER);

        loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 0, 3);
        GridPane.setHalignment(loginButton, HPos.CENTER);
        loginButton.setStyle("-fx-background-color: #0077C2; -fx-text-fill: white;");
        loginButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                loginButton.setStyle("-fx-background-color: #82c2ff; -fx-text-fill: white;");
            } else {
                loginButton.setStyle("-fx-background-color: #0077C2; -fx-text-fill: white;");
            }
        });



        registrationButton = new Button("Register");
        GridPane.setConstraints(registrationButton, 1, 3);
        GridPane.setHalignment(registrationButton, HPos.CENTER);
        registrationButton.setStyle("-fx-background-color: #00c237; -fx-text-fill: white;");

        grid.getChildren().addAll(textUsername, username, textPassword, password, loginButton, registrationButton, loadingText);

        scene = new Scene(grid, 900, 540);
    }


    public int callBackData = -2;


    /**
     * Takes in no arguments as it is an inner function that can access the username and password fields by itself.
     * @return -1 for not found, 0 for admin, 1 for user, 2 for driver.
     */
    public int checkDatabase(){
        callBackData = -2;




        return callBackData;
    }



    public Scene getScene() {
        return scene;
    }

    public Label getLoadingText() {
        return loadingText;
    }

    public TextField getUsername() {
        return username;
    }

    public PasswordField getPassword() {
        return password;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getRegistrationButton() {
        return registrationButton;
    }
}
