package ir.ac.aut.ceit.ap.fileserver.network;

import java.io.IOException;
import java.io.InputStream;

public class LimitedInputStream extends InputStream {
    private InputStream inputStream;
    private long limit;

    public LimitedInputStream(InputStream inputStream, long limit) {
        this.inputStream = inputStream;
        this.limit = limit;
    }

    @Override
    public int read() throws IOException {
        if (limit-- > 0)
            return inputStream.read();
        else
            return -1;
    }
}
