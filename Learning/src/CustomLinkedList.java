public class CustomLinkedList<T> {
    private Node<T> startNode;

   
    CustomLinkedList(T data) {
        startNode = new Node<T>(data);
    }
    CustomLinkedList() {
    }
    
    public Integer size() {
        int size = 0;
        Node<T> currentNode = startNode;
        while (currentNode!=null) {
            currentNode = currentNode.nextNode;
            size++;
        }
        return size;
    }

    public Node<T> get(Integer index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException(index);
        }
        Node<T> currentNode = startNode;
        int k = 0;
        while (k != index && currentNode != null) {
            currentNode = currentNode.nextNode;
            k++;
        }
        return currentNode;
    }

    public void delete(Integer index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException(index);
        }
        if (index == 0) {
            startNode = startNode.nextNode;
            return;
        }
        Node<T> currentNode = startNode;
        int k = 0;
        while (k != index-1 && currentNode != null) {
            currentNode = currentNode.nextNode;
            k++;
        }
        currentNode.nextNode = currentNode.nextNode.nextNode;
    }

    public void insert(Integer index, T data){
        if (index > size()) {
            throw new IndexOutOfBoundsException(index);
        }
        if (index == 0 && size() == 0) {
            startNode = new Node<T>(data);
            return;
        }
        if (index == 0) {
            Node<T> newNode = new Node<T>(startNode.data,startNode.nextNode);
            startNode = new Node<T>(data, newNode);
            return;
        }
        Node<T> currentNode = startNode;
        int k = 0;
        while (k != index-1 && currentNode != null) {
            currentNode = currentNode.nextNode;
            k++;
        }
        if (index == size()) {
            Node<T> newNode = new Node<T>(data, null);
            currentNode.nextNode = newNode;
        }
        else {
            Node<T> newNode = new Node<T>(data, currentNode.nextNode);
            currentNode.nextNode = newNode;
        }
        
    }
    public void printList() {
        Node<T> currentNode = startNode;
        while(currentNode != null) {
            System.err.println(currentNode.data);
            currentNode = currentNode.nextNode;
        }
        
    }
    public static void main(String[] args) {
        

    }
}
