package mhewedy;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import mhewedy.crawler.El7lCrawler;
import mhewedy.crawler.WebsiteCrawler;

import java.io.IOException;
import java.util.Set;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }


    public void testCrawler(){
        WebsiteCrawler crawler = WebsiteCrawler.getCrawler("http://el7l.co");
        assertEquals(El7lCrawler.class, crawler.getClass());
    }

    public void testNonExistCrawler(){
        WebsiteCrawler crawler = WebsiteCrawler.getCrawler("http://newWebsite.com");
        assertEquals("invalid url", crawler.getDomain());
    }

    public void testNonExistCrawler2(){
        WebsiteCrawler crawler = WebsiteCrawler.getCrawler("http://newWebsite.com");
        try {
            assertEquals(0, crawler.getMovies("http://newWebsite.com", -1).size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testEl7lCrawler(){
        WebsiteCrawler crawler = WebsiteCrawler.getCrawler("http://el7l.co");
        String url = "http://el7l.co/online2/415/%D8%A7%D9%81%D9%84%D8%A7%D9%85_%D8%A7%D8%AC%D9%86%D8%A8%D9%8A%D8%A9/1.html";
        try {
            assertEquals(40, crawler.getMovies(url, 40).size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testEl7lCrawler2(){
        WebsiteCrawler crawler = WebsiteCrawler.getCrawler("http://el7l.co");
        String url = "http://el7l.co/tag/%D8%A7%D9%81%D9%84%D8%A7%D9%85+%D8%B1%D8%B9%D8%A8";
        try {
            assertTrue(crawler.getMovies(url, 100).size() == 120);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testEl7lCrawler3(){
        WebsiteCrawler crawler = WebsiteCrawler.getCrawler("http://el7l.co");
        String url = "http://el7l.co/tag/%D8%A7%D9%81%D9%84%D8%A7%D9%85+%D8%B1%D8%B9%D8%A8";
        try {
            Set<Movie> movies = crawler.getMovies(url, 40);

            long cnt = movies.stream()
                    .map(Movie::getName)
                    .filter(n -> n.contains("بجودة"))
                    .count();
            assertEquals("movies with name بجودة are ", 0, cnt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
