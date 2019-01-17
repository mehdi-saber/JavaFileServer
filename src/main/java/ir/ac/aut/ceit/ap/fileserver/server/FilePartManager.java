package ir.ac.aut.ceit.ap.fileserver.server;

import java.util.ArrayList;
import java.util.List;

class FilePartManager {
    private List<ClientInfo> clientList;
     int splitSize;

    FilePartManager(int splitSize, List<ClientInfo> clientList) {
        this.splitSize = splitSize;
        this.clientList = clientList;

    }

    List<ClientInfo> partsDestinations(int fileSize) {
        List<ClientInfo> receivingClients = new ArrayList<>();
        int cIndex = 0;
        for (int i = 0; i < (fileSize / splitSize) + 1; i++) {
            cIndex = clientList.size() < cIndex + 1 ? 0 : cIndex + 1;
            receivingClients.add(clientList.get(cIndex));
        }
        return receivingClients;
    }
}
