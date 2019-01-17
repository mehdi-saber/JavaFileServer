package ir.ac.aut.ceit.ap.fileserver.network;


import java.io.Serializable;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {
    Map<String, Object> parameters;
    private static final String TITLE_MAP_KEY = "TITLE_MAP_KEY";

    public Message(Subject title) {
        this.parameters = new HashMap<>();
        addParameter(TITLE_MAP_KEY, title);
    }

    public Subject getTitle() {
        return (Subject) getObject(TITLE_MAP_KEY);
    }

    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public void addBytes(String key, byte[] value) {
        byte[] encoded = Base64.getEncoder().encode(value);
        parameters.put(key, new String(encoded));
    }

    public byte[] getBytes(String key) {
        return Base64.getDecoder().decode((String) parameters.get(key));
    }

    public Object getObject(String key) {
        return parameters.get(key);
    }

}
