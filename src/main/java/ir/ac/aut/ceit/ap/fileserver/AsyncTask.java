package ir.ac.aut.ceit.ap.fileserver;

abstract public class AsyncTask {
    private Thread thread;

    public AsyncTask() {
        thread = new Thread(this::doTask);
    }

    protected abstract void doTask();

    public void startTask() {
        thread.start();
    }
}
