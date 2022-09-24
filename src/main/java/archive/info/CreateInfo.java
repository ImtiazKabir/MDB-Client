package archive.info;

import archive.entity.Movie;

import java.io.Serializable;

public class CreateInfo implements Serializable  {
    private final Movie movie;
    public CreateInfo(final Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }
}
