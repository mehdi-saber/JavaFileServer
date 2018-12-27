package ir.ac.aut.ceit.ap.fileserver.model.filesys;

abstract public class AddressInfo {
    protected String address;

    public String getName() {
        return address.substring(address.lastIndexOf("/"), address.length() - 1);
    }

    public String getAddress() {
        return address;
    }
}
