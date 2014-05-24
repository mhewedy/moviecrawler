package mhewedy;

import mhewedy.crawler.WebsiteCrawler;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        if (args.length == 1) {
            if ("--hosts".equals(args[0])) {
                processShowHosts();
            } else {
                processCrawler(args[0]);
            }
        } else if (args.length == 2) {
            if ("-v".equals(args[0])) {
                Util.setVerbose(true);
                processCrawler(args[1]);
            } else {
                usage();
            }
        } else {
            usage();
        }
    }

    private static void processCrawler(String url) {
    }

    private static void processShowHosts() {
        System.out.println("Following all available movie websites ");
        WebsiteCrawler.getAllSupportedDomains().forEach(System.out::println);
    }

    private static void usage() {
        System.out.println("usage:\n\tapp <url>\n\tapp -v <url>\n\tapp --hosts");
        System.exit(-1);
    }
}
