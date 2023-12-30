package comp.CMPE343;

import comp.CMPE343.Database.DatabaseConnector;
import comp.CMPE343.UserInterface.SceneManager;

import static comp.CMPE343.Logger.*;

public class Main {


    public static void main(String[] args){
        log("Thread Started.");

        SceneManager.startUI(args);

        log("Thread Ended.");

    }
}
