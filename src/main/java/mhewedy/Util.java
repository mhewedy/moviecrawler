package mhewedy;

/**
 * Created by mhewedy on 5/23/14.
 */
public class Util {

    public static boolean isVerbose(){
        return false;
    }

    public static void printVerbose(Object o){
        if (isVerbose()) {
            System.out.println(o);
        }
    }
}
