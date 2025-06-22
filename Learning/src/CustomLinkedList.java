public class CustomLinkedList extends RuntimeException{
    private static Node startNode;

    private CustomLinkedList(String message, boolean t) {
        super(message);
    }
    CustomLinkedList(String data) {
        startNode = new Node(data);
    }
    CustomLinkedList() {

    }
    
    public Integer size() {
        int size = 0;
        Node currentNode = startNode;
        while (currentNode!=null) {
            currentNode = currentNode.nextNode;
            size++;
        }
        return size;
    }

    public Node get(Integer index) {
        if (index >= size()) {
            throw new CustomLinkedList("Index " + index + " out of bounds for length of " + size(),true);
        }
        Node currentNode = startNode;
        int k = 0;
        while (k != index && currentNode != null) {
            currentNode = currentNode.nextNode;
            k++;
        }
        return currentNode;
    }

    public void delete(Integer index) {
        if (index >= size()) {
            throw new CustomLinkedList("Index " + index + " out of bounds for length of " + size(),true);
        }
        if (index == 0) {
            startNode = startNode.nextNode;
            return;
        }
        Node currentNode = startNode;
        int k = 0;
        while (k != index-1 && currentNode != null) {
            currentNode = currentNode.nextNode;
            k++;
        }
        currentNode.nextNode = currentNode.nextNode.nextNode;
    }

    public void insert(Integer index, String data){
        if (index > size()) {
            throw new CustomLinkedList("Index " + index + " out of bounds for length of " + size(),true);
        }
        if (index == 0 && size() == 0) {
            startNode = new Node(data);
            return;
        }
        if (index == 0) {
            Node newNode = new Node(startNode.data,startNode.nextNode);
            startNode = new Node(data, newNode);
            return;
        }
        Node currentNode = startNode;
        int k = 0;
        while (k != index-1 && currentNode != null) {
            currentNode = currentNode.nextNode;
            k++;
        }
        if (index == size()) {
            Node newNode = new Node(data, null);
            currentNode.nextNode = newNode;
        }
        else {
            Node newNode = new Node(data, currentNode.nextNode);
            currentNode.nextNode = newNode;
        }
        
    }
    public void printList() {
        Node currentNode = startNode;
        while(currentNode != null) {
            System.err.println(currentNode.data);
            currentNode = currentNode.nextNode;
        }
        
    }
    public static void main(String[] args) {
        

    }
}
