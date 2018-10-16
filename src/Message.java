import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    private int sourceId;
    private int destId;
    private ArrayList<Integer> path;
    private ArrayList<Integer> neighbour;

    public void buildMessage(int sourceId, int destId, ArrayList<Integer> path) {
        this.sourceId = sourceId;
        this.destId = destId;
        this.path = path;
        this.neighbour = null;
    }

    public void setSourceId (int i) {
        this.sourceId = i;
    }

    public void setDestId (int i) {
        this.destId = i;
    }

    public void setNeighbour(ArrayList<Integer> neighbour) {
        this.neighbour = neighbour;
    }

    public int getSourceId() {
        return this.sourceId;
    }

    public int getDestId() {
        return this.destId;
    }

    public ArrayList<Integer> getPath() {
        return this.path;
    }

    public ArrayList<Integer> getNeighbour() {
        return this.neighbour;
    }

    @Override
    public String toString() {
        return "Message [sourceId=" + sourceId + ", destId=" + this.destId + ", path=" + path + ", neighbour=" + neighbour + "]";
    }
}