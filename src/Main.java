// Members: Helee Thumber (hat170030), Tanushri Singh (tts150030), Ko-Chen (Jack) Chen (kxc170002)

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        //parse config file
        String PATH = System.getProperty("user.dir");
        PATH = PATH + "/CS6378/Project-1/src/config_file.txt";
        System.out.println(PATH);
        Nodes[] array_of_nodes = Parser.parse(PATH);

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
        Server server = new Server(array_of_nodes, source);
        server.start();

        //spawn threads for multiple client
        for (int hops = 1; hops < array_of_nodes.length; hops++) {
            for (int dest = 0; dest < array_of_nodes.length; dest++)
            {
                if (array_of_nodes[source].getNodalConnections(dest).size() == hops) {
                    Client client = new Client(array_of_nodes[source], array_of_nodes[dest], dest);
                    client.start();
                }
            }
        }

        while (Thread.activeCount() > 2) {
            //System.out.println(Thread.activeCount());
        }

        //Output Final Results
        System.out.println("Final Results: ");
    }
}