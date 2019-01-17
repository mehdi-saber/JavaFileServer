package ir.ac.aut.ceit.ap.fileserver.network;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Message implements Serializable {
    protected Map<String, Object> parameters;
    private static final String TITLE_MAP_KEY = "TITLE_MAP_KEY";

    public Message(Subject title) {
        this.parameters = new HashMap<>();
        addParameter(TITLE_MAP_KEY, title);
    }

    Message(Message message) {
        this.parameters = message.parameters;
    }

    public Subject getTitle() {
        return (Subject) getObject(TITLE_MAP_KEY);
    }

    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public Object getObject(String key) {
        return parameters.get(key);
    }

}
