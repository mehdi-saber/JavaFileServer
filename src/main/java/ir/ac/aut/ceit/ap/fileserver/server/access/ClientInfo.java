package ir.ac.aut.ceit.ap.fileserver.server.access;

import ir.ac.aut.ceit.ap.fileserver.client.view.Controller;
import ir.ac.aut.ceit.ap.fileserver.client.FilePartitionInfo;

import java.io.File;
import java.util.Map;

public class ClientInfo {
    private Map<FilePartitionInfo, File> partitionsMap;
    private Controller controller;

}
