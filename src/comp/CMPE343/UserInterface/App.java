package comp.CMPE343.UserInterface;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        if(callBack != null){
            callBack.app = this;
        }
        Button btOK = new Button("OK");
        Scene scene = new Scene(btOK, 200, 250);
        primaryStage.setTitle("MyJavaFX"); // Set the stage title
        primaryStage.setScene (scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }

    public static SceneManager callBack = null;
}
