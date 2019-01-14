package ir.ac.aut.ceit.ap.fileserver.network;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private Map<String, Object> parameters;
    private static final String COMMAND_KEY = "command";

    public Request(Command command) {
        this.parameters = new HashMap<>();
        parameters.put(COMMAND_KEY, command);

    }

    public Request(InputStream is) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        Gson gson = new Gson();
        parameters = gson.fromJson(reader, Map.class);
        is.close();
    }

    public void send(OutputStream os) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(parameters);
        os.write(json.getBytes());
        os.close();
    }

    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public Object getObject(String key) {
        return parameters.get(key);
    }

    public Command getCommand() {
        return (Command) getObject(COMMAND_KEY);
    }
}
