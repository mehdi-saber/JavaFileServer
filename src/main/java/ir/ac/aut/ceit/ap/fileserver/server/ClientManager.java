package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.file.SaveAble;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ClientManager implements SaveAble {

    private List<ClientInfo> clientList;
    private int repeat;

    ClientManager(int repeat) {
        List<ClientInfo> clientList = (List<ClientInfo>) load();
        if (clientList != null)
            this.clientList = clientList;
        else
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

    @Override
    public Object getSaveObject() {
        return clientList;
    }

    @Override
    public String getSaveFileName() {
        return "clientManager";
    }

    Map<ClientInfo, Set<Long>> getDeletingParts(Set<FSFile> files, Set<FSPath> remainPath) {
        Map<ClientInfo, Set<Long>> partsMap = new HashMap<>();

        Set<Long> filesParts = files.stream()
                .flatMap(file -> file.getParts().stream())
                .collect(Collectors.toSet());

        Set<Long> remainFilesParts = remainPath.stream()
                .flatMap(file -> file instanceof FSFile ? ((FSFile) file).getParts().stream() : Stream.of())
                .collect(Collectors.toSet());

        filesParts.removeIf(remainFilesParts::contains);//may copied other place

        for (ClientInfo client : clientList) {
            Set<Long> intersection = new HashSet<>(filesParts);
            intersection.retainAll(client.getParts());
            if (intersection.size() > 0)
                partsMap.put(client, intersection);
        }

        return partsMap;
    }
}
