package ir.ac.aut.ceit.ap.fileserver.network.progress;

import ir.ac.aut.ceit.ap.fileserver.network.protocol.ProgressSubject;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProgressWriter implements ProgressCallback {
    private final List<Integer> doneList;
    private final PipedInputStream pipedInputStream;
    private boolean close = false;

    public ProgressWriter() {
        pipedInputStream = new PipedInputStream();

        this.doneList = new ArrayList<>();
        new Thread(() -> {
            PrintWriter out = null;
            try {
                out = new PrintWriter(new PipedOutputStream(pipedInputStream));
                while (!close) {
                    synchronized (doneList) {
                        if (doneList.size() > 0) {
                            Iterator<Integer> iterator = doneList.iterator();
                            while (iterator.hasNext()) {
                                int doneDelta = iterator.next();
                                iterator.remove();
                                out.println(ProgressSubject.CALL_BACK.name());
                                out.println(doneDelta);
                                out.flush();
                            }
                        } else {
                            out.println(ProgressSubject.WAIT.name());
                            out.flush();
                        }
                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            out.println(ProgressSubject.END.name());
            out.close();
        }).start();
    }

    @Override
    public void call(int doneDelta) {
        synchronized (doneList) {
            doneList.add(doneDelta);
        }
    }

    public void close() {
        close = true;
    }

    public PipedInputStream getPipedInputStream() {
        return pipedInputStream;
    }
}
