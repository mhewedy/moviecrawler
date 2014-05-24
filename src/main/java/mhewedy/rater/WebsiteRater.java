package mhewedy.rater;

import mhewedy.beans.Movie;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by mhewedy on 5/23/14.
 */
public interface WebsiteRater {

    void updateMovieRating(Movie movie) throws IOException;

}
