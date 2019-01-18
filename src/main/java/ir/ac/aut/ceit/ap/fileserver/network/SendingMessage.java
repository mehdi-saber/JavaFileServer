package ir.ac.aut.ceit.ap.fileserver.network;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendingMessage extends Message implements Serializable {
    private transient Map<String, InputStream> streams;
    List<String> streamKey;
    private transient ResponseCallback responseCallback;

    public SendingMessage(Subject title) {
        super(title);
        streams = new HashMap<>();
        streamKey = new ArrayList<>();
    }

    InputStream getStream(String key) {
        return streams.get(key);
    }

    public void addStream(String key,InputStream inputStream) {
        streamKey.add(key);
        streams.put(key, inputStream);
    }

    public void setResponseCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    public ResponseCallback getResponseCallback() {
        return responseCallback;
    }
}
