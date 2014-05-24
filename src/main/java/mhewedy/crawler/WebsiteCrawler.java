package mhewedy.crawler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

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

    java.util.Set<mhewedy.Movie> getMovies(String url, int limit) throws IOException;

    default HttpURLConnection openConnection(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", "moviecrawler");
        return conn;
    }
}
