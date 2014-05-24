package mhewedy.crawler;

import mhewedy.beans.Movie;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Set;

/**
 * Created by mhewedy on 5/23/14.
 */
public interface WebsiteCrawler {

    // Put all known instances here
    static WebsiteCrawler _el7lCrawler = new El7lCrawler();
    //~

    static WebsiteCrawler getCrawler(String url) throws InvalidCrawlerException {

        URI uri = URI.create(url);
        String host = uri.getHost();
        if (host != null) {
            if (host.equalsIgnoreCase(_el7lCrawler.getDomain())) {
                return _el7lCrawler;
            } else {
                throw new InvalidCrawlerException("cannot handle website, use -h to print all supported websites");
            }
        } else {
            throw new InvalidCrawlerException("host info not correct");
        }
    }

    String getDomain();
    Set<Movie> getMovies(String url, int limit) throws IOException;
}
