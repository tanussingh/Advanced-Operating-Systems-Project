// Members: Helee Thumber (hat170030), Tanushri Singh (tts15030), Ko-Chen (Jack) Chen (kxc170002)

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Objects;

public class ReadFile {
    
    public static class Nodes {
        int nodeID;
        String hostName;
        int portNumber;
        ArrayList <Integer> nodalConnections = new ArrayList<Integer>();
    }

    public static Nodes[] parser() {
        //get number of nodes
        int numNode = 0;
        try {
            //open file
            File file = new File("src/config_file.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            numNode = Integer.parseInt(bufferedReader.readLine());
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //build the array of nodes
        Nodes[] array_of_nodes = new Nodes[numNode];

        try {
            //open file
            File file = new File("src/config_file.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            //StringBuffer stringBuffer = new StringBuffer();
            String line;
            //int numNode;
            boolean empty = false;
            int valid_lines = 0;

            //read in number of nodes
            //numNode = Integer.parseInt(bufferedReader.readLine());
            //create array to hold pointers to nodes
            //Nodes [] array_of_nodes = new Nodes[numNode];

            //node info
            line = bufferedReader.readLine();
            while (valid_lines != numNode) {
                line = bufferedReader.readLine();                
                empty = false;
                //format each line to correct format
                if (line.contains("#")) {
                    int index = line.indexOf("#");
                    line = line.substring(0, index);
                }
                if (line.isEmpty()) {
                    empty = true;
                }
                //create nodes
                if (!empty) {
                    //parse info
                    Nodes node = new Nodes();
                    node.nodeID = Integer.parseInt(line.substring(0, 1));
                    node.hostName = line.substring(2, 19);
                    node.portNumber = Integer.parseInt(line.substring(20, 24));
                    array_of_nodes[valid_lines] = node;
                    valid_lines++;
                }
            }
            //nodal connections
            valid_lines = 0;
            while (valid_lines != numNode) {
                line = bufferedReader.readLine();
                empty = false;
                //format each line to correct format
                if (line.contains("#")) {
                    int index = line.indexOf("#");
                    line = line.substring(0, index);
                }
                if (line.isEmpty()) {
                    empty = true;
                }
                //create array to store nodal connections
                if (!empty) {
                    int nodeId = Integer.parseInt(line.substring(0, 1));
                    line = line.substring(2);
                    while (line != null) {
                        //parse info
                        int next;
                        if (!(line.indexOf(" ") == -1)) {
                            next = line.indexOf(" ");
                            array_of_nodes[nodeId].nodalConnections.add(Integer.parseInt(line.substring(0, next)));
                            line = line.substring(next+1);
                        } else {
                            next = 1;
                            array_of_nodes[nodeId].nodalConnections.add(Integer.parseInt(line.substring(0, next)));
                            line = null;
                        }
                    }
                    valid_lines++;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return array_of_nodes;
    }

    public static void server(Nodes[] array_of_nodes) {
        //figure out which machine this is
        int numNode = array_of_nodes.length;
        int thisServer = 0;
        try {
            String thisHostName = InetAddress.getLocalHost().getHostName();
            for (int i = 0; i < numNode; i++) {
                thisHostName = "dc02.utdallas.edu";
                if (Objects.equals(thisHostName, array_of_nodes[i].hostName)){
                    thisServer = i;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void client(Nodes[] array_of_nodes) {
        //figure out which machine this is
        int numNode = array_of_nodes.length;
        int thisServer = 0;
        try {
            String thisHostName = InetAddress.getLocalHost().getHostName();
            for (int i = 0; i < numNode; i++) {
                thisHostName = "dc02.utdallas.edu";
                if (Objects.equals(thisHostName, array_of_nodes[i].hostName)){
                    thisServer = i;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        //parse config file
        Nodes[] array_of_nodes = parser();
        //create server
        server(array_of_nodes);
        //create client
        client(array_of_nodes);
    }
}