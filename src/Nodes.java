import java.util.ArrayList;

public class Nodes {
    private int nodeID;
    private String hostName;
    private int portNumber;
    private ArrayList<Integer> nodalConnections = new ArrayList<Integer>();
    private boolean discovered = false;
    private int parent = null;
    private ArrayList<Integer> children = new ArrayList<Integer>();

    public void setNodeID(int i) {
        this.nodeID = i;
    }

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

    public void setParent(int parent) {
        this.parent = parent;
    }

    public void addChild(int child) {
        this.children.add(child);
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

    public ArrayList<Integer> getNodalConnections() {
        return this.nodalConnections;
    }

    public boolean getDiscovered() {
        return this.discovered;
    }

    public int getParent() {
        return this.parent;
    }

    public ArrayList<Integer> getChildren() {
        return this.children;
    }
}