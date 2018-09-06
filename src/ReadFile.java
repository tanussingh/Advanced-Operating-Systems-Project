// Members: Helee Thumber (hat170030), Tanushri Singh (tts15030), Ko-Chen (Jack) Chen (kxc170002)

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadFile {

    public static class Nodes {
        int nodeID;
        String hostName;
        int portNumber;
        ArrayList <Integer> nodalConnections = new ArrayList<Integer>();
    }

    public static void parser() {
        try {
            //create variables
            File file = new File("src/config_file.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            int numNode;
            boolean empty = false;
            int valid_lines = 0;

            //read in number of nodes
            numNode = Integer.parseInt(bufferedReader.readLine());
            //create array to hold pointers to nodes
            Nodes [] array_of_nodes = new Nodes[numNode];

            //node info
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        parser();
    }
}