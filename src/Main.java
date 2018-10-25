// Members: Helee Thumber (hat170030), Tanushri Singh (tts150030), Ko-Chen (Jack) Chen (kxc170002)
// Project 2

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        //parse config file
        String PATH = System.getProperty("user.dir");
        PATH = PATH + "/CS6378/Project-2/src/config_file.txt";
        Nodes[] array_of_nodes = Parser.parse(PATH);

        //figure out which machine this is
        int source = -1;
        try {
            String thisHostName = InetAddress.getLocalHost().getHostName();
            for (int i = 1; i < array_of_nodes.length; i++) {
                /*line for testing
                thisHostName = "dc01.utdallas.edu";*/
                //end testing line
                if (Objects.equals(thisHostName, array_of_nodes[i].getHostName())) {
                    //create server
                    Server server = new Server(array_of_nodes, i);
                    server.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}