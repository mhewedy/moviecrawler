package mhewedy;

import mhewedy.beans.Movie;
import mhewedy.crawler.InvalidCrawlerException;
import mhewedy.crawler.WebsiteCrawler;
import mhewedy.rater.WebsiteRater;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CLI {

    public static void start(String[] args) {

        try{
            if (args.length == 1) {
                if ("--hosts".equals(args[0])) {
                    processShowHosts();
                } else {
                    App.usage();
                }
            } else if (args.length == 2) {
                App.processCrawler(args[0], -1, new PrintStream(new FileOutputStream(args[1])));
            }else if (args.length == 3) {
                if ("-v".equals(args[0])) {
                    Util.setVerbose(true);
                    App.processCrawler(args[1], -1, new PrintStream(new FileOutputStream(args[2])));
                } else if (args[1].matches("\\d+")){
                    App.processCrawler(args[0], Integer.parseInt(args[1]), new PrintStream(new FileOutputStream(args[2])));
                }else{
                    App.usage();
                }
            } else if (args.length == 4) {
                if ("-v".equals(args[0]) && args[2].matches("\\d+")){
                    Util.setVerbose(true);
                    App.processCrawler(args[1], Integer.parseInt(args[2]), new PrintStream(new FileOutputStream(args[3])));
                }else{
                    App.usage();
                }
            } else {
                App.usage();
            }
        }catch (FileNotFoundException ex){
            System.err.println(ex.getMessage());
        }
    }

    private static void processShowHosts() {
        System.out.println("Following all available movie websites: ");
        WebsiteCrawler.getAllSupportedDomains().forEach(System.out::println);
    }
}
