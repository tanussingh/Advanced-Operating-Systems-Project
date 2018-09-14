// Members: Helee Thumber (hat170030), Tanushri Singh (tts15030), Ko-Chen (Jack) Chen (kxc170002)

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        //parse config file
        Nodes[] array_of_nodes = Parser.parse("src/config_file.txt");

        //figure out which machine this is
        int source = -1;
        try {
            String thisHostName = InetAddress.getLocalHost().getHostName();
            for (int i = 0; i < array_of_nodes.length; i++) {
                if (Objects.equals(thisHostName, array_of_nodes[i].getHostName())){
                    source = i;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create server
        Server server = new Server(array_of_nodes[source]);
        server.start();


        //spawn threads for multiple client
        for (int dest = 0; dest < array_of_nodes[source].getNodalConnections().size(); dest++)
        {
            Client client = new Client(array_of_nodes[(int)(array_of_nodes[source].getNodalConnections().get(dest))]);
            client.start();
        }
    }
}