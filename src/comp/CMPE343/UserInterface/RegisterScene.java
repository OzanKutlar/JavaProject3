package comp.CMPE343.UserInterface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class RegisterScene {
    private Scene scene;
    private TextField username;
    private PasswordField password;
    private Button loginButton;
    private Button registrationButton;


    // Identical Class to LoginScene, just text are different.
    public RegisterScene() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);


        // Username and password inputs and text
        Label textUsername = new Label("Username : ");
        GridPane.setConstraints(textUsername, 0, 1);

        username = new TextField();
        GridPane.setConstraints(username, 1, 1);

        Label textPassword = new Label("Password : ");
        GridPane.setConstraints(textPassword, 0, 2);

        password = new PasswordField();
        GridPane.setConstraints(password, 1, 2);

        loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 0, 3);

        registrationButton = new Button("Register");
        GridPane.setConstraints(registrationButton, 1, 3);

        grid.getChildren().addAll(textUsername, username, textPassword, password, loginButton, registrationButton);

        scene = new Scene(grid, 900, 540);
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
