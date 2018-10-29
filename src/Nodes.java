import java.util.ArrayList;

public class Nodes {
    private String hostName;
    private int portNumber;
    private ArrayList<Integer> nodalConnections = new ArrayList<Integer>();
    private boolean discovered = false;
    private ArrayList<Integer> treeNeighbours = new ArrayList<Integer>();

    public void setHostName(String i) {
        this.hostName = i;
    }

    public void setPortNumber (int i) {
        this.portNumber = i;
    }

    public void addNodalConnections(int neighbour) {
        this.nodalConnections.add(neighbour);
    }

    public void setDiscovered(boolean value){
        this.discovered = value;
    }

    public void addTreeNeighbours(int num) {
        this.treeNeighbours.add(num);
    }

    public String getHostName() {
        return this.hostName;
    }

    public int getPortNumber() {
        return this.portNumber; 
    }

    public ArrayList<Integer> getNodalConnections() {
        return this.nodalConnections;
    }

    public boolean getDiscovered() {
        return this.discovered;
    }

    public ArrayList<Integer> getTreeNeighbours() {
        return this.treeNeighbours;
    }
}