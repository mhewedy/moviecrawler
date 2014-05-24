package mhewedy.rater;

import com.moviejukebox.imdbapi.tools.ApiBuilder;
import com.moviejukebox.imdbapi.wrapper.ResponseDetail;
import mhewedy.beans.Movie;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * for http://www.imdb.com/ <br />
 * Created by mhewedy on 5/23/14.
 */
public class ImdbRater implements WebsiteRater {
    @Override
    public Map<Movie, Integer> getMovieRatings(Set<Movie> movieNameList) {

        String url = "http://www.imdb.com/xml/find?q=Goodbye+World+2013&s=all&json=1";

        Map<String, String> opts = new HashMap<>();
        opts.put("q", "Goodbye World 2013");
        opts.put("s", "all");

        ApiBuilder.getResponse("find", opts);

        return null;
    }

    public static void main(String[] args) {
        Map<String, String> opts = new HashMap<>();
        opts.put("q", "Goodbye World 2013");
        opts.put("s", "all");

        ResponseDetail result = ApiBuilder.getResponse("find", opts);
        System.out.println(result);

    }
}
