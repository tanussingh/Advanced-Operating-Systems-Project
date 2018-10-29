import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    //set up variables
    int messagesToSend = 5;
    private Nodes[] array_of_nodes;
    private int serverNum;
    private int expectedReplies = 0;
    //private int[][] records = new int[array_of_nodes.length - 1][3];
    private int parent = -1;
    /*
    source
    node
    waiting
     */

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
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        int serverPort = this.array_of_nodes[serverNum].getPortNumber();
        String serverHostname = this.array_of_nodes[serverNum].getHostName();

        //starts server and waits for a connection
        //this block of try is fro tree building
        try {
            server = new ServerSocket(serverPort);
            System.out.println("Started at Host: " + serverHostname + " Port: " + serverPort);
            //ONLY SERVER 1 RUNS THIS IF BLOCK OF CODE
            Packet packet;
            if (serverNum == 1) {
                Thread.sleep(3000);
                array_of_nodes[serverNum].setDiscovered(true);
                parent = serverNum;
                //send to all children ie neighbours
                for (int i = 0; i < array_of_nodes[serverNum].getNodalConnections().size(); i++) {
                    int dest = array_of_nodes[serverNum].getNodalConnections().get(i);
                    packet = new Packet();
                    packet.buildPacket(serverNum, reqMsg);
                    System.out.println("Packet to be sent: " + packet + ", to: " + dest);
                    outSocket = new Socket(array_of_nodes[dest].getHostName(), array_of_nodes[dest].getPortNumber());
                    out = new ObjectOutputStream(outSocket.getOutputStream());
                    out.writeObject(packet);
                    out.flush();
                    out.close();
                    outSocket.close();
                    expectedReplies += 1;
                }
            }

            //once server 1 completes, all server is at this stage
            do {
                System.out.println("Server: Waiting for a client ...");
                inSocket = server.accept();
                System.out.println("Server: Client accepted");
                in = new ObjectInputStream(inSocket.getInputStream());
                packet = (Packet) in.readObject();
                System.out.println("Server: Packet Received - " + packet);
                in.close();

                //get info out of msg
                int dest = packet.getSourceId();
                String msg = packet.getMsg();

                //if it is a request message
                if (msg.equals(reqMsg)) {
                    if (!(array_of_nodes[serverNum].getDiscovered())) {
                        //if this server is not already discovered, set myself to discovered and remember parent
                        array_of_nodes[serverNum].setDiscovered(true);
                        parent = dest;
                        System.out.println("Discovered Set!, Parent Set for server: " + serverNum);

                        //send out req to my neighbours
                        for (int i = 0; i < array_of_nodes[serverNum].getNodalConnections().size(); i++) {
                            dest = array_of_nodes[serverNum].getNodalConnections().get(i);
                            if (dest != parent) {
                                packet = new Packet();
                                packet.buildPacket(serverNum, reqMsg);
                                System.out.println("Packet to be sent: " + packet + ", to: " + dest);
                                outSocket = new Socket(array_of_nodes[dest].getHostName(), array_of_nodes[dest].getPortNumber());
                                out = new ObjectOutputStream(outSocket.getOutputStream());
                                out.writeObject(packet);
                                out.flush();
                                out.close();
                                outSocket.close();
                                expectedReplies += 1;
                            }
                        }
                    } else if (array_of_nodes[serverNum].getDiscovered()) {
                        //if this server is already discovered, send nack
                        packet = new Packet();
                        packet.buildPacket(serverNum, nackMsg);
                        outSocket = new Socket(array_of_nodes[dest].getHostName(), array_of_nodes[dest].getPortNumber());
                        out = new ObjectOutputStream(outSocket.getOutputStream());
                        out.writeObject(packet);
                        out.flush();
                        out.close();
                        outSocket.close();
                        System.out.println("Send NACK to: " + dest + ". For server: " + serverNum);
                    }
                } else if (!msg.equals(reqMsg)) {
                    //if it is a ack or nack message
                    expectedReplies -= 1;
                    if (msg.equals(ackMsg)) {
                        //if message is ack, add message source to child list
                        array_of_nodes[serverNum].addTreeNeighbours(dest);
                        System.out.println("Received ACK from: " + dest + ". For server: " + serverNum);
                        System.out.println("New child: " + dest + ", added to: " + serverNum);
                    } else if (msg.equals(nackMsg)) {
                        //if message is nack, do nothing with it
                        System.out.println("Received NACK from: " + dest + ". For server: " + serverNum);
                    }
                }
            } while (expectedReplies != 0);

            //send ack back to parent
            packet = new Packet();
            packet.buildPacket(serverNum, ackMsg);
            outSocket = new Socket(array_of_nodes[parent].getHostName(), array_of_nodes[parent].getPortNumber());
            out = new ObjectOutputStream(outSocket.getOutputStream());
            out.writeObject(packet);
            out.flush();
            out.close();
            outSocket.close();
            System.out.println("Send ACK to parent: " + parent + ". For server: " + serverNum);

            //close all ports and servers so it can start new in another try block for broadcasting
            inSocket.close();
            server.close();
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //print tree neighbours
        array_of_nodes[serverNum].addTreeNeighbours(parent);
        System.out.println("Server = " + serverNum + ", Tree Neighbours = " + array_of_nodes[serverNum].getTreeNeighbours());

        //broadcast to every node
        /*try {
            boolean finished = false;
            server = new ServerSocket(serverPort);
            System.out.println("Started at Host: " + serverHostname + " Port: " + serverPort);
            Thread.sleep(3000);

            Packet packet;
            for (int i = 0; i < array_of_nodes[serverNum].getTreeNeighbours().size(); i++) {
                int dest = array_of_nodes[serverNum].getTreeNeighbours().get(i);
                packet = new Packet();
                packet.buildPacket(serverNum, "Hello");
                System.out.println("Packet to be sent: " + packet + ", to: " + dest);
                outSocket = new Socket(array_of_nodes[dest].getHostName(), array_of_nodes[dest].getPortNumber());
                out = new ObjectOutputStream(outSocket.getOutputStream());
                out.writeObject(packet);
                out.flush();
                out.close();
                outSocket.close();
            }

            while (!finished) {
                System.out.println("Server: Waiting for a messages ...");
                inSocket = server.accept();
                System.out.println("Server: Packet received!");
                in = new ObjectInputStream(inSocket.getInputStream());
                packet = (Packet) in.readObject();
                System.out.println("Server: Packet Received - " + packet);
                in.close();

                //get info out of msg
                String msg = packet.getMsg();

                //send out message to my children
                for (int i = 0; i < array_of_nodes[serverNum].getTreeNeighbours().size(); i++) {
                    int dest = array_of_nodes[serverNum].getTreeNeighbours().get(i);
                    if (dest != parent) {
                        packet = new Packet();
                        packet.buildPacket(serverNum, msg);
                        System.out.println("Packet to be sent: " + packet + ", to: " + dest);
                        outSocket = new Socket(array_of_nodes[dest].getHostName(), array_of_nodes[dest].getPortNumber());
                        out = new ObjectOutputStream(outSocket.getOutputStream());
                        out.writeObject(packet);
                        out.flush();
                        out.close();
                        outSocket.close();
                    }
                }
            }
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }*/
    }
}
