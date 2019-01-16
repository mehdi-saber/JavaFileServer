package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.client.view.MainJFrameController;
import ir.ac.aut.ceit.ap.fileserver.filesys.DirectoryInfo;
import ir.ac.aut.ceit.ap.fileserver.filesys.FileInfo;
import ir.ac.aut.ceit.ap.fileserver.filesys.PathInfo;
import ir.ac.aut.ceit.ap.fileserver.network.ExchangeData;
import ir.ac.aut.ceit.ap.fileserver.network.ExchangeTitle;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

public class Client {
    private ConnectionManager connectionManager;

    public Client()  {
        MainJFrameController mainJFrameController = new MainJFrameController(this);
        try {
            connectionManager = new ConnectionManager("localhost", 5000);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void login(String username, String password) throws IOException {
        ExchangeData requestData = new ExchangeData(ExchangeTitle.LOGIN_USER);
        requestData.addParameter("username", username);
        requestData.addParameter("password", password);
        ExchangeData response = connectionManager.request(requestData);
    }

    public void register(String username, String password) throws IOException {
        ExchangeData requestData = new ExchangeData(ExchangeTitle.REGISTER_USER);
        requestData.addParameter("username", username);
        requestData.addParameter("password", password);
        ExchangeData response = connectionManager.request(requestData);
        System.out.println(response.getObject("token"));
    }

    public void download(FileInfo fileInfo) {
//        todo:implement
    }

    public void copy(PathInfo pathInfo) {
//        todo:implement
    }

    public void cut(PathInfo pathInfo) {
        //        todo:implement
    }

    public void rename(PathInfo pathInfo, String newName) {
        //        todo:implement
    }

    public void delete(PathInfo pathInfo) {
        //        todo:implement
    }

    public void upload(File file) {
        //        todo:implement
    }

    public void paste(DirectoryInfo directoryInfo) {
        //        todo:implement
    }

    public void search(DirectoryInfo directoryInfo) {
        //        todo:implement
    }
}
