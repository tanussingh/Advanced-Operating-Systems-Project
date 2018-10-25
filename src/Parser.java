import java.io.*;

public class Parser {
    public static Nodes[] parse (String PATH) {
        File file = new File(PATH);
        BufferedReader bufferedReader = null;
        int numNode = 0;
        boolean found = false;
        String line;
        int index;
        boolean empty;

        //read numNode
        try {
            //open file
            FileReader fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            
            //get numNodes
            while (!found) {
                line = bufferedReader.readLine();
                empty = false;
                //format line to just have what we want
                if (line.contains("#")) {
                    index = line.indexOf("#");
                    line = line.substring(0, index);
                }
                if (line.isEmpty()) {
                    empty = true;
                }
                line = line.trim();
                //create nodes
                if (!empty) {
                    //parse info
                    numNode = Integer.parseInt(line);
                    found = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //populate node information
        Nodes[] array_of_nodes = new Nodes[numNode + 1];
        try {
            int valid_lines = 1;
            while (valid_lines != numNode + 1) {
                line = bufferedReader.readLine();
                empty = false;
                //format line to just have what we want
                if (line.contains("#")) {
                    index = line.indexOf("#");
                    line = line.substring(0, index);
                }
                if (line.isEmpty()) {
                    empty = true;
                }
                line = line.trim();
                //create nodes
                if (!empty) {
                    //parse info
                    Nodes node = new Nodes();
                    index = line.indexOf(" ");
                    node.setHostName(line.substring(0, index) + ".utdallas.edu");
                    line = line.substring(index).trim();
                    index = line.indexOf(" ");
                    node.setPortNumber(Integer.parseInt(line.substring(0, index)));
                    line = line.substring(index).trim();
                    while (line != null) {
                        if (line.contains(" ")) {
                            index = line.indexOf(" ");
                            node.addNodalConnections(Integer.parseInt(line.substring(0, index)));
                            line = line.substring(index + 1);
                        } else {
                            node.addNodalConnections(Integer.parseInt(line));
                            line = null;
                        }
                    }
                    array_of_nodes[valid_lines] = node;
                    valid_lines++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array_of_nodes;
    }
}