package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSFile;

import java.util.*;
import java.util.stream.Collectors;

class ClientManager {
    private List<ClientInfo> clientList;
    private int repeat;

    ClientManager(int repeat) {
        this.clientList = new ArrayList<>();
        this.repeat = repeat;
    }

    void addClient(ClientInfo clientInfo) {
        clientList.add(clientInfo);
    }

    Map<ClientInfo, Set<Long>> getDestinations(List<Long> parts) {
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

    Map<Long, ClientInfo> getFileClientList(FSFile file) {
        Map<Long, ClientInfo> partMap = new LinkedHashMap<>();

        List<Long> parts = new ArrayList<>(file.getParts());
        parts.sort(Long::compareTo);

        for (Long partId : parts) {
            Set<ClientInfo> containingClients = clientList.stream()
                    .filter(client -> client.getParts().contains(partId))
                    .collect(Collectors.toSet());
            ClientInfo theClient = containingClients.stream().findAny().get();
            partMap.put(partId, theClient);
        }
        return partMap;
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
