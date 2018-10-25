// Members: Helee Thumber (hat170030), Tanushri Singh (tts150030), Ko-Chen (Jack) Chen (kxc170002)
// Project 2

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;
import java.lang.Integer;

public class Main {
    public static void main(String[] args) {
        System.out.println();
        //parse config file
        String PATH = System.getProperty("user.dir");
        PATH = PATH + "/CS6378/Project-2/src/config_file.txt";
        Nodes[] array_of_nodes = Parser.parse(PATH);

        //start server with array_of_nodes[args[0]], args[0] is passed in through launcher.sh
        Server server = new Server(array_of_nodes, Integer.parseInt(args[0]));
    }
}