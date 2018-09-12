// Members: Helee Thumber (hat170030), Tanushri Singh (tts15030), Ko-Chen (Jack) Chen (kxc170002)

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        //parse config file
        Nodes[] array_of_nodes = Parser.parse("src/config_file.txt");

        //figure out which machine this is
        int source = 0;
        try {
            String thisHostName = InetAddress.getLocalHost().getHostName();
            for (int i = 0; i < array_of_nodes.length; i++) {
                thisHostName = "cometnet-10-21-13-240.utdallas.edu";
                if (Objects.equals(thisHostName, array_of_nodes[i].getHostName())){
                    source = i;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Nodes testNode = new Nodes();
        String thisHostName = "172.0.0.1";
        testNode.setHostName(thisHostName);
        testNode.setNodeID(0);
        testNode.setPortNumber(8000);
        testNode.addNodalConnections(0);


        //create server
        //Server server = new Server(array_of_nodes[source]);

        Server server = new Server();
        System.out.println("hello");
        server.start();
        //System.out.println("ran here?");

        Client client = new Client(testNode);
        client.start();

        /*
        //spawn threads for multiple client
        for (int i = 0; i < array_of_nodes[i].getNodalConnections().size(); i++)
        {
            Client client = new Client(array_of_nodes[i]);
            client.start();
        }
        */
    }
}