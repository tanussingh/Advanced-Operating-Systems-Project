import java.lang.reflect.Array;
import java.util.ArrayList;

public class Nodes {
    private int nodeID;
    private String hostName;
    private int portNumber;
    //private int[] neighbours;
    private ArrayList<Integer>[] nodalConnections; //new ArrayList<ArrayList<Integer>>();
    //private int[][] neighbours;

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
            nodalConnections[i] = new ArrayList<Integer>();
        }
    }

    //for parser
    public void addNodalConnections(ArrayList<Integer> path, int dest) {
        this.nodalConnections[dest] = path;
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

    public ArrayList getNodalConnections(int i) {
        return this.nodalConnections[i];
    }

    public int getNodalConnectionsLength() {
        return this.nodalConnections.length;
    }
}
