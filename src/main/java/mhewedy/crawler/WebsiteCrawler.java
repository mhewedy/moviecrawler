package mhewedy.crawler;

import mhewedy.beans.Movie;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by mhewedy on 5/23/14.
 */
public interface WebsiteCrawler {

    static List<WebsiteCrawler> CRAWLERS = new ArrayList<>();

    static WebsiteCrawler getCrawler(String url) throws InvalidCrawlerException {
        URI uri = URI.create(url);
        String host = uri.getHost();

        if (host != null) {
            return CRAWLERS.stream()
                    .filter(wc -> host.equalsIgnoreCase(wc.getDomain()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidCrawlerException("cannot handle website, " +
                            "use -h to print all supported websites"));
        } else {
            throw new InvalidCrawlerException("host info not correct");
        }
    }


    default void register() {
        CRAWLERS.add(this);
    }

    String getDomain();

    Set<Movie> getMovies(String url, int limit) throws IOException;
}
