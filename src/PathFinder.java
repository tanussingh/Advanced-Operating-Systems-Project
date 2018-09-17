import java.util.ArrayList;

//given source node and dest node find path connecting the two
public class PathFinder {
        public static ArrayList<Integer> find(Nodes source, Nodes dest, int hops) {
            ArrayList<Integer> path = new ArrayList<Integer>();

            //build a tree from source.nodalconnection
            TreeNode root = new TreeNode();
            root.setCurrent(source.getNodeID());
            root.setChildren(source.getNodalConnections(hops));

            //dst find find path
            //return path
            return path;
        }
}
