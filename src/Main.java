// Members: Helee Thumber (hat170030), Tanushri Singh (tts15030), Ko-Chen (Jack) Chen (kxc170002)

public class Main {
    public static void main(String[] args) {
        //parse config file
        Nodes[] array_of_nodes = Parser.parse("src/config_file.txt");

        for (int i = 0; i < array_of_nodes.length; i++) {
            System.out.println(array_of_nodes[i].getNodeID());
            System.out.println(array_of_nodes[i].getHostName());
            System.out.println(array_of_nodes[i].getPortNumber());
            System.out.println(array_of_nodes[i].getNodalConnections());
        }
        //create server
        //Server you = new Server(array_of_nodes);
        //create client
        //client(array_of_nodes);
    }
}