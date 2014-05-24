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

 app --hosts

 app <url> <outfile>

 app -v <url> <outfile>
 app <url> [limit] <outfile>

 app -v <url> [limit] <outfile>

 */
public class App {
    public static void main(String[] args) {

        try{
            if (args.length == 1) {
                if ("--hosts".equals(args[0])) {
                    processShowHosts();
                } else {
                    usage();
                }
            } else if (args.length == 2) {
                processCrawler(args[0], -1, new PrintStream(new FileOutputStream(args[1])));
            }else if (args.length == 3) {
                if ("-v".equals(args[0])) {
                    Util.setVerbose(true);
                    processCrawler(args[1], -1, new PrintStream(new FileOutputStream(args[2])));
                } else if (args[1].matches("\\d+")){
                    processCrawler(args[0], Integer.parseInt(args[1]), new PrintStream(new FileOutputStream(args[2])));
                }else{
                    usage();
                }
            } else if (args.length == 4) {
                if ("-v".equals(args[0]) && args[2].matches("\\d+")){
                    Util.setVerbose(true);
                    processCrawler(args[1], Integer.parseInt(args[2]), new PrintStream(new FileOutputStream(args[3])));
                }else{
                    usage();
                }
            } else {
                usage();
            }
        }catch (FileNotFoundException ex){
            System.err.println(ex.getMessage());
        }
    }

    private static void processCrawler(String url, int limit, PrintStream out) {
        try {
            WebsiteCrawler crawler = WebsiteCrawler.getCrawler(url);
            Set<Movie> movies = crawler.getMovies(url, limit);

            WebsiteRater rater = WebsiteRater.getRater();

            out.println("<h2>rating for : " + URLDecoder.decode(url, "utf8") +
                    "</h2> <br /> <table border=\"1\">");
            List<Movie> collect = movies.stream()
                    .parallel()
                    .peek(rater::updateMovieRating)
                    .sorted(Comparator.comparing(Movie::getRating).reversed())
                    .collect(Collectors.toList());

            collect.forEach(m -> out.println
                    ("<tr><td> <a href=\"" + m.getLink() + "\">" + m.getName() + "</a></td><td>"
                            + "<img src=\"" + m.getPoster() + "\" /></td><td>" +
                            + m.getRating() + "</td></tr>"));
            out.println("</table>");
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
