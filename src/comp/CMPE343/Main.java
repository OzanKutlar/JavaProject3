package comp.CMPE343;

import comp.CMPE343.Database.DatabaseConnector;
import comp.CMPE343.UserInterface.SceneManager;

import static comp.CMPE343.Logger.*;

public class Main {


    public static void main(String[] args){
        log("Main Thread Started.");

        SceneManager.startUI(args);

        for (int i = 0; i < 19; i++) {
            log("Test %d", i);
        }

        log("Main Thread Ended.");

    }
}
