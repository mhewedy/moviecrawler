package mhewedy.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * for http://el7l.co/ <br />
 * Created by mhewedy on 5/23/14.
 */
public class El7lCrawler implements WebsiteCrawler {

    private static String MOVIE_LINE_START = "<p style=\"font-size: 14px; font-family:Custom;\">";
    private static String MOVIE_LINE_END = "</p>";
    private static String FILM = "فيلم";


    @Override
    public String getDomain() {
        return "el7l.co";
    }

    @Override
    public List<String> getMovieNames(String url, int limit) throws IOException {

        List<String> ret = new ArrayList<>();

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", "moviecrawler");
        InputStream stream = conn.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

        List<String> movies = _getMovieNames(br.lines());
        ret.addAll(movies);

        System.out.println(ret);

        stream.close();
        conn.disconnect();
        return ret;
    }

    private List<String> _getMovieNames(Stream<String> lines) {
        return
                lines.filter(line -> line.contains(MOVIE_LINE_START))
                        .map(line -> line.substring(line.indexOf(MOVIE_LINE_START) + MOVIE_LINE_START.length(),
                                line.indexOf(MOVIE_LINE_END)))
                        .map(name -> name.replace(FILM, ""))
                        .map(name -> name.trim())
                        .collect(Collectors.toList());
    }
}
