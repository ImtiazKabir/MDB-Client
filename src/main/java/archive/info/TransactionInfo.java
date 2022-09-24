package archive.info;

import archive.entity.Movie;

import java.io.Serializable;

public class TransactionInfo implements Serializable {
    private final Movie movie;
    private final String previousOwner;
    private final String newOwner;

    public TransactionInfo(Movie movie, String previousOwner, String newOwner) {
        this.movie = movie;
        this.previousOwner = previousOwner;
        this.newOwner = newOwner;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getPreviousOwner() {
        return previousOwner;
    }

    public String getNewOwner() {
        return newOwner;
    }
}
