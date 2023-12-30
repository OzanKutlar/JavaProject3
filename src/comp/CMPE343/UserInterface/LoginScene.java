package comp.CMPE343.UserInterface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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


        // Username and password inputs and text
        Label textUsername = new Label("Username : ");
        GridPane.setConstraints(textUsername, 0, 0);

        username = new TextField();
        GridPane.setConstraints(username, 1, 0);

        Label textPassword = new Label("Password : ");
        GridPane.setConstraints(textPassword, 0, 1);

        password = new PasswordField();
        GridPane.setConstraints(password, 1, 1);

        loadingText = new Label("");
        GridPane.setConstraints(loadingText, 0, 2);

        loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 0, 3);

        registrationButton = new Button("Register");
        GridPane.setConstraints(registrationButton, 1, 3);

        grid.getChildren().addAll(textUsername, username, textPassword, password, loginButton, registrationButton, loadingText);

        scene = new Scene(grid, 900, 540);
    }


    /**
     * Takes in no arguments as it is an inner function that can access the username and password fields by itself.
     * @return -1 for not found, 0 for admin, 1 for user, 2 for driver.
     */

    public int callBackData = -2;

    public int checkDatabase(){

        return -1;
    }

    public Scene getScene() {
        return scene;
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
