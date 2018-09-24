import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Server extends Thread {
    //set up variables
    private Nodes[] array_of_nodes;
    private int source;
    private Thread t = null;

    //initialize socket and input/output stream
    private ServerSocket server = null;
    private Socket socket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    Server (Nodes[] array_of_nodes, int source) {
        this.array_of_nodes = array_of_nodes;
        this.source = source;
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
            int port = this.array_of_nodes[source].getPortNumber();
            // starts server and waits for a connection
            try {
                server = new ServerSocket(port);
                System.out.println("Server: Started at Host: " + this.array_of_nodes[source].getHostName() + " Port: " + port);
                while (!finished) {
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
                    if (Objects.equals(msg.getDestId(), this.source) && msg.getNeighbour() != null) {
                        //stuff in neighbour is for this server
                        System.out.println("Server: Updating...");
                        Collections.reverse(msg.getPath());
                        for (int i = 0; i < msg.getNeighbour().size(); i++) {
                            if ((array_of_nodes[source].getNodalConnections(msg.getNeighbour().get(i)).isEmpty()) && (msg.getNeighbour().get(i) != source)) {
                                array_of_nodes[source].addNodalConnections(msg.getPath(), msg.getNeighbour().get(i));
                                array_of_nodes[source].addNodalConnections(msg.getNeighbour().get(i));
                            } else if ((array_of_nodes[source].getNodalConnections(msg.getNeighbour().get(i)).size() > ((msg.getPath().size()) + 1)) && (msg.getNeighbour().get(i) != source)) {
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
                }
            } catch (IOException | ClassNotFoundException i) {
                i.printStackTrace();
            }

    }
}