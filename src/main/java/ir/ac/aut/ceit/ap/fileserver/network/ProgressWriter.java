package ir.ac.aut.ceit.ap.fileserver.network;

import java.io.OutputStream;
import java.io.PrintWriter;

public class ProgressWriter implements ProgressCallback {
    private PrintWriter out;

    public ProgressWriter(OutputStream outputStream) {
        this.out = new PrintWriter(outputStream);
    }

    @Override
    public void call(int doneDelta) {
        out.println(StreamsCommand.PROGRESS_PASSED.name());
        out.println(doneDelta);
        out.flush();
    }

    public void close() {
        out.println(StreamsCommand.PROGRESS_END.name());
        out.close();
    }
}
