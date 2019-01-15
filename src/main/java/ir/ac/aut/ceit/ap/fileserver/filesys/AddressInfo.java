package ir.ac.aut.ceit.ap.fileserver.filesys;

public class AddressInfo {
    protected String address;

    public AddressInfo(String address) {
        this.address = address;
    }

    public String getName() {
        return address.substring(address.lastIndexOf("/")+1, address.length() - 1);
    }

    public String getAddress() {
        return address;
    }
}
