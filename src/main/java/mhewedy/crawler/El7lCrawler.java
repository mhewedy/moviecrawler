package mhewedy.crawler;

import mhewedy.Util;
import mhewedy.beans.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * for http://el7l.co/ <br />
 * Created by mhewedy on 5/23/14.
 */
public class El7lCrawler extends WebsiteCrawler {

    private static String MOVIE_LINE_START = "\"><p style=\"font-size: 14px; font-family:Custom;\">";
    private static String MOVIE_LINE_END = "</p>";
    private static String FILM = "فيلم";
    private static String HREF_START = "href=\"";
    private static String QUALITY = "بجود";

    private static Movie getMovieObject(String line) {
        // extract movie name
        String name = line.substring(line.indexOf(MOVIE_LINE_START) + MOVIE_LINE_START.length(),
                line.indexOf(MOVIE_LINE_END));
        String link = line.substring(line.indexOf(HREF_START) + HREF_START.length(), line.indexOf(MOVIE_LINE_START));

        // remove invalid work بجودة
        if (name.contains(QUALITY)) {
            name = name.substring(0, name.indexOf(QUALITY));
        }
        // remove date from movie name
        if (name.matches(".*(19|20)\\d{2}.*")){
            name.replaceAll("(19|20)\\d{2}", "");
        }
        return new Movie(name, link);
    }

    @Override
    public String getDomain() {
        return "el7l.co";
    }

    @Override
    public Set<Movie> getMovies(String url, int limit) throws IOException {
        if (limit == -1) {
            limit = Integer.MAX_VALUE;
        }

        final int pageSize = 40;    // page in el7l.co contains 40 movie
        final int numRequests = (int) Math.floor(limit / pageSize);
        Util.printVerbose("numRequests: " + numRequests);

        Set<Movie> ret = new HashSet<>();

        ExecutorService executorService = Executors.newCachedThreadPool();

        List<Callable<Set<Movie>>> callables = new ArrayList<>();
        for (int i=0; i<numRequests; i++){
            Util.printVerbose("creating callable #" + i);
            int iRef = i;
            callables.add(() -> getMoviesPerPage(url, iRef));
        }

        try {
            Util.printVerbose("invoking all");
            List<Future<Set<Movie>>> futures = executorService.invokeAll(callables);

            for (Future<Set<Movie>> future : futures){
                Util.printVerbose("waiting for future");
                ret.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println(e.getMessage());
        }

        Util.printVerbose("done create movie list");

        return ret;
    }

    private Set<Movie> getMoviesPerPage(String url, int pageNo) throws IOException {
        url = getQualifiedUrl(url, pageNo);
        Util.printVerbose("requesting " + url);
        HttpURLConnection conn = Util.openConnection(url);
        InputStream stream = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        Set<Movie> movies = _getMovie(br.lines());
        stream.close();
        conn.disconnect();
        return movies;
    }

    private String getQualifiedUrl(String url, int pageNo){
        if (url.matches(".+/\\d+\\.html")) {
            url = url.replaceAll("/\\d+\\.html", "/" + pageNo + ".html");
        } else {
            url += "/" + pageNo + ".html";
        }
        return url;
    }

    private Set<Movie> _getMovie(Stream<String> lines) {
        Set<Movie> movies = lines.filter(line -> line.contains(MOVIE_LINE_START))
                .map(El7lCrawler::getMovieObject)
                .map(movie -> new Movie(movie.getName().replace(FILM, "").trim(), movie.getLink()))
                .collect(Collectors.toSet());
        return movies;
    }
}
