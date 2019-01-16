package ir.ac.aut.ceit.ap.fileserver.filesys;

abstract public class PathInfo {
    protected String address;

    public PathInfo(String address) {
        this.address = address;
    }

    public String getName() {
        return address.substring(address.lastIndexOf("/")+1, address.length() - 1);
    }

    public String getAddress() {
        return address;
    }
}
