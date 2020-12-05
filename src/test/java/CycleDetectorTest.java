import Utility.CycleDetector;
import Utility.Node;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CycleDetectorTest {

    @Test
    public void FloydCycleDetectorTest() {
        int[] randomInts = new int[]{1,2,3,1,2,3};
        List<Node> nodes = new ArrayList<>();

        Node head = new Node(randomInts[0]);
        Node node = head;

        for(int i = 1; i <randomInts.length; i ++) {
            Node newNode = new Node(randomInts[i]);
            if(nodes.contains(newNode)) {
                int index = nodes.indexOf(newNode);
                node.next = nodes.get(index);
            }else {
                node.next = newNode;
                nodes.add(newNode);
            }
            node = node.next;
        }

        CycleDetector detector = new CycleDetector();
        detector.detect(head);

        boolean hasCycle = detector.hasCycle();
        int length = detector.length();
        int position = detector.position();

    }

    @Test
    public void cycleDetection() {
        Node head = createList(3);
        Node node = head;
        while (node.next != null) {
            node = node.next;
        }
        node.next = head;

        CycleDetector detector = new CycleDetector();
        detector.detect(head);

        boolean hasCycle = detector.hasCycle();
        int length = detector.length();
        int position = detector.position();
    }

    private Node createList(int n) {
        Node head = new Node(1);
        Node node = head;
        for (int i = 2; i <= n; ++i) {
            node.next = new Node(i);
            node = node.next;
        }
        return head;
    }
}
