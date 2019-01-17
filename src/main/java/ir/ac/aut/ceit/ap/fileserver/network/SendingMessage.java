package ir.ac.aut.ceit.ap.fileserver.network;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SendingMessage extends Message {
    Map<String, InputStream> streams;

    public SendingMessage(Subject title) {
        super(title);
        streams = new HashMap<>();
    }

    InputStream getStream(String key) {
        return streams.get(key);
    }

    public void addStream(String key,InputStream inputStream) {
         streams.put(key,inputStream);
    }
}
