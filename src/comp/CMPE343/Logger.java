package comp.CMPE343;

public class Logger {

    public static String DEBUG = "Scene,Database";


    public static void log(String s, Object... objects){
        logPrefixed("[" + Thread.currentThread().getName() + "] : ", s, objects);
    }

    private static void logPrefixed(String prefix, String s, Object... objects){
        for (Object object : objects) {
            s = s.replaceFirst("%d", object.toString());
        }
        System.out.println(prefix + s);
    }

    public static void debugLog(String debugType, String s, Object... objects){
        boolean shouldPrint = false;
        for (String a : DEBUG.split(",")){
            if(debugType.contains(a)) shouldPrint = true;
        }

        if(shouldPrint){
            logPrefixed("[DEBUG] - [" + debugType + "] : ", s, objects);
        }
    }

}
