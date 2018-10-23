import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Server extends Thread {
    //set up variables
    private Nodes[] array_of_nodes;
    private int serverNum;
    private Thread t = null;
    private int parent;

    //initialize socket and input/output stream
    private ServerSocket server = null;
    private Socket inSocket = null;
    private Socket outSocket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    //Set up messages
    String reqMsg = "REQ";
    String ackMsg = "ACK";
    String nackMsg = "NACK";

    Server (Nodes[] array_of_nodes, int serverNum) {
        this.array_of_nodes = array_of_nodes;
        this.serverNum = serverNum;
    }

    public void start () {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    // constructor with port
    public void run() {
        boolean finished = false;
            int serverPort = this.array_of_nodes[serverNum].getPortNumber();
            String serverHostname = this.array_of_nodes[serverNum].getHostName();
            // starts server and waits for a connection
            try {
                server = new ServerSocket(serverPort);
                System.out.println("Server: Started at Host: " + serverHostname + " Port: " + serverPort);
                Thread.sleep(2000);
                if (serverNum == 1) {
                    array_of_nodes[serverNum].setDiscovered(true);
                    //send to all children ie neighbours
                    for (int i = 0; i < array_of_nodes[serverNum].getNodalConnections().size(); i++) {
                        int dest = array_of_nodes[serverNum].getNodalConnections().get(i);
                        Packet packet = new Packet();
                        packet.buildPacket(array_of_nodes[serverNum].getNodeID(), array_of_nodes[dest].getNodeID(), reqMsg);
                        System.out.println("Client: Packet to be sent: " + packet);
                        outSocket = new Socket(array_of_nodes[dest].getHostName(), array_of_nodes[dest].getPortNumber());
                        out = new ObjectOutputStream(outSocket.getOutputStream());
                        out.writeObject(packet);
                        out.flush();
                        out.close();
                        outSocket.close();
                    }
                }
                while (!finished) {
                    System.out.println("Server: Waiting for a client ...");
                    inSocket = server.accept();
                    System.out.println("Server: Client accepted");
                    in = new ObjectInputStream(inSocket.getInputStream());
                    Packet packet = (Packet) in.readObject();
                    System.out.println("Server: Packet Received - " + packet);
                    in.close();

                    //get info out of msg
                    int source = packet.getSourceId();
                    int dest = packet.getDestId();
                    String msg = packet.getMsg();

                    //if flag == 0
                    if (msg == reqMsg) {
                        if (!(array_of_nodes[source].getDiscovered())) {
                            //return ack
                            packet = new Packet();
                            packet.buildPacket(dest, source, ackMsg);
                            outSocket = new Socket(array_of_nodes[source].getHostName(), array_of_nodes[source].getPortNumber());
                            out = new ObjectOutputStream(outSocket.getOutputStream());
                            out.writeObject(packet);
                            out.flush();
                            out.close();
                            outSocket.close();
                            
                            //mark own flag and remember parent
                            array_of_nodes[source].setDiscovered(true);
                            parent = source;
                        } else if (array_of_nodes[source].getDiscovered()) {
                            //send nack
                            packet = new Packet();
                            packet.buildPacket(dest, source, nackMsg);
                            outSocket = new Socket(array_of_nodes[source].getHostName(), array_of_nodes[source].getPortNumber());
                            out = new ObjectOutputStream(outSocket.getOutputStream());
                            out.writeObject(packet);
                            out.flush();
                            out.close();
                            outSocket.close();
                        }
                    } else if (msg != reqMsg) {

                    }
                }
                /*while (!finished) {
                    System.out.println("Server: Waiting for a client ...");
                    socket = server.accept();
                    System.out.println("Server: Client accepted");

                    // takes input from the client socket
                    in = new ObjectInputStream(socket.getInputStream());
                    Message msg = (Message) in.readObject();
                    System.out.println("Server: Message Received - " + msg);
                    in.close();
                    socket.close();

                    /*
                    3 cases
                    1. if dest = this server && neighbour != null
                    2. if dest = this server && neighbour = null
                    3. if dest != this server
                     */
                    /*
                    to send back to orig source, replace destID and invert path
                     */
                    /*if (Objects.equals(msg.getDestId(), this.source) && msg.getNeighbour() != null) {
                        //stuff in neighbour is for this server
                        System.out.println("Server: Updating...");
                        Collections.reverse(msg.getPath());
                        for (int i = 0; i < msg.getNeighbour().size(); i++) {
                            if ((array_of_nodes[source].getNodalConnections(msg.getNeighbour().get(i)).size() > msg.getPath().size() ) && (msg.getNeighbour().get(i) != source)) {
                                array_of_nodes[source].addNodalConnections(msg.getPath(), msg.getNeighbour().get(i));
                                array_of_nodes[source].addNodalConnections(msg.getNeighbour().get(i));
                            }else if ((array_of_nodes[source].getNodalConnections(msg.getNeighbour().get(i)).isEmpty()) && (msg.getNeighbour().get(i) != source)) {
                                array_of_nodes[source].addNodalConnections(msg.getPath(), msg.getNeighbour().get(i));
                                array_of_nodes[source].addNodalConnections(msg.getNeighbour().get(i));
                            }
                        }
                        System.out.println("Server: Updated");
                    } else if (Objects.equals(msg.getDestId(), this.source) && msg.getNeighbour() == null) {
                        ArrayList<Integer> neighbours = new ArrayList<>();
                        for (int i = 0; i < array_of_nodes[source].getNodalConnectionsLength(); i++) {
                            if (array_of_nodes[source].getNodalConnections(i).size() == 2) {
                                neighbours.add(i);
                            }
                        }
                        msg.setNeighbour(neighbours);
                        Collections.reverse(msg.getPath());
                        msg.setDestId(msg.getSourceId());
                        msg.setSourceId(source);
                        System.out.println("Server: Message to be sent back " + msg);

                        int i = msg.getPath().indexOf(source);
                        i = msg.getPath().get(i + 1);
                        String next_hostname = array_of_nodes[i].getHostName();
                        int next_port = array_of_nodes[i].getPortNumber();
                        System.out.println("Server: Connecting to Host: " + next_hostname + " Port: " + next_port);
                        socket = new Socket(next_hostname, next_port);
                        System.out.println("Server: Connected");
                        out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(msg);
                        out.flush();
                        out.close();
                        socket.close();
                    } else if (!Objects.equals(msg.getDestId(), this.source)) {
                        System.out.println("Server: Passing on msg");
                        int i = msg.getPath().indexOf(source);
                        i = msg.getPath().get(i + 1);
                        String next_hostname = array_of_nodes[i].getHostName();
                        int next_port = array_of_nodes[i].getPortNumber();
                        System.out.println("Server: connecting to Host: " + next_hostname + " Port: " + next_port);
                        socket = new Socket(next_hostname, next_port);
                        System.out.println("Server: Connected");
                        out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(msg);
                        out.flush();
                        out.close();
                        socket.close();
                    } else {
                        System.out.println("Server: Error Occurred, This should never print out");
                    }
                }*/
            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                e.printStackTrace();
            }

    }
}
