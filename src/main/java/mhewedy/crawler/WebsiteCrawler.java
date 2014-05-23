package mhewedy.crawler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by mhewedy on 5/23/14.
 */
public interface WebsiteCrawler {

    // Put all known instances here
    static WebsiteCrawler _el7lCrawler = new El7lCrawler();
    //~

    default String getDomain() {
        return "invalid url";
    }

    java.util.Set<mhewedy.Movie> getMovieNames(String url, int limit) throws IOException;

    static WebsiteCrawler getCrawler(String url){
        final WebsiteCrawler defaultCrawler = (u, l) -> Collections.emptySet();

        URI uri = URI.create(url);
        String host = uri.getHost();
        if (host != null){
            if (host.equalsIgnoreCase(_el7lCrawler.getDomain())){
                return _el7lCrawler;
            }else {
                return defaultCrawler;
            }
        }else{
            return defaultCrawler;
        }
    }

    default HttpURLConnection openConnection(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", "moviecrawler");
        return conn;
    }
}
