import java.io.*;
import java.net.*;

public class Client extends Thread {
    // initialize socket and input output streams
    private Socket socket            = null;
    private DataInputStream input   = null;
    private DataOutputStream out     = null;

    // constructor to put ip address and port
    public Client(Nodes node) {
        try {
            // Displaying the thread that is running
            System.out.println ("Thread such " +
                    Thread.currentThread().getId() +
                    " is running");

        }
        catch (Exception e) {
            // Throwing an exception
            System.out.println ("Exception is caught");
        }
        String address = node.getHostName();
        int port = node.getPortNumber();
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal
            input  = new DataInputStream(System.in);

            // sends output to the socket
            out    = new DataOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException u) {
            System.out.println(u);
        }
        catch(IOException i) {
            System.out.println(i);
        }

        // string to read message from input
        String line = "";

        // keep reading until "Over" is input
        while (!line.equals("Over")) {
            try {
                line = input.readLine();
                out.writeUTF(line);
            }
            catch(IOException i) {
                System.out.println(i);
            }
        }

        // close the connection
        try {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i) {
            System.out.println(i);
        }
    }
}