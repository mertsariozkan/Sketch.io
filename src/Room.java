import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.concurrent.CopyOnWriteArrayList;

public class Room {
    private int id;
    private CopyOnWriteArrayList<PrintWriter> clientOutputs;
    private CopyOnWriteArrayList<BufferedReader> clientInputs;

    public Room(int id){
        setId(id);
        clientOutputs = new CopyOnWriteArrayList<>();
        clientInputs = new CopyOnWriteArrayList<>();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public CopyOnWriteArrayList<PrintWriter> getClientOutputs() {
        return clientOutputs;
    }
    public CopyOnWriteArrayList<BufferedReader> getClientInputs() {
        return clientInputs;
    }
}
