public class NodalCommunication {
    public static void Communicate (Nodes[] array_of_nodes) {
        //Declare Variables
        int counter = 0;
        int totalNodes = array_of_nodes.length;
        int [] connections = new int[totalNodes];

        //Quickest Method
        for (int i = 0; i < array_of_nodes[i].getNodalConnections().size(); i++){
            //Message 1 hop nodes from each source


            //If more nodes remain that need to be reached, message them through child node


            //If all nodes have been discovered for source node, move on


        }

        int connectedNode = 0;
        //Scalable Method
       while (connectedNode <= totalNodes){
           // Each source must hold records of all other nodes, when a child process specifies sub children
           // then add to record if not already known

           for (int i = 0; i < array_of_nodes[i].getNodalConnections().size(); i++){

               //If all nodes are not known to the source
               if (array_of_nodes[i].getNodalConnections().size() < totalNodes -1){
                   //Message children to find not known node
               }


           }
       }
    }
}
