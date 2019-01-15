package ir.ac.aut.ceit.ap.fileserver.network;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Exchange {
    private Gson gson;
    private Socket socket;

    public Exchange(Socket socket) {
        this.gson = new Gson();
        this.socket = socket;
    }

   public void send(ExchangeData exchangeData) throws IOException {
        String json = gson.toJson(exchangeData);
        socket.getOutputStream().write(json.getBytes());
    }

    public  ExchangeData receive() throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(socket.getInputStream()));
        return gson.fromJson(reader, ExchangeData.class);
    }
}
