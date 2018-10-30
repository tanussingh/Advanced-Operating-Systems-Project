import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    //set up variables
    private Nodes[] array_of_nodes;
    private int serverNum;

    Server (Nodes[] array_of_nodes, int serverNum) {
        this.array_of_nodes = array_of_nodes;
        this.serverNum = serverNum;
        Thread t = new Thread(this);
        t.start();
    }

    public void sendPacket(Socket outSocket, ObjectOutputStream out, int broadcast, int source, String message, int dest) {
        try {
            Packet packet = new Packet();
            packet.buildPacket(broadcast, source, message);
            System.out.println("Packet to be sent: " + packet + ", to: " + dest);
            outSocket = new Socket(array_of_nodes[dest].getHostName(), array_of_nodes[dest].getPortNumber());
            out = new ObjectOutputStream(outSocket.getOutputStream());
            out.writeObject(packet);
            out.flush();
            out.close();
            outSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        //initialize socket and input/output stream
        ServerSocket server = null;
        Socket inSocket = null;
        Socket outSocket = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;

        //Set up messages
        String reqMsg = "REQ";
        String ackMsg = "ACK";
        String nackMsg = "NACK";

        //set up variables
        int serverPort = this.array_of_nodes[serverNum].getPortNumber();
        String serverHostname = this.array_of_nodes[serverNum].getHostName();
        int parent = -1;
        int[][] records = new int[array_of_nodes.length][2];// index:source 0:node 1:waiting

        //starts server and waits for a connection
        //this block of try is for tree building
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
                    sendPacket(outSocket, out, serverNum, serverNum, reqMsg, dest);
                    records[serverNum][1] += 1;
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
                int broadcastSource = packet.getBroadcastNode();
                int source = packet.getSourceId();
                String msg = packet.getMsg();

                //if it is a request message
                if (msg.equals(reqMsg)) {
                    if (!(array_of_nodes[serverNum].getDiscovered())) {
                        //if this server is not already discovered, set myself to discovered and remember parent
                        array_of_nodes[serverNum].setDiscovered(true);
                        parent = source;
                        records[broadcastSource][0] = source;
                        System.out.println("Discovered Set!, Parent Set for server: " + serverNum);

                        if (array_of_nodes[serverNum].getNodalConnections().size() == 1) {
                            //send ack back to parent because neighbour only 1
                            sendPacket(outSocket, out, broadcastSource, serverNum, ackMsg, records[broadcastSource][0]);
                            System.out.println("Send ACK to parent: " + records[broadcastSource][0] + ". For server: " + serverNum);
                        } else {
                            //send out req to all neighbours
                            for (int i = 0; i < array_of_nodes[serverNum].getNodalConnections().size(); i++) {
                                int dest = array_of_nodes[serverNum].getNodalConnections().get(i);
                                if (dest != parent) {
                                    sendPacket(outSocket, out, broadcastSource, serverNum, reqMsg, dest);
                                    records[broadcastSource][1] += 1;
                                }
                            }
                        }
                    } else if (array_of_nodes[serverNum].getDiscovered()) {
                        //if this server is already discovered, send nack
                        sendPacket(outSocket, out, broadcastSource, serverNum, nackMsg, source);
                        System.out.println("Send NACK to: " + source + ". For server: " + serverNum);
                    }
                } else if (!msg.equals(reqMsg)) {
                    //if it is a ack or nack message
                    records[broadcastSource][1] -= 1;
                    if (msg.equals(ackMsg)) {
                        //if message is ack, add message source to child list
                        array_of_nodes[serverNum].addTreeNeighbours(source);
                        System.out.println("Received ACK from: " + source + ". For server: " + serverNum);
                        System.out.println("New child: " + source + ", added to: " + serverNum);
                    } else if (msg.equals(nackMsg)) {
                        //if message is nack, do nothing with it
                        System.out.println("Received NACK from: " + source + ". For server: " + serverNum);
                    }
                    if (records[broadcastSource][1] == 0 && broadcastSource != serverNum) {
                        //send ack back to parent since all replies recieved
                        sendPacket(outSocket, out, broadcastSource, serverNum, ackMsg, records[broadcastSource][0]);
                        System.out.println("Send ACK to parent: " + records[broadcastSource][0] + ". For server: " + serverNum);
                    }
                }
            } while (records[1][1] != 0);

            //close all ports and servers so it can start new in another try block for broadcasting
            inSocket.close();
            server.close();

            //print tree neighbours
            if (parent != serverNum){
                array_of_nodes[serverNum].addTreeNeighbours(parent);
            }
            System.out.println("Server = " + serverNum + ", Tree Neighbours = " + array_of_nodes[serverNum].getTreeNeighbours());
            System.out.println("--------------------------------------SPANNING TREE DONE--------------------------------------");
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //broadcast to every node
        int messagesToSend = 5;
        try {
            server = new ServerSocket(serverPort);
            System.out.println("Started at Host: " + serverHostname + " Port: " + serverPort);
            Thread.sleep(3000);

            Packet packet;
            //sends messagesToSend messages
            for (int j = 0; j < messagesToSend; j++) {
                //sends to all tree neighbours
                for (int i = 0; i < array_of_nodes[serverNum].getTreeNeighbours().size(); i++) {
                    int dest = array_of_nodes[serverNum].getTreeNeighbours().get(i);
                    sendPacket(outSocket, out, serverNum, serverNum, "Hello", dest);
                    records[serverNum][1] += 1;
                }

                do {
                    System.out.println("Server: Waiting for a messages ...");
                    inSocket = server.accept();
                    System.out.println("Server: Packet received!");
                    in = new ObjectInputStream(inSocket.getInputStream());
                    packet = (Packet) in.readObject();
                    System.out.println("Server: Packet Received - " + packet);
                    in.close();

                    //get info out of msg
                    int broadcastSource = packet.getBroadcastNode();
                    int source = packet.getSourceId();
                    String msg = packet.getMsg();

                    //if record at broadcastSource == 0 then it means its a new message
                    if (!msg.equals(ackMsg)) {
                        records[broadcastSource][0] = source;
                        if (array_of_nodes[serverNum].getTreeNeighbours().size() == 1) {
                            //send ack back to source if neighbour list is 1
                            sendPacket(outSocket, out, broadcastSource, serverNum, ackMsg, source);
                        } else {
                            //continue broadcast message
                            for (int i = 0; i < array_of_nodes[serverNum].getTreeNeighbours().size(); i++) {
                                int dest = array_of_nodes[serverNum].getTreeNeighbours().get(i);
                                if (dest != source) {
                                    sendPacket(outSocket, out, broadcastSource, serverNum, msg, dest);
                                    records[broadcastSource][1] += 1;
                                }
                            }
                        }
                    } else if (msg.equals(ackMsg)) {
                        records[broadcastSource][1] -= 1;
                        if (records[broadcastSource][1] == 0 && broadcastSource != serverNum){
                            //send ack since all replies recieved
                            sendPacket(outSocket, out, broadcastSource, serverNum, ackMsg, records[broadcastSource][0]);
                            System.out.println("Send ACK to parent: " + records[broadcastSource][0] + ". For server: " + serverNum);
                        }
                    }
                } while (records[serverNum][1] != 0);
            }
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
