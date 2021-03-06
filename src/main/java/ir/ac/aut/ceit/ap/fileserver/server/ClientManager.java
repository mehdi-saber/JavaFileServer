package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.file.SaveAble;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handles server behaviors based on clients info
 */
class ClientManager implements SaveAble {
    private List<ClientInfo> clientList;
    private int redundancy;

    /**
     * Constructs a new client manager
     */
    ClientManager() {
        List<ClientInfo> clientList = (List<ClientInfo>) load();
        if (clientList != null)
            this.clientList = clientList;
        else
            this.clientList = new ArrayList<>();
    }

    /**
     * Adds a client to the List
     *
     * @param clientInfo The Client
     */
    void addClient(ClientInfo clientInfo) {
        clientList.add(clientInfo);
    }

    /**
     * Decide about distribution of parts
     *
     * @param partSet Parts
     * @return Distribution
     */
    Map<ClientInfo, Set<Long>> getDestinations(Set<Long> partSet) {
        Map<ClientInfo, Set<Long>> distribution = new HashMap<>();

        List<Long> parts = new ArrayList<>(partSet);
        parts.addAll(new ArrayList<>(parts));

        int sum = clientList.stream().mapToInt(ClientInfo::getSpace).sum();
        int chosenIndex = -1;
        int clientRemain = 0;
        ClientInfo clientInfo=null;
        for (Long partId : parts) {
            if (clientRemain == 0) {
                chosenIndex++;
                chosenIndex = clientList.size() == chosenIndex ? 0 : chosenIndex;
                clientInfo = clientList.get(chosenIndex);
                clientRemain = ((Double) Math.ceil((double) clientInfo.getSpace() / sum * partSet.size())).intValue();
            }

            //add a Hash set for client list if doesn't exists
            distribution.computeIfAbsent(clientInfo, key -> new HashSet<>());
            distribution.get(clientInfo).add(partId);
            clientRemain--;
        }
        return distribution;
    }

    /**
     * Decide download each part from which node
     *
     * @param file The File
     * @return The parts sender nodes
     */
    LinkedHashMap<Long, ClientInfo> getFileClientList(FSFile file) {
        LinkedHashMap<Long, ClientInfo> partMap = new LinkedHashMap<>();

        List<Long> parts = new ArrayList<>(file.getParts().keySet());
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

    /**
     * Adding new parts to clients info
     *
     * @param sentParts The parts
     */
    void updateParts(Map<ClientInfo, Set<Long>> sentParts) {
        for (ClientInfo client : sentParts.keySet())
            client.getParts().addAll(sentParts.get(client));
    }

    /**
     * @return The clients List
     */
    List<ClientInfo> getClientList() {
        return clientList;
    }

    /**
     * Represents which data should be saved
     *
     * @return The data
     */
    @Override
    public Object getSaveObject() {
        return clientList;
    }

    /**
     * @return Save file name
     */
    @Override
    public String getSaveFileName() {
        return "clientManager";
    }

    /**
     * Decides which part need to be deleted during delete process
     *
     * @param files      Deleting files
     * @param remainPath Remaining path (their parts should not be removed)
     * @return Each client deleting parts
     */
    Map<ClientInfo, Set<Long>> getDeletingParts(Set<FSFile> files, Set<FSPath> remainPath) {
        Map<ClientInfo, Set<Long>> partsMap = new HashMap<>();

        Set<Long> filesParts = files.stream()
                .flatMap(file -> file.getParts().keySet().stream())
                .collect(Collectors.toSet());

        Set<Long> remainFilesParts = remainPath.stream()
                .flatMap(file -> file instanceof FSFile ? ((FSFile) file).getParts().keySet().stream() : Stream.of())
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

    /**
     * Return the file parts distribution on all of nodes
     *
     * @param file The file
     * @return Each node list for each part
     */
    LinkedHashMap<Long, List<ClientInfo>> getDist(FSFile file) {
        LinkedHashMap<Long, List<ClientInfo>> nodes = new LinkedHashMap<>();
        for (Long partId : file.getParts().keySet()) {
            List<ClientInfo> clients = new ArrayList<>();
            nodes.put(partId, clientList);
            for (ClientInfo client : clientList)
                if (client.getParts().contains(partId))
                    clients.add(client);
        }
        return nodes;
    }

    /**
     * set redundancy count
     *
     * @param redundancy Redundancy count
     */
    void setRedundancy(int redundancy) {
        this.redundancy = redundancy;
    }
}
