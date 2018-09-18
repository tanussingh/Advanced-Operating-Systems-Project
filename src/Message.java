import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    private String dest_address;
    private int dest_port;
    private ArrayList<Integer> path;
    private ArrayList<Integer> neighbour = null;

    public void buildMessage(String dest_address, int dest_port, ArrayList<Integer> path) {
        this.dest_address = dest_address;
        this.dest_port = dest_port;
        this.path = path;
    }

    public void setNeighbour(ArrayList<Integer> neighbour) {
        this.neighbour = neighbour;
    }

    public String getDest_address() {
        return dest_address;
    }

    public ArrayList<Integer> getNeighbour () {
        return this.neighbour;
    }

    @Override
    public String toString() {
        return "Message [dest_address=" + this.dest_address + ", dest_port=" + dest_port + ", path=" + path + ", neighbour=" + neighbour + "]";
    }
}
