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
    private int expectedReplies = 0;

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
        int serverPort = this.array_of_nodes[serverNum].getPortNumber();
        String serverHostname = this.array_of_nodes[serverNum].getHostName();
        // starts server and waits for a connection
        try {
            server = new ServerSocket(serverPort);
            System.out.println("Started at Host: " + serverHostname + " Port: " + serverPort);
            //ONLY SERVER 1 RUNS THIS IF BLOCK OF CODE
            Packet packet;
            if (serverNum == 1) {
                Thread.sleep(3000);
                array_of_nodes[serverNum].setDiscovered(true);
                array_of_nodes[serverNum].setParent(serverNum);
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

                //if flag == 0
                if (msg.equals(reqMsg)) {
                    if (!(array_of_nodes[serverNum].getDiscovered())) {
                        //set myself to discovered and remember parent
                        array_of_nodes[serverNum].setDiscovered(true);
                        array_of_nodes[serverNum].setParent(dest);
                        System.out.println("Discovered Set!, Parent Set for server: " + serverNum);

                        //send out req to my neighbours
                        for (int i = 0; i < array_of_nodes[serverNum].getNodalConnections().size(); i++) {
                            dest = array_of_nodes[serverNum].getNodalConnections().get(i);
                            if (dest != array_of_nodes[serverNum].getParent()) {
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
                        //send nack
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
                    expectedReplies -= 1;
                    if (msg.equals(ackMsg)) {
                        //here dest is source number. Cool? Cool
                        array_of_nodes[serverNum].addChild(dest);
                        System.out.println("Recieved ACK from: " + dest + ". For server: " + serverNum);
                        System.out.println("New child: " + dest + ", added to: " + serverNum);
                    } else if (msg.equals(nackMsg)) {
                        //do nothing with it
                        System.out.println("Recieved NACK from: " + dest + ". For server: " + serverNum);
                    }
                }
            } while (expectedReplies != 0);
            //send ack back to parent
            System.out.print("LOOKING FOR THIS LINE PLEASE");
            packet = new Packet();
            packet.buildPacket(serverNum, ackMsg);
            outSocket = new Socket(array_of_nodes[array_of_nodes[serverNum].getParent()].getHostName(), array_of_nodes[array_of_nodes[serverNum].getParent()].getPortNumber());
            out = new ObjectOutputStream(outSocket.getOutputStream());
            out.writeObject(packet);
            out.flush();
            out.close();
            outSocket.close();
            System.out.println("Send ACK to parent: " + array_of_nodes[serverNum].getParent() + ". For server: " + serverNum);

            in.close();
            inSocket.close();
            server.close();
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //print tree neighbours
        System.out.println("Parent = " + array_of_nodes[serverNum].getParent() + ", Children = " + array_of_nodes[serverNum].getChildren());

        //broadcast
        try {
            server = new ServerSocket(serverPort);
            System.out.println("Started at Host: " + serverHostname + " Port: " + serverPort);
            Thread.sleep(3000);
            if (serverNum == 1) {
                
            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
