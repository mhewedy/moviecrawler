package mhewedy.crawler;

import java.io.IOException;
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

    List<String> getMovieNames(String url, int limit) throws IOException;

    static WebsiteCrawler getCrawler(String url){
        final WebsiteCrawler defaultCrawler = (u, l) -> Collections.emptyList();

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
}
