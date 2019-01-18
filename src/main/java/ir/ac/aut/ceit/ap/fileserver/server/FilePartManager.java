package ir.ac.aut.ceit.ap.fileserver.server;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import ir.ac.aut.ceit.ap.fileserver.network.ReceivingMessage;
import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FilePartManager {
    private List<ClientInfo> clientList;
    FileIdentityManager splits;
    FileIdentityManager parts;
    private int splitSize;
    private int repeat;
    private Server server;

    FilePartManager(List<ClientInfo> clientList, Server server) {
        this.splitSize = 3 * 1024 * 1024;
        this.repeat = 2;
        this.clientList = clientList;
        this.splits = new FileIdentityManager("temp", 0L);
        this.parts = new FileIdentityManager("part", 0L);
        this.server = server;
    }

    void getAndSplitFile(ReceivingMessage request) {
        try {
            Long fetchId = parts.createId();
            File fetchFile = splits.getFileById(fetchId);
            IOUtil.writeI2O(
                    new FileOutputStream(fetchFile),
                    request.getStream("file"),
                    request.getStreamSize("file")
            );

            List<Long> partList = splitFile(fetchId);
            Map<ClientInfo, List<Long>> destinations = getDestinations(partList);
            server.sendParts( destinations);

            for (Long partId : partList)
                parts.getFileById(partId).delete();
            fetchFile.delete();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    List<Long> splitFile(Long fetchId) {
        List<Long> partList = new ArrayList<>();
        try {
            File fetchFile = splits.getFileById(fetchId);
            long fileSize = fetchFile.length();
            FileInputStream fileInputStream = new FileInputStream(fetchFile);

            byte[] buffer = new byte[splitSize];
            int len;
            while (fileSize > 0 && (len = fileInputStream.read(buffer)) != -1) {
                fileSize -= len;
                ByteInputStream byteInputStream = new ByteInputStream(buffer, len);
                long partId = parts.createId();
                IOUtil.writeI2O(new FileOutputStream(parts.getFileById(partId)), byteInputStream, (long) len);
                partList.add(partId);
            }

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
