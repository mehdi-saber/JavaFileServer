package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.filesys.AddressInfo;
import ir.ac.aut.ceit.ap.fileserver.filesys.DirectoryInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FileSystem {
    List<AddressInfo> addressList;

    public FileSystem(List<AddressInfo> addressList) {
        this.addressList = new LinkedList<>();
    }

    public List<AddressInfo> listSubDirAndFiles(DirectoryInfo directory) {
        return addressList.stream()
                .filter(address -> !address.equals(directory) && address.getAddress().startsWith(directory.getAddress()))
                .collect(Collectors.toList());
    }

    public boolean dirExists(String dir) {
        return addressList.stream()
                .anyMatch(address -> address.getAddress().equals(dir));
    }
}
