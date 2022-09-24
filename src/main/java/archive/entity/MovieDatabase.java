package archive.entity;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class MovieDatabase implements Serializable{
    private final String fileName;
    private final List<Movie> movieList;

    public MovieDatabase(final String fileName) throws IOException {
        this.fileName = fileName;
        movieList = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            Movie movie = new Movie(line.split(","));
            movieList.add(movie);
        }
        br.close();
    }

    public MovieDatabase(final List<Movie> list) {
        fileName = null;
        movieList = list;
    }



    public MovieDatabase databaseFromQuery(String field, String value) {
        return new MovieDatabase(
                movieList.stream()
                .filter(m -> m.matchWithQuery(field, value))
                .collect(Collectors.toList())
        );
    }

    public MovieDatabase databaseFromContainsQuery(String field, String value) {
        return new MovieDatabase(
                movieList.stream()
                        .filter(m -> m.matchWithContainsQuery(field, value))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Movie m: movieList) {
            str.append(m);
            str.append(System.lineSeparator());
        }
        return str.toString();
    }

    private void showAppendConfirmation() {
        System.out.println("Movie successfully added to database. Effects will be visible after exiting the program from main menu");
    }

    private void showDuplicateError() {
        System.out.println("A movie with the same title already exists. Append failed.");
    }

    public final void addMovie(Movie movie) {
        for (Movie m : movieList) {
            if (m.matchWithQuery("title", movie.getTitle())) {
                showDuplicateError();
                return;
            }
        }
        movieList.add(movie);
        showAppendConfirmation();
    }

    public final void dumpToFile() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        for (Movie movie: movieList) {
            bw.write(movie.toCombinedString());
            bw.write(System.lineSeparator());
        }
        bw.close();
    }

    private void showFilteredMovies(MovieDatabase db, String filterLabel) {
        if (db.movieList.isEmpty()) {
            System.out.println("No such movie with this " + filterLabel);
            return;
        }

        int index = 0;
        for (Movie movie: db.movieList) {
            System.out.println((++index) + ") ");
            movie.display();
        }
    }

    public void showQueryResult(String field, String value) {
        MovieDatabase db = databaseFromQuery(field, value);
        showFilteredMovies(db, field);
    }


    public final void showTopTen() {
        MovieDatabase db = new MovieDatabase(
                movieList.stream()
                .sorted(Comparator.comparingLong(Movie::getProfit).reversed())
                .collect(Collectors.toList())
        );
        showFilteredMovies(db, null);
    }

    public final void showMostRecentMovies(final String productionCompany) {
        MovieDatabase db = new MovieDatabase(
                movieList.stream()
                .filter(m -> m.matchWithQuery("productionCompany", productionCompany))
                .sorted(Comparator.comparingInt(Movie::getYearOfRelease).reversed())
                .collect(Collectors.toList())
        );
        showFilteredMovies(db, "production company");
    }

    public final void showMaximumRevenueMovies(final String productionCompany) {
        MovieDatabase db = new MovieDatabase(
                movieList.stream()
                .filter(m -> m.matchWithQuery("productionCompany", productionCompany))
                .sorted(Comparator.comparingLong(Movie::getRevenue).reversed())
                .collect(Collectors.toList())
        );
        showFilteredMovies(db, "production company");
    }

    public final void showTotalProfit(final String productionCompany) {
        long totalProfit = movieList.stream()
                .filter(m -> m.matchWithQuery("productionCompany", productionCompany))
                .mapToLong(Movie::getProfit)
                .sum();
        System.out.printf("%s has a total profit of %d%s", productionCompany.toUpperCase(), totalProfit, System.lineSeparator());
    }


    public final void showProductionCompanyWithMovieCount() {
        Map<String, Long> movieCount = movieList.stream()
                .collect(Collectors.groupingBy(Movie::getProductionCompany, Collectors.counting()));
        for (String productionCompany : movieCount.keySet()) {
            if (productionCompany.isEmpty()) continue;
            System.out.printf("%s has produced %d movie(s) %s", productionCompany, movieCount.get(productionCompany), System.lineSeparator());
        }
    }

    public final List<String> getProductionCompanies() {
        List<String> companies = new LinkedList<>();
        for (Movie m: movieList) {
            if (!companies.contains(m.getProductionCompany())) {
                companies.add(m.getProductionCompany());
            }
        }
        return companies;
    }

    public final List<Movie> getMovieList() {
        return movieList;
    }

    public final Movie removeMovieWithTitle(final String title) {
        Movie movie = null;
        for (int i = 0; i < movieList.size(); ++i) {
            if (movieList.get(i).getTitle().equals(title)) {
                movie = movieList.get(i);
                movieList.remove(i);
                break;
            }
        }
        return movie;
    }

    public MovieDatabase databaseFromProductionCompany(String company) {
        return new MovieDatabase(
                movieList.stream()
                        .filter(m -> m.getProductionCompany().equals(company))
                        .collect(Collectors.toList())
        );
    }
}
