package Utility;

public class Node {
    public int data;
    public Utility.Node next;

    public Node(int data) {
        this.data = data;
    }

    @Override
    public boolean  equals(Object o) {
        Node otherNode = (Node) o;
        if(this.data == otherNode.data) {
            return true;
        } else {
            return false;
        }

    }
}
