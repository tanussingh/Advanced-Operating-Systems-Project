import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client extends Thread {
    //set up the variables
    private Nodes source;
    private Nodes dest;
    private int hops;
    private Thread t = null;

    // initialize socket and input output streams
    private Socket socket = null;
    private ObjectInputStream input = null;
    private ObjectOutputStream out = null;

    Client (Nodes source, Nodes dest, int hops) {
        this.source = source;
        this.dest = dest;
        this.hops = hops;
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
            String address = dest.getHostName();
            int port = dest.getPortNumber();
            // establish a connection
            try {
                System.out.println("Client connecting to Host: " + address + " Port: " + port);
                socket = new Socket(address, port);
                System.out.println("Client: Connected");

                // set up message
                Message msg = new Message();
                ArrayList<Integer> path = PathFinder.find(source, dest, hops);
                msg.buildMessage(address, port, path);
                System.out.println("Client: Message to be sent: " + msg);

                // sends output to the socket
                out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(msg);
                out.flush();
            } catch (UnknownHostException u) {
                u.printStackTrace();
            } catch (IOException i) {
                i.printStackTrace();
            }

            // string to read message from input
            Message msg_in = new Message();

            // read reply from server
            try {
                input = new ObjectInputStream(socket.getInputStream());
                msg_in = (Message) input.readObject();
                System.out.println("Client: Message received from server " + msg_in);
                //pass msg.neighbour back to main
                                                                                                                                                        // before adding check for dups
                source.addNodalConnections(hops + 1, msg_in.getNeighbour());
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
            } catch (IOException i) {
                i.printStackTrace();
            }

            // close the connection
            try {
                input.close();
                out.close();
                socket.close();
                System.out.println("Client thread closed.");
            } catch (IOException i) {
                i.printStackTrace();
            }
            finished = true;
        }

        //return reply from server
    }
}