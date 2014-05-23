package mhewedy.crawler;

import mhewedy.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * for http://el7l.co/ <br />
 * Created by mhewedy on 5/23/14.
 */
public class El7lCrawler implements WebsiteCrawler {

    private static String MOVIE_LINE_START = "\"><p style=\"font-size: 14px; font-family:Custom;\">";
    private static String MOVIE_LINE_END = "</p>";
    private static String FILM = "فيلم";
    private static String HREF_START = "href=\"";

    private static Movie getMovieObject(String line) {
        String name = line.substring(line.indexOf(MOVIE_LINE_START) + MOVIE_LINE_START.length(),
                line.indexOf(MOVIE_LINE_END));
        String link = line.substring(line.indexOf(HREF_START) + HREF_START.length(), line.indexOf(MOVIE_LINE_START));

        return new Movie(name, link);
    }

    @Override
    public String getDomain() {
        return "el7l.co";
    }

    @Override
    public Set<Movie> getMovieNames(String url, int limit) throws IOException {

        if (limit == -1){
            limit= Integer.MAX_VALUE;
        }

        Set<Movie> ret = new HashSet<>();
        int pageNo = 1;
        do{
            if (url.matches(".+/\\d+\\.html")){
                url = url.replaceAll("/\\d+\\.html", "/" + pageNo + ".html");
            }else{
                url += "/" + pageNo + ".html";
            }
            System.out.println("requesting " + url);
            HttpURLConnection conn = openConnection(url);
            InputStream stream = conn.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            ret.addAll(_getMovieNames(br.lines()));

            pageNo++;

            stream.close();
            conn.disconnect();
        }while (ret.size() < limit);
        return ret;
    }

    private Set<Movie> _getMovieNames(Stream<String> lines) {
        Set<Movie> movies = lines.filter(line -> line.contains(MOVIE_LINE_START))
                .map(El7lCrawler::getMovieObject)
                .map(movie -> new Movie(movie.getName().replace(FILM, "").trim(), movie.getLink()))
                .collect(Collectors.toSet());
        return movies;
    }
}
