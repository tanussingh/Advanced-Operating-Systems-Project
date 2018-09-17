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
                thisHostName = "dc02.utdallas.edu";
                if (Objects.equals(thisHostName, array_of_nodes[i].getHostName())){
                    source = i;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create server
        Server server = new Server(array_of_nodes[1]);
        server.start();

        //spawn threads for multiple client
        for (int hops = 1; hops < array_of_nodes.length; hops++) {
            for (int dest = 0; dest < array_of_nodes[source].getNodalConnections(hops).size(); dest++)
            {
                Client client = new Client(array_of_nodes[source], array_of_nodes[(int)(array_of_nodes[source].getNodalConnections(hops).get(dest))], hops);
                //Client client = new Client(array_of_nodes[(int)(array_of_nodes[source].getNodalConnections(hops).get(dest))]);
                client.start();
            }
        }

        while (Thread.activeCount() > 2) {
            //System.out.println(Thread.activeCount());
        }
        System.out.println("YAY");
    }
}