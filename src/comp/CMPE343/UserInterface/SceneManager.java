package comp.CMPE343.UserInterface;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static comp.CMPE343.Logger.*;



public class SceneManager {

    public static Thread UIThread = null;

    public static void startUI(String... args){
        if(UIThread == null){
            try{
                UIThread = new Thread(() -> {
                    instance = new SceneManager(args);
                });
                UIThread.run();
            }catch (Exception e){
                debugLog("Scene:Exception", "An exception occured, with the message : %d\nWith the stack trace %d", e.getMessage(), e.getStackTrace());
            }
        }
    }

    public static void stopUI(){
        if(UIThread != null){
            try {
                instance.app.stop();
                UIThread.interrupt();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static SceneManager instance;


    public App app;



    private SceneManager(String... args){
        app = new App(args);
    }

}
