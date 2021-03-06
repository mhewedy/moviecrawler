package mhewedy.beans;

/**
 * Created by mhewedy on 5/23/14.
 */
public class Movie {
    private String name;
    private String link;
    private double rating;
    private String poster;
    private String imdbUrl;

    public Movie(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setImdbUrl(String imdbUrl) {
        this.imdbUrl = imdbUrl;
    }

    public String getImdbUrl() {
        return imdbUrl;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", rating=" + rating +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (link != null ? !link.equals(movie.link) : movie.link != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return link != null ? link.hashCode() : 0;
    }
}
