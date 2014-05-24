package mhewedy;

import mhewedy.beans.Movie;
import mhewedy.crawler.InvalidCrawlerException;
import mhewedy.crawler.WebsiteCrawler;
import mhewedy.rater.WebsiteRater;

import java.io.IOException;
import java.util.Comparator;
import java.util.Set;

/**
 app <url>
 app --hosts

 app -v <url>
 app <url> [limit]

 app -v <url> [limit]

 */
public class App {
    public static void main(String[] args) {

        if (args.length == 1) {
            if ("--hosts".equals(args[0])) {
                processShowHosts();
            } else {
                processCrawler(args[0], -1);
            }
        } else if (args.length == 2) {
            if ("-v".equals(args[0])) {
                Util.setVerbose(true);
                processCrawler(args[1], -1);
            } else if (args[1].matches("\\d+")){
                processCrawler(args[0], Integer.parseInt(args[1]));
            }else{
                usage();
            }
        } else if (args.length == 3) {
            if ("-v".equals(args[0]) && args[2].matches("\\d+")){
                Util.setVerbose(true);
                processCrawler(args[1], Integer.parseInt(args[2]));
            }else{
                usage();
            }
        } else {
            usage();
        }
    }

    private static void processCrawler(String url, int limit) {
        try {
            WebsiteCrawler crawler = WebsiteCrawler.getCrawler(url);
            Set<Movie> movies = crawler.getMovies(url, limit);

            WebsiteRater rater = WebsiteRater.getRater();

            System.out.println("<table>");
            movies.stream()
                    .peek(rater::updateMovieRating)
                    .sorted(Comparator.comparing(Movie::getRating).reversed())
                    .forEach(m -> System.out.println
                            ("<tr><td>" + m.getName() + "</td><td>" + m.getLink() + "</td><td>"
                                    + m.getRating()+"</td></tr>"));
            System.out.println("</table>");
        } catch (InvalidCrawlerException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void processShowHosts() {
        System.out.println("Following all available movie websites: ");
        WebsiteCrawler.getAllSupportedDomains().forEach(System.out::println);
    }

    private static void usage() {
        String out = "usage:";
        out += "\n\tmoviecrawler --hosts";
        out += "\n\tmoviecrawler [-v] <url> [limit]";
        System.out.println(out);
        System.exit(-1);
    }
}
