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
                if (array_of_nodes[source].getNodalConnections(dest).size() == (hops + 1)) {
                    Client client = new Client(array_of_nodes[source], array_of_nodes[dest], dest);
                    client.start();
                }
            }
            try {
                Thread.sleep(7000);
            } catch (InterruptedException x) {
                x.printStackTrace();
            }
            System.out.println("---------TESTTTT=----------");
            for (int i = 0; i < array_of_nodes[source].getNodalConnectionsLength(); i++) {
                System.out.println(array_of_nodes[source].getNodalConnections(i));
            }

        }

        while (Thread.activeCount() > 2) {
            if (Thread.activeCount() == 2) {//waits till == 2 which means only main and server
                break;
            }
        }

        //Output Final Results
        System.out.println("Final Results: ");
        System.out.println("Source Node: " + source);
        for (int i = 1; i < array_of_nodes[source].getNodalConnectionsLength(); i++) { //loop through to find what server for each hop i = hop number
            System.out.print("Hops: " + i + " - ");
            for (int j = 0; j < array_of_nodes[source].getNodalConnectionsLength(); j++) { //loop through to print server for this hop j = server
                if (array_of_nodes[source].getNodalConnections(j).size() == i + 1) {
                    System.out.print(j + " ");
                }
            }
            System.out.println();
        }
    }
}