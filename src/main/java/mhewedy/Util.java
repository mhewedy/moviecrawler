package mhewedy;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mhewedy on 5/23/14.
 */
public class Util {

    private static boolean _isVerbose = false;

    public static void setVerbose(boolean isVerbose){
        _isVerbose = isVerbose;
    }
    public static boolean isVerbose(){
        return _isVerbose;
    }

    public static void printVerbose(Object o){
        if (isVerbose()) {
            System.out.println(o);
        }
    }

    public static HttpURLConnection openConnection(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", "moviecrawler");
        return conn;
    }
}
