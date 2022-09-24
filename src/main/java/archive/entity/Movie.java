package archive.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

public final class Movie implements Serializable {
        private final String title;
        private final int yearOfRelease;
        private final String[] genre;
        private final int runningTime;
        private String productionCompany;
        private final long budget;
        private final long revenue;
        private final long profit;




    enum ParseIndex {TITLE, YEAR_OF_RELEASE, GENRE1, GENRE2, GENRE3, RUNNING_TIME, PRODUCTION_COMPANY, BUDGET, REVENUE}
    public Movie(
            final String title, final int yearOfRelease, final String[] genre,
            final int runningTime, final String productionCompany, final long budget,
            final long revenue
    ) {
        this.title = title;
        this.yearOfRelease = yearOfRelease;
        this.genre = genre;
        this.runningTime = runningTime;
        this.productionCompany = productionCompany;
        this.budget = budget;
        this.revenue = revenue;
        this.profit = revenue - budget;
    }

    public Movie(final String[] attributes) {
        this(
                attributes[ParseIndex.TITLE.ordinal()],    // title
                Integer.parseInt(attributes[ParseIndex.YEAR_OF_RELEASE.ordinal()]),    // yearOfRelease
                Arrays.copyOfRange(attributes, ParseIndex.GENRE1.ordinal(), ParseIndex.GENRE3.ordinal() + 1),   // genre
                Integer.parseInt(attributes[ParseIndex.RUNNING_TIME.ordinal()]),  // runningTime
                attributes[ParseIndex.PRODUCTION_COMPANY.ordinal()],   // productionCompany
                Long.parseLong(attributes[ParseIndex.BUDGET.ordinal()]), // budget
                Long.parseLong(attributes[ParseIndex.REVENUE.ordinal()]) // revenue
        );
    }

    @Override
    public String toString() {
        return String.format("Title: %s, Year Of Release: %d, Genres: %s, Running Time (in minutes): %d, Production Company: %s, Budget: %d, Revenue: %d",
                title, yearOfRelease, Arrays.toString(genre), runningTime, productionCompany, budget, revenue);
    }

    public String getTitle() {
        return title;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public String[] getGenre() {
        return genre;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public String getProductionCompany() {
        return productionCompany;
    }

    public long getBudget() {
        return budget;
    }

    public long getRevenue() {
        return revenue;
    }

    public long getProfit() {
        return profit;
    }

    public void setProductionCompany(String productionCompany) {
        this.productionCompany = productionCompany;
    }

    public void display() {
        System.out.println("###########################################");
        System.out.printf("%s (%d) %s%d min | %s", title, yearOfRelease, System.lineSeparator(), runningTime, genre[0]);
        for (int i = 1; i < 3; ++i) {
            if (genre[i].isEmpty()) {
                break;
            }
            System.out.printf(",%s", genre[i]);
        }
        String pattern = "#,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        decimalFormat.setGroupingSize(3);
        System.out.printf("%sFrom %s%sBudget: $%s%sRevenue: $%s%s",
                System.lineSeparator(), productionCompany, System.lineSeparator(),
                decimalFormat.format(budget), System.lineSeparator(), decimalFormat.format(revenue),
                System.lineSeparator()
        );
        System.out.println("###########################################");
    }

    String toCombinedString() {
        // Toy Story,1995,Animation,Comedy,Family,81,Pixar Animation Studios,30000000,373554033
        return String.format("%s,%d,%s,%s,%s,%d,%s,%d,%d",
                title, yearOfRelease, genre[0], genre[1], genre[2], runningTime, productionCompany, budget, revenue
        );
    }

    boolean matchWithQuery(String field, String value) {
        if (field.equals("title")) {
            return title.equalsIgnoreCase(value);
        }
        if (field.equals("yearOfRelease")) {
            return yearOfRelease == Integer.parseInt(value);
        }
        if (field.equals("genre")) {
            return Arrays.stream(genre).anyMatch(value::equalsIgnoreCase);
        }
        if (field.equals("productionCompany")) {
            return productionCompany.equalsIgnoreCase(value);
        }
        if (field.equals("runningTime")) {
            return runningTime == Integer.parseInt(value);
        }
        return false;
    }


    public boolean matchWithContainsQuery(String field, String value) {
        if (field.equals("title")) {
            return title.toLowerCase().contains(value.toLowerCase());
        }
        if (field.equals("yearOfRelease")) {
            return String.valueOf(yearOfRelease).contains(value);
        }
        if (field.equals("genre")) {
            return Arrays.stream(genre).anyMatch(g -> g.toLowerCase().contains(value.toLowerCase()));
        }
        if (field.equals("productionCompany")) {
            return productionCompany.toLowerCase().contains(value.toLowerCase());
        }
        if (field.equals("runningTime")) {
            return String.valueOf(runningTime).contains(value);
        }
        if (field.equals("revenue")) {
            return String.valueOf(revenue).contains(value);
        }
        if (field.equals("budget")) {
            return String.valueOf(budget).contains(value);
        }

        return false;
    }

    public static String readString(Scanner scanner, String prompt) {
        System.out.printf("%s: ", prompt);
        return scanner.nextLine();
    }

    public static int readInt(Scanner scanner, String prompt) {
        return Integer.parseInt(readString(scanner, prompt));
    }

    public static long readLong(Scanner scanner, String prompt) {
        return Long.parseLong(readString(scanner, prompt));
    }

    private static String[] readGenre(Scanner scanner) {
        String[] genre = new String[3];
        System.out.println("Input at least one genre.");
        genre[0] = readString(scanner, "Genre 1");
        genre[1] = readString(scanner, "Genre 2 (Press Enter to skip)");
        if (!genre[1].isEmpty()) {
            genre[2] = readString(scanner, "Genre 3 (Press Enter to skip)");
        } else genre[2] = "";
        return genre;
    }

    public static Movie fromUserInput(Scanner scanner) {
        System.out.println("Enter movie details");

        String title = readString(scanner, "Title");
        int yearOfRelease = readInt(scanner, "Year of Release");
        String[] genre = readGenre(scanner);
        int runningTime = readInt(scanner, "Running Time (in minute)");
        String productionCompany = readString(scanner, "Production Company");
        long budget = readLong(scanner, "Budget");
        long revenue = readLong(scanner, "Revenue");
        return new Movie(title, yearOfRelease, genre, runningTime, productionCompany, budget, revenue);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Movie)) return false;
        Movie other = (Movie) obj;
        if (other.title.equals(this.title)) return true;
        return false;
    }
}

