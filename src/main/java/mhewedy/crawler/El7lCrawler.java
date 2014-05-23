package mhewedy.crawler;

import java.util.List;

/**
 *
 * for http://el7l.co/ <br />
 * Created by mhewedy on 5/23/14.
 */
public class El7lCrawler implements WebsiteCrawler {
    @Override
    public String getDomain() {
        return "el7l.co";
    }

    @Override
    public List<String> getMovieNames(String url, int limit) {

        //

        return null;
    }
}
