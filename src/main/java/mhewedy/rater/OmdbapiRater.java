package mhewedy.rater;

import com.google.gson.Gson;

import mhewedy.Util;
import mhewedy.beans.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by mhewedy on 5/24/14.
 * http://www.omdbapi.com/
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class OmdbapiRater implements WebsiteRater {

    private static final String SEARCH_URL = "http://www.omdbapi.com/?s=$name&yg=0&r=json";
    private static final String DETAILS_URL = "http://www.omdbapi.com/?i=$imdbId&r=json";
    private static final String IMDB_URL = "http://www.imdb.com/title/$imdbId/";
    private static Gson gson = new Gson();

    @Override
    public void updateMovieRating(Movie movie) {
        try {
            String imdbId = getImdbId(movie.getName());
            if (!imdbId.isEmpty()) {
                fillMovieData(imdbId, movie);
            }
        } catch (IOException e) {
            if (e instanceof UnknownHostException){
                System.err.println("cannot resolve host: " + e.getMessage() + "\n   ");
            }else {
                System.out.println(e.getMessage());
            }
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
                Map movie = geMatchingMovie(list, movieName);
                imdbID = (String) movie.get("imdbID");
            }
        }
        Util.printVerbose(movieName + " -> " + imdbID);
        stream.close();
        conn.disconnect();
        return imdbID;
    }
    
	private Map geMatchingMovie(List list, String movieName) {
		
		list.sort(new SortByYearDescComparator());
		
		Util.printVerbose(list);
		
		Optional findFirst = list.stream()
				.filter(mov -> "movie".equalsIgnoreCase((String) (((Map) mov)
						.get("Type"))))
				.filter(mov -> movieName.equalsIgnoreCase((String) ((Map) mov)
						.get("Title"))).findFirst();
		
		return (Map) findFirst.orElse(list.get(0));
	}

    private void fillMovieData(String imdbID, Movie movie) throws IOException {
        double imdbRating = 0.0;
        String url = DETAILS_URL.replace("$imdbId", imdbID);
        Util.printVerbose(url);
        HttpURLConnection conn = Util.openConnection(url);
        InputStream stream = conn.getInputStream();
        Map map = gson.fromJson(new InputStreamReader(stream), Map.class);

        if (map != null) {
            String poster = (String) map.get("Poster");
            if (poster != null && !poster.isEmpty()){
                movie.setPoster(poster);
            }

            movie.setImdbUrl(IMDB_URL.replace("$imdbId", imdbID));

            String imdbRatingStr = (String) map.get("imdbRating");
            if (imdbRatingStr.matches("\\d+.\\d+")) {
                imdbRating = Double.parseDouble(imdbRatingStr);
                movie.setRating(imdbRating);
            }
        }
        stream.close();
        conn.disconnect();
    }
    
    class SortByYearDescComparator implements Comparator{
    	@Override
    	public int compare(Object o1, Object o2) {

			Map m1 = (Map) o1;
			Map m2 = (Map) o2;
			
			Integer year1 = null;
			Integer year2 = null;
			try {
				year1 = Integer.parseInt((String) m1.get("Year"));
			}catch(Exception ex) {
				return -1;
			}
			try {
				year2 = Integer.parseInt((String) m2.get("Year"));
			}catch(Exception ex) {
				return 1;
			}
			if (year1 == null) {
				return -1;
			}
			if (year2 == null) {
				return 1;
			}
			return year2.compareTo(year1);
		
    	}
    }
}
