package comp.CMPE343.UserInterface;


import javafx.application.Application;

import static comp.CMPE343.Logger.debugLog;
import static comp.CMPE343.Logger.log;



public class SceneManager {

    public static Thread UIThread = null;

    public static void startUI(String... args){
        if(UIThread == null){
            try{
                UIThread = new Thread(() -> {
                    log("UI Thread Begun.");
                    instance = new SceneManager(args);
                    log("UI Thread Ended.");
                });
                UIThread.setName("UserInterface");
                log("Starting UI Thread");
                UIThread.start();
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
        App.callBack = this;
        Application.launch(App.class, args);
    }

}
