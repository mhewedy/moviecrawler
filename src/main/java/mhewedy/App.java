package mhewedy;

import mhewedy.beans.Movie;
import mhewedy.crawler.InvalidCrawlerException;
import mhewedy.crawler.WebsiteCrawler;
import mhewedy.rater.WebsiteRater;

import java.io.*;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 app -cli --hosts

 app -cli <url> <outfile>

 app -cli -v <url> <outfile>
 app -cli <url> [limit] <outfile>

 app -cli -v <url> [limit] <outfile>

 app -web

 * Created by mhewedy on 6/1/14.
 */
public class App {

    public static void main(String[] args) {
        if (args.length > 0){
            if ("-cli".equals(args[0])){
                String[] target = new String[args.length -1];
                System.arraycopy(args, 1, target, 0, target.length);
                CLI.start(target);
            }else if ("-web".equals(args[0])){
                Web.start();
            }else {
                usage();
            }
        }else{
            usage();
        }
    }


    public static void processCrawler(String url, int limit, PrintStream out) {
        try {
            WebsiteCrawler crawler = WebsiteCrawler.getCrawler(url);
            Set<Movie> movies = crawler.getMovies(url, limit);

            if (movies.isEmpty()) {
                System.err.println("no movies found");
                return;
            }

            WebsiteRater rater = WebsiteRater.getRater();

            out.println("<h2>rating for : " + URLDecoder.decode(url, "utf8") +
                    "</h2> <br /> <table border=\"1\">");
            List<Movie> collect = movies.stream()
                    .parallel()
                    .peek(rater::updateMovieRating)
                    .sorted(Comparator.comparing(Movie::getRating).reversed())
                    .collect(Collectors.toList());

            collect.forEach(m -> out.println
                    ("<tr><" +
                            "td> <a href=\"" + m.getLink() + "\" target=\"_blank\">" + m.getName() + "</a></td>" +
                            "<td><a href=\"" + m.getImdbUrl() + "\"><img src=\"" + m.getPoster() + "\" /></a></td>" +
                            "<td>" + m.getRating() + "</td>" +
                            "</tr>"));
            out.println("</table>");
        } catch (InvalidCrawlerException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static void usage() {
        String out = "usage:";
        out += "\n\tmoviecrawler -cli --hosts";
        out += "\n\tmoviecrawler -cli [-v] <url> [limit] <outputfile>";
        out += "\n\tmoviecrawler -web";
        System.out.println(out);
        System.exit(-1);
    }
}
