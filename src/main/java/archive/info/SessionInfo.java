package archive.info;

import archive.entity.Movie;
import archive.entity.MovieDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class SessionInfo implements Serializable {
    private final String clientName;

    private MovieDatabase database;
    private final List<String> companyList;

    public SessionInfo(String clientName, MovieDatabase database, List<String> companyList) {
//        List<Movie> list = new LinkedList<>();
//        Movie m1 = new Movie("Toy Story,1995,Animation,Comedy,Family,81,Pixar Animation Studios,30000000,373554033".split(","));
//        Movie m2 = new Movie("boy Story,2095,Animation,Comedy,Family,81,Pixar Animation Studios,30000000,373554033".split(","));
//        Movie m3 = new Movie("joy Story,2195,Animation,Comedy,Family,81,Pixar Animation Studios,30000000,373554033".split(","));
//        list.add(m1);
//        list.add(m2);
//        list.add(m3);
//        database = new MovieDatabase(list);
//
//        companyList = new LinkedList<>();
//        companyList.add("Universal Pictures");
//        companyList.add("Miramax Films");
//        companyList.add("Paramount Pictures");
        this.clientName = clientName;
        this.database = database;
        this.companyList = companyList;
    }

    public MovieDatabase getDatabase() {
        return database;
    }

    public List<String> getCompanyList() {
        return companyList;
    }

    public String getClientName() {
        return clientName;
    }

    public void createDatabase(String json) {
        Movie[] movies = new Gson().fromJson(JsonParser.parseString(json).getAsJsonObject().getAsJsonArray("movieList").toString(), Movie[].class);
        database = new MovieDatabase(Arrays.asList(movies));
    }
}
