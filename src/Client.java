import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;

public class Client extends Thread {
    //set up the variables
    private Nodes source_node;
    private Nodes dest_node;
    private int destId;
    private ArrayList<Integer> path;
    private Thread t = null;

    // initialize socket and input output streams
    private Socket socket = null;
    private ObjectOutputStream out = null;

    Client (Nodes source_node, Nodes dest_node, int destId) {
        this.source_node = source_node;
        this.dest_node = dest_node;
        this.destId = destId;
        this.path = source_node.getNodalConnections(destId);
    }

    public void start () {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    // constructor to put ip address and port
    public void run() {
        boolean finished = false;
        while (!finished) {
            // establish a connection
            try {
                System.out.println("Client is sleeping");
                t.sleep(5000);
                System.out.println("Client started again");
                System.out.println("Client connecting to Host: " + dest_node.getHostName() + " Port: " + dest_node.getPortNumber());
                socket = new Socket(dest_node.getHostName(), dest_node.getPortNumber());
                System.out.println("Client: Connected");

                // set up message
                Message msg = new Message();
                msg.buildMessage(source_node.getNodeID(), destId, path);
                System.out.println("Client: Message to be sent: " + msg);

                // sends output to the socket
                out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(msg);
                out.flush();
            } catch (UnknownHostException x) {
                x.printStackTrace();
            } catch (IOException y) {
                y.printStackTrace();
            } catch (InterruptedException z) {
                z.printStackTrace();
            }

            // close the connection
            try {
                out.close();
                socket.close();
                System.out.println("Client thread closed.");
            } catch (IOException i) {
                i.printStackTrace();
            }
            finished = true;
        }
    }
}