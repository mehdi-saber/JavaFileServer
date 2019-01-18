package ir.ac.aut.ceit.ap.fileserver.server;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FilePartManager {
    private List<ClientInfo> clientList;
    FileIdentityManager fetchIdentityManager;
    FileIdentityManager partIdentityManager;
    private int splitSize;
    private int repeat;

    FilePartManager(List<ClientInfo> clientList) {
        this.splitSize = 3 * 1024 * 1024;
        this.repeat = 2;
        this.clientList = clientList;
        this.fetchIdentityManager = new FileIdentityManager("fetch", 0L);
        this.partIdentityManager = new FileIdentityManager("part", 0L);
    }


    List<Long> splitFile(Long fetchId) {
        List<Long> partList = new ArrayList<>();
        try {
            File fetchFile = fetchIdentityManager.getFileById(fetchId);
            long fileSize = fetchFile.length();
            FileInputStream fileInputStream = new FileInputStream(fetchFile);
            byte[] buffer = new byte[splitSize];
            int len;
            while (fileSize > 0 && (len = fileInputStream.read(buffer)) != -1) {
                fileSize -= len;
                ByteInputStream byteInputStream = new ByteInputStream(buffer, len);
                partList.add(partIdentityManager.storeFile(byteInputStream, (long) len));
            }
            fetchFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return partList;
    }


    Map<ClientInfo, List<Long>> getDestinations(List<Long> parts) {
        Map<ClientInfo, List<Long>> receivingClients = new HashMap<>();
        int cIndex = 0;
        for (ClientInfo client : clientList)
            receivingClients.put(client, new ArrayList<>());

        for (Long partId : parts)
            for (int i = 0; i < repeat; i++) {
                cIndex = clientList.size() <= cIndex + 1 ? 0 : cIndex + 1;
                ClientInfo clientInfo = clientList.get(cIndex);
                receivingClients.get(clientInfo).add(partId);
            }

        return receivingClients;
    }
}
