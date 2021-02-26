public class Node {
    private int node;
    private ArrayList<Integer> outConnections;

    public Node(int newNode){
        node = newNode;
        outConnections = new ArrayList<Integer>();
    }

    public void addNextNode(int nextNode){
        outConnections.add(nextNode);
    }
}
