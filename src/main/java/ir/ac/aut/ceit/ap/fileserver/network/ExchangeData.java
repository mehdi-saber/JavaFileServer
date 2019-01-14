package ir.ac.aut.ceit.ap.fileserver.network;

import java.util.HashMap;
import java.util.Map;

public class ExchangeData {
    private Map<String, Object> parameters;
    private static final String TITLE_MAP_KEY = "TITLE_MAP_KEY";

    public ExchangeData(ExchangeTitle title) {
        this.parameters = new HashMap<>();
        addParameter(TITLE_MAP_KEY, title);
    }

    public ExchangeTitle getTitle() {
        return  ExchangeTitle.valueOf((String) getObject(TITLE_MAP_KEY));
    }

    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public Object getObject(String key) {
        return parameters.get(key);
    }

}
