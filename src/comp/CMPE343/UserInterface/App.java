package comp.CMPE343.UserInterface;

import comp.CMPE343.UserInterface.Buyer.Product;
import comp.CMPE343.UserInterface.Buyer.ProductPage;
import comp.CMPE343.UserInterface.Buyer.ProductPane;
import controller.LoginFormController;
import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;

import static comp.CMPE343.Logger.*;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        if(callBack != null){
            callBack.app = this;
        }
        LoginScene loginScene = new LoginScene();
        loginScene.getLoginButton().setOnAction((event) ->{
            debugLog("Scene:Login:Buttons", "Login Button Action Fired, with the event name %d", event.getEventType().getName());

            // Happens when button is pressed.
            if(event.getEventType().getName() == "ACTION"){
                debugLog("Scene:Login:Buttons", "Login Button pressed. Checking database.");
                loginScene.getLoginButton().setDisable(true);
                switch (loginScene.checkDatabase()){
                    case -1:
                        debugLog("Scene:Login", "Login Failed.");
                        loginScene.getPassword().setText("");
                        loginScene.getLoadingText().setText("\tLogin Failed...");
                        loginScene.getLoadingText().setStyle("-fx-text-fill: red");
                    case 0:

                }
            }
        });

//        Scene testScene = new ProductPage().scene;
        Scene testScene = LoginFormController.getScene();

        // Sample code taken from teacher's presentation.
        primaryStage.setTitle("Group19 GreenGrocer"); // Set the stage title
        primaryStage.setScene (testScene); // Place the scene in the stage
        primaryStage.show(); // Display the stage


    }

    public static SceneManager callBack = null;
}
