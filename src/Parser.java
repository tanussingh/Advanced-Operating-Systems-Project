import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    public static Nodes[] parse (String PATH) {
        //open file
        try {
            File file = new File(PATH);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get number of nodes
        int numNode = 0;
        try {
            //open file
            File file = new File(PATH);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            int index;
            boolean empty;
            int valid_lines = 0;

            while (valid_lines != 1) {
                empty = false;
                if (line.contains("#")) {
                    index = line.indexOf("#");
                    line = line.substring(0, index);
                }
                if (line.isEmpty()) {
                    empty = true;
                }
                if (!empty) {
                    numNode = Integer.parseInt(bufferedReader.readLine());
                    bufferedReader.close();
                }
            }
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
            boolean empty;
            int valid_lines = 0;

            //node info
            line = bufferedReader.readLine();
            while (valid_lines != numNode) {
                line = bufferedReader.readLine();
                empty = false;
                int index;
                //format each line to correct format
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
                    node.setNodalConnections(numNode);
                    node.setNodeID(valid_lines + 1);
                    index = line.indexOf(" ");
                    node.setHostName(line.substring(0, index) + "utdallas.edu");
                    line = line.substring(index).trim();
                    node.setPortNumber(Integer.parseInt(line.substring(0, 4)));
                    line = line.substring(index).trim();
                    array_of_nodes[valid_lines] = node;
                    while (line != null) {
                        //parse info
                        int next;
                        if (line.contains(" ")) {
                            next = line.indexOf(" ");
                            ArrayList<Integer> path = new ArrayList<>();
                            path.add(valid_lines + 1);
                            path.add(Integer.parseInt(line.substring(0, next)));
                            array_of_nodes[valid_lines + 1].addNodalConnections(path, Integer.parseInt(line.substring(0, next)));
                            line = line.substring(next+1);
                        } else {
                            next = 1;
                            ArrayList<Integer> path = new ArrayList<>();
                            path.add(valid_lines + 1);
                            path.add(Integer.parseInt(line.substring(0, next)));
                            array_of_nodes[valid_lines + 1].addNodalConnections(path, Integer.parseInt(line.substring(0, next)));
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
