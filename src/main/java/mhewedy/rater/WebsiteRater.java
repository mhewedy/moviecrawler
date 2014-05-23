package mhewedy.rater;

import java.util.List;
import java.util.Map;

/**
 * Created by mhewedy on 5/23/14.
 */
public interface WebsiteRater {

    Map<String, Integer> getMovieRatings(List<String> movieNameList);

}
