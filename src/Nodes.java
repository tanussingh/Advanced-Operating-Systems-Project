import java.util.ArrayList;

public class Nodes {
    private int nodeID;
    private String hostName;
    private int portNumber;
    private ArrayList<Integer>[] nodalConnections;
    private boolean known;
    private boolean discovered;

    public void setNodeID(int i) {
        this.nodeID = i;
    }

    public void setHostName(String i) {
        this.hostName = i;
    }

    public void setPortNumber (int i) {
        this.portNumber = i;
    }

    public void setNodalConnections(int i) {
        nodalConnections = new ArrayList[i];
        for (int j = 0; j < i; j++){
            nodalConnections[j] = new ArrayList<>();
        }
    }

    public void addNodalConnections(ArrayList<Integer> path, int dest) {
        this.nodalConnections[dest] = new ArrayList<>();
        this.nodalConnections[dest].addAll(path);
    }

    public void addNodalConnections(int dest) {
        this.nodalConnections[dest].add(dest);
    }

    public int getNodeID() {
        return this.nodeID;
    }

    public String getHostName() {
        return this.hostName;
    }

    public int getPortNumber() {
        return this.portNumber; 
    }

    public ArrayList<Integer> getNodalConnections(int i) {
        return this.nodalConnections[i];
    }

    public int getNodalConnectionsLength() {
        return this.nodalConnections.length;
    }

    public void setKnown(boolean value){
        this.known = value;
    }

    public void setDiscovered(boolean value){
        this.discovered = value;
    }
}