package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.util.HashUtil;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FilePartManager {
    private List<ClientInfo> clientList;
    private int splitSize;

    FilePartManager(int splitSize, List<ClientInfo> clientList) {
        this.splitSize = splitSize;
        this.clientList = clientList;

    }

    Map<ClientInfo, Pair<String, byte[]>> splitFile(byte[] file) {
        Map<ClientInfo, Pair<String, byte[]>> map = new HashMap<>();
        int cIndex = 0;
        for (int i = 0; true; i++) {
            int from = i * splitSize;
            int to = Math.min(from + splitSize, file.length);
            if (from > file.length)
                break;
            byte[] subArray = Arrays.copyOfRange(file, from, to);
            String hash = HashUtil.md5Hash(subArray);
            cIndex = clientList.size() < cIndex + 1 ? 0 : cIndex + 1;
            map.put(clientList.get(cIndex), new Pair<>(hash, subArray));
        }
        return map;
    }
}
