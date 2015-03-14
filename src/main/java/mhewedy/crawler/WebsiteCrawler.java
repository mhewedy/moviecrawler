package mhewedy.crawler;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import mhewedy.beans.Movie;

/**
 * Created by mhewedy on 5/23/14.
 */
public abstract class WebsiteCrawler {

    private static List<WebsiteCrawler> CRAWLERS = new ArrayList<>();

    public static WebsiteCrawler getCrawler(String url) throws InvalidCrawlerException {
		url = url.substring(0, url.indexOf("/", url.indexOf("://") + 3));
		System.out.println("URL:" + url);
        URI uri = URI.create(url);
        String host = uri.getHost();

        if (host != null) {
            return CRAWLERS.stream()
                    .filter(wc -> host.equalsIgnoreCase(wc.getDomain()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidCrawlerException("cannot handle website, " +
                            "use --hosts to print all supported websites"));
        } else {
            throw new InvalidCrawlerException("host info not correct");
        }
    }

    public static List<String> getAllSupportedDomains() {
        return CRAWLERS.stream().map(WebsiteCrawler::getDomain).collect(Collectors.toList());
    }

    static {
        CRAWLERS.add(new El7lCrawler());
    }

    public abstract String getDomain();

    public abstract Set<Movie> getMovies(String url, int limit) throws IOException;
}
