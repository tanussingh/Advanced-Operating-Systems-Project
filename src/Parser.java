import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    public static Nodes[] parse (String PATH) {
        //get number of nodes
        int numNode = 0;
        try {
            //open file
            File file = new File(PATH);
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
            File file = new File(PATH);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            boolean empty = false;
            int valid_lines = 0;

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
                    node.setNodeID(Integer.parseInt(line.substring(0, 1)));
                    node.setHostName(line.substring(2, 19));
                    node.setPortNumber(Integer.parseInt(line.substring(20, 24)));
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
                            array_of_nodes[nodeId].addNodalConnections(Integer.parseInt(line.substring(0, next)));
                            line = line.substring(next+1);
                        } else {
                            next = 1;
                            array_of_nodes[nodeId].addNodalConnections(Integer.parseInt(line.substring(0, next)));
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
}
