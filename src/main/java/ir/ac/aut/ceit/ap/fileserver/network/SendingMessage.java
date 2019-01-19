package ir.ac.aut.ceit.ap.fileserver.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SendingMessage extends Message implements Serializable, Transformer {
    private transient Map<String, InputStream> streams;
    private transient ResponseCallback responseCallback;
    private transient Map<String, ProgressCallback> progressCallbacks;

    public SendingMessage(Subject title) {
        super(title);
        streams = new HashMap<>();
        streamSize = new HashMap<>();
        progressCallbacks = new HashMap<>();
    }

    InputStream getStream(String key) {
        return streams.get(key);
    }

    public void addInputStream(String key, InputStream inputStream, Long size) {
        streamSize.put(key, size);
        streams.put(key, inputStream);
    }

    void closeInputStreams() throws IOException {
        for (InputStream inputStream : streams.values())
            inputStream.close();
    }

    public void addProgressCallback(String key, ProgressCallback progressCallback) {
        progressCallbacks.put(key, progressCallback);
    }

    ProgressCallback getProgressCallback(String key) {
        return progressCallbacks.get(key);
    }

    public void setResponseCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    public Thread send( String address, int port) {
        Thread requestThread = new Thread(() -> {
            try {
                Socket socket = new Socket(address, port);
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();
                writeMessage(this, outputStream);
                waitForStreamRequest(this, outputStream, inputStream);
                this.closeInputStreams();
                ReceivingMessage receivingMessage = readMessage(socket);
                if (responseCallback != null)
                    responseCallback.call(receivingMessage);
                outputStream.write((StreamsCommand.END_READING_STREAMS + "\n").getBytes());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        requestThread.start();
        return requestThread;
    }
}
