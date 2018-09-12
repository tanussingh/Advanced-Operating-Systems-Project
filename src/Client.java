import java.io.*;
import java.net.*;

public class Client extends Thread {
    //set up the variables
    private Nodes node;
    private Thread t = null;

    // initialize socket and input output streams
    private Socket socket            = null;
    private DataInputStream input   = null;
    private DataOutputStream out     = null;

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
    public void run()
    {
        String address = node.getHostName();
        int port = node.getPortNumber();
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal
            input  = new DataInputStream(System.in);

            // sends output to the socket
            out    = new DataOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException u)
        {
            u.printStackTrace();
        }
        catch(IOException i)
        {
            i.printStackTrace();
        }

        // string to read message from input
        String line = "";

        // keep reading until "Over" is input
        while (!line.equals("Over"))
        {
            try
            {
                line = input.readLine();
                out.writeUTF(line);
            }
            catch(IOException i)
            {
                i.printStackTrace();
            }
        }

        // close the connection
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            i.printStackTrace();
        }
    }
}