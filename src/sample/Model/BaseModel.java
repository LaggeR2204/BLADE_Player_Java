package sample.Model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class BaseModel implements Serializable {
    public void writeToFile(ObjectOutputStream out) throws IOException {
        out.writeObject(this);
        out.close();
    }
}
