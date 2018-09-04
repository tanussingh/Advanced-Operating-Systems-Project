// Members: Helee Thumber (hat170030), Tanushri Singh (tts15030), Ko-Chen (Jack) Chen (kxc170002)

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {

    public static class nodes {
        int nodeID;
        int portNumber;
        String hostName;
        int [] nodalConnections;
    }

    public static void main(String[] args) {
        //Declare Variables

        try {
            //create variables
            File file = new File("C:\\Users\\jack1\\IdeaProjects\\untitled\\src\\config_file.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            int numNode;

            //read in number of nodes
            numNode = Integer.parseInt(bufferedReader.readLine());

            //read in node info
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (line.contains("#")) {
                        int index = line.indexOf("#");
                        line = line.substring(0, index);
                    }
                    line = line.trim();
                    System.out.println(line);

                }
            }
            //fileReader.close();
            //System.out.println("Contents of file:");
            //System.out.println(stringBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}