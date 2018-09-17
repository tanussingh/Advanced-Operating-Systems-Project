import java.util.ArrayList;

public class TreeNode {
    private int current;
    private ArrayList<Integer> children = new ArrayList<>();

    public void setCurrent(int i) {
        this.current = i;
    }

    public void setChildren(ArrayList<Integer> i) {
        this.children = i;
    }

    public int getCurrent () {
        return this.current;
    }

    public ArrayList<Integer> getChildren () {
        return this.children;
    }
}
