package mhewedy.rater;

import com.google.gson.Gson;
import mhewedy.Util;
import mhewedy.beans.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by mhewedy on 5/24/14.
 * http://www.omdbapi.com/
 */
public class OmdbapiRater implements WebsiteRater {

    private static final String SEARCH_URL = "http://www.omdbapi.com/?s=$name&yg=0&r=json";
    private static final String DETAILS_URL = "http://www.omdbapi.com/?i=$imdbId&r=json";
    private static Gson gson = new Gson();

    @Override
    public void updateMovieRating(Movie movie) {
        try {
            String imdbId = getImdbId(movie.getName());
            if (!imdbId.isEmpty()) {
                movie.setRating(getRating(imdbId));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getImdbId(String movieName) throws IOException {
        String imdbID = "";

        String url = SEARCH_URL.replace("$name", URLEncoder.encode(movieName, "utf8"));
        Util.printVerbose(url);
        HttpURLConnection conn = Util.openConnection(url);
        InputStream stream = conn.getInputStream();
        Map map = gson.fromJson(new InputStreamReader(stream), Map.class);

        if (map != null) {
            List list = (List) map.get("Search");

            if (list != null && !list.isEmpty()) {
                Map title = (Map) list.get(0);
                imdbID = (String) title.get("imdbID");
            }
        }
        stream.close();
        conn.disconnect();
        return imdbID;
    }

    private double getRating(String imdbID) throws IOException {
        double imdbRating = 0.0;
        String url = DETAILS_URL.replace("$imdbId", imdbID);
        Util.printVerbose(url);
        HttpURLConnection conn = Util.openConnection(url);
        InputStream stream = conn.getInputStream();
        Map map = gson.fromJson(new InputStreamReader(stream), Map.class);

        if (map != null) {
            String imdbRatingStr = (String) map.get("imdbRating");
            if (imdbRatingStr.matches("\\d+.\\d+")) {
                imdbRating = Double.parseDouble(imdbRatingStr);
            }
        }
        stream.close();
        conn.disconnect();
        return imdbRating;
    }
}
