package mhewedy;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import mhewedy.crawler.El7lCrawler;
import mhewedy.crawler.WebsiteCrawler;

import java.io.IOException;

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
        assertEquals(crawler.getClass(), El7lCrawler.class);
    }

    public void testNonExistCrawler(){
        WebsiteCrawler crawler = WebsiteCrawler.getCrawler("http://newWebsite.com");
        assertEquals(crawler.getDomain(), "invalid url");
    }

    public void testNonExistCrawler2(){
        WebsiteCrawler crawler = WebsiteCrawler.getCrawler("http://newWebsite.com");
        try {
            assertEquals(crawler.getMovieNames("http://newWebsite.com", -1).size(), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testEl7lCrawler(){
        WebsiteCrawler crawler = WebsiteCrawler.getCrawler("http://el7l.co");
        String url = "http://el7l.co/online2/415/%D8%A7%D9%81%D9%84%D8%A7%D9%85_%D8%A7%D8%AC%D9%86%D8%A8%D9%8A%D8%A9/1.html";
        try {
            assertEquals(crawler.getMovieNames(url, 40).size(), 40);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testEl7lCrawler2(){
        WebsiteCrawler crawler = WebsiteCrawler.getCrawler("http://el7l.co");
        String url = "http://el7l.co/tag/%D8%A7%D9%81%D9%84%D8%A7%D9%85+%D8%B1%D8%B9%D8%A8";
        try {
            assertTrue(crawler.getMovieNames(url, 100).size() == 120);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
