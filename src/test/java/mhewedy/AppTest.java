package mhewedy;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import mhewedy.crawler.El7lCrawler;
import mhewedy.crawler.WebsiteCrawler;

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
        assertEquals(crawler.getMovieNames("http://newWebsite.com", -1).size(), 0);
    }

}
