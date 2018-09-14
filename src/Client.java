import java.io.*;
import java.net.*;

public class Client extends Thread {
    //set up the variables
    private Nodes node;
    private Thread t = null;

    // initialize socket and input output streams
    private Socket socket = null;
    private ObjectInputStream input = null;
    private ObjectOutputStream out = null;

    Client (Nodes node) {
        this.node = node;
    }

    public void start () {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    // constructor to put ip address and port
    public void run() {
        String address = node.getHostName();
        int port = node.getPortNumber();
        System.out.println("Client connecting to Host: " + address + " Port: " + port);
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Client: Connected");

            // set up message
            Message msg = new Message();
            msg.buildMessage(address, port, null);
            System.out.println("Client: Message to be sent: " + msg);

            // sends output to the socket
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(msg);
            out.flush();
        } catch(UnknownHostException u) {
            u.printStackTrace();
        } catch(IOException i) {
            i.printStackTrace();
        }

        // string to read message from input
        Message msg_in = new Message();

        // read reply from server
        try {
            input = new ObjectInputStream(socket.getInputStream());
            msg_in = (Message) input.readObject();
            System.out.println("Client: Message received from server " + msg_in);
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        } catch(IOException i) {
            i.printStackTrace();
        }

        // close the connection
        try {
            input.close();
            out.close();
            socket.close();
            System.out.println("Client thread closed.");
        } catch(IOException i) {
            i.printStackTrace();
        }

        //return reply from server
    }
}