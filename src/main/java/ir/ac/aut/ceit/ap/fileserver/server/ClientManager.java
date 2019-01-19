package ir.ac.aut.ceit.ap.fileserver.server;

import java.util.*;

class ClientManager {
    private List<ClientInfo> clientList;

    ClientManager() {
        this.clientList = new ArrayList<>();
    }

    void addClient(ClientInfo clientInfo) {
        clientList.add(clientInfo);
    }


    Map<ClientInfo, Set<Long>> getDestinations(List<Long> parts, int repeat) {
        Map<ClientInfo, Set<Long>> receivingClients = new HashMap<>();
        int cIndex = 0;
        for (Long partId : parts)
            for (int i = 0; i < repeat; i++) {
                cIndex = clientList.size() <= cIndex + 1 ? 0 : cIndex + 1;
                ClientInfo clientInfo = clientList.get(cIndex);
                receivingClients.computeIfAbsent(clientInfo, k -> new HashSet<>());
                receivingClients.get(clientInfo).add(partId);
            }
        return receivingClients;
    }

    void updateParts(Map<ClientInfo, Set<Long>> sentParts) {
        for (ClientInfo client : sentParts.keySet())
            client.getParts().addAll(sentParts.get(client));
    }

    public void setClientList(List<ClientInfo> clientList) {
        this.clientList = clientList;
    }

    List<ClientInfo> getClientList() {
        return clientList;
    }
}
