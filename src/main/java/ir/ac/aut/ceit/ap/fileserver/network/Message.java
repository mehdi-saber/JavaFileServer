package ir.ac.aut.ceit.ap.fileserver.network;


import ir.ac.aut.ceit.ap.fileserver.network.protocol.Subject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * a transportable message
 */
public abstract class Message implements Serializable {
    private static final String TITLE_MAP_KEY = "TITLE_MAP_KEY";
    protected Map<String, Long> streamSize;
    private Map<String, Object> parameters;

    /**
     * creates new message
     *
     * @param title the message title
     */
    public Message(Subject title) {
        this.parameters = new HashMap<>();
        addParameter(TITLE_MAP_KEY, title);
    }

    /**
     * create message from others data
     *
     * @param message the other message
     */
    public Message(Message message) {
        this.parameters = message.parameters;
        this.streamSize = message.streamSize;
    }

    /**
     * @return the title
     */
    public Subject getTitle() {
        return (Subject) getParameter(TITLE_MAP_KEY);
    }

    /**
     * adds a parameter
     *
     * @param key   key
     * @param value value
     */
    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    /**
     * get a parameter by key
     *
     * @param key key
     * @return parameter
     */
    public Object getParameter(String key) {
        return parameters.get(key);
    }

    /**
     * gets streams size
     *
     * @param key key
     * @return size
     */
    public Long getStreamSize(String key) {
        return streamSize.get(key);
    }

    /**
     * get stream size list
     *
     * @return list
     */
    public Map<String, Long> getStreamSize() {
        return streamSize;
    }
}
