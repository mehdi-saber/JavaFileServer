package ir.ac.aut.ceit.ap.fileserver.network.request;

import ir.ac.aut.ceit.ap.fileserver.network.Message;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.Subject;
import ir.ac.aut.ceit.ap.fileserver.network.progress.ProgressCallback;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ResponseCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SendingMessage extends Message implements Serializable {
    private transient Map<String, InputStream> streams;
    protected transient ResponseCallback responseCallback;
    private transient Map<String, ProgressCallback> progressCallbacks;

    public SendingMessage(Subject title) {
        super(title);
        streams = new HashMap<>();
        streamSize = new HashMap<>();
        progressCallbacks = new HashMap<>();
    }

    public InputStream getStream(String key) {
        return streams.get(key);
    }

    public void addInputStream(String key, InputStream inputStream, Long size) {
        streamSize.put(key, size);
        streams.put(key, inputStream);
    }

    public void closeInputStreams() throws IOException {
        for (InputStream inputStream : streams.values())
            inputStream.close();
    }

    public void addProgressCallback(String key, ProgressCallback progressCallback) {
        progressCallbacks.put(key, progressCallback);
    }

    public ProgressCallback getProgressCallback(String key) {
        return progressCallbacks.get(key);
    }

    public void setResponseCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }


}
