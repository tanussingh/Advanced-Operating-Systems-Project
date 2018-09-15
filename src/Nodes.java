import java.lang.reflect.Array;
import java.util.ArrayList;

public class Nodes {
    private int nodeID;
    private String hostName;
    private int portNumber;
    //private int[] neighbours;
    private ArrayList<ArrayList<Integer>> nodalConnections = new ArrayList<ArrayList<Integer>>();
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
        for (int j = 1; j < i+1; j++){
            nodalConnections.add(new ArrayList<Integer>());
        }
    }

    //for parser
    public void addNodalConnections(int hops, int i) {
        this.nodalConnections.get(hops).add(i);
    }

    //for client
    public void addNodalConnections(int hops, ArrayList<Integer> i) {
        this.nodalConnections.get(hops).addAll(i);
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

    public ArrayList getNodalConnections(int hops) {
        return this.nodalConnections.get(hops);
    }
}
