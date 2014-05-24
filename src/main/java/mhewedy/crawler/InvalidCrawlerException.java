package mhewedy.crawler;

/**
 * Created by mhewedy on 5/24/14.
 */
public class InvalidCrawlerException extends Exception {
    public InvalidCrawlerException() {
    }

    public InvalidCrawlerException(String message) {
        super(message);
    }

    public InvalidCrawlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCrawlerException(Throwable cause) {
        super(cause);
    }

    public InvalidCrawlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
