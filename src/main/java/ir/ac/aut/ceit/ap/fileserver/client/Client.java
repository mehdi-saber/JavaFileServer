package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.client.view.MainJFrameController;
import ir.ac.aut.ceit.ap.fileserver.network.ExchangeData;
import ir.ac.aut.ceit.ap.fileserver.network.ExchangeTitle;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

public class Client {
//    private Map<FilePartitionInfo, File> partitionsMap;
    private MainJFrameController MainJFrameController;
    private ConnectionManager connectionManager;

    public Client()  {
        MainJFrameController = new MainJFrameController(this);
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
}
