package ir.ac.aut.ceit.ap.fileserver.model.server.access;

import ir.ac.aut.ceit.ap.fileserver.controller.ClientViewController;
import ir.ac.aut.ceit.ap.fileserver.model.client.FilePartitionInfo;

import java.io.File;
import java.util.Map;

public class ClientInfo {
    private Map<FilePartitionInfo, File> partitionsMap;
    private ClientViewController viewController;

}
