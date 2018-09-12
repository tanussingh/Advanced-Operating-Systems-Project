import java.util.ArrayList;

public class Nodes {
    private int nodeID;
    private String hostName;
    private int portNumber;
    private ArrayList<Integer> nodalConnections = new ArrayList<>();

    public void setNodeID(int i) {
        this.nodeID = i;
    }

    public void setHostName(String i) {
        this.hostName = i;
    }

    public void setPortNumber (int i) {
        this.portNumber = i;
    }

    public void addNodalConnections(int i) {
        this.nodalConnections.add(i);
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

    public ArrayList getNodalConnections() {
        return this.nodalConnections;
    }
}
