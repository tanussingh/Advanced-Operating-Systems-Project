import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    private int hops;
    private String dest_address;
    private int dest_port;
    private int[] route;
    private ArrayList<Integer> neighbour = new ArrayList<>();

    public void buildMessage(String dest_address, int dest_port, ArrayList<Integer> list) {
        this.dest_address = dest_address;
        this.dest_port = dest_port;
        this.neighbour = list;
    }

    public ArrayList<Integer> getNeighbour () {
        return this.neighbour;
    }

    @Override
    public String toString() {
        return "Message [hops=" + hops + ", dest_address=" + this.dest_address + ", dest_port=" + dest_port + ", route=" + route + ", neighbour=" + neighbour + "]";
    }
}
