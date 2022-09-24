package archive.info;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Serializable;

public class InfoWrapper implements Serializable {
    private final String command;
    private final Object content;

    public InfoWrapper(String command, Object content) {
        this.command = command;
        this.content = content;
    }

    public InfoWrapper(String json) {
        JsonObject jsonObj = JsonParser.parseString(json).getAsJsonObject();
        command = jsonObj.get("command").getAsString();
        switch (command) {
            case "login":
                content = new Gson().fromJson(jsonObj.getAsJsonObject("content").toString(), LoginInfo.class);
                break;
            case "session":
                content = new Gson().fromJson(jsonObj.getAsJsonObject("content").toString(), SessionInfo.class);
                ((SessionInfo) content).createDatabase(json);
                break;
            case "create":
                content = new Gson().fromJson(jsonObj.getAsJsonObject("content").toString(), CreateInfo.class);
                break;
            case "transaction":
                content = new Gson().fromJson(jsonObj.getAsJsonObject("content").toString(), TransactionInfo.class);
                break;
            default:
                content = null;
                break;
        }
    }

    public String getCommand() {
        return command;
    }

    public <T> T getContent() {
        return (T) content;
    }
}
