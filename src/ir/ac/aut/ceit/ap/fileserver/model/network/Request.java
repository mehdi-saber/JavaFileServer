package ir.ac.aut.ceit.ap.fileserver.model.network;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private Map<String, Object> parameterMap;

    public Request(Command command) {
        parameterMap = new HashMap<>();
        parameterMap.put("command", command);
    }


    public Request(InputStream is) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(is);
        int parametersSize = ois.readInt();
        for (long pIndex = 0; pIndex < parametersSize; pIndex++) {
            int keyLen = ois.readInt();
            String key = "";
            for (int i = 0; i < keyLen; i++)
                key += (char) ois.readByte();
            Object parameter = ois.readObject();
            parameterMap.put(key, parameter);
        }
    }


    public void send(OutputStream os) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(os);
        out.write(parameterMap.size());
        for (Map.Entry<String, Object> pItem : parameterMap.entrySet()) {
            out.writeInt(pItem.getKey().length());
            out.writeBytes(pItem.getKey());
            out.writeObject(pItem.getValue());
        }
    }

    private void addObject(String key, Object value) {
        parameterMap.put(key, value);
    }

    public Object getObject(String key) {
        return parameterMap.get(key);
    }

    public Command getCommand() {
        return (Command) getObject("command");
    }
}
