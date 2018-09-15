import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Server extends Thread {
    //set up variables
    private Nodes node;
    private Thread t = null;

    //initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    Server (Nodes node) {
        this.node = node;
    }

    public void start () {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    // constructor with port
    public void run() {
        int port = this.node.getPortNumber();
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
            System.out.println("Server: Server started at Host: " + this.node.getHostName() + " Port: " + port);
            System.out.println("Server: Waiting for a client ...");

            socket = server.accept();
            System.out.println("Server: Client accepted");

            // takes input from the client socket
            Message msg = new Message();
            in = new ObjectInputStream(socket.getInputStream());
            msg = (Message) in.readObject();
            System.out.println("Server: Message Received - " + msg);

            //reply to client
            if (Objects.equals(msg.getDest_address(), this.node.getHostName())) {
                msg.setNeighbour(this.node.getNodalConnections(1));
                System.out.println("Server: Message to be sent back " + msg);
                out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(msg);
                out.flush();
            }
            // close connection
            socket.close();
            in.close();
            System.out.println("Server Thread Closed.");
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }
}