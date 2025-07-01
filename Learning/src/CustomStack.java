import java.util.EmptyStackException;
import java.util.Stack;

public class CustomStack<T> {

    private Node<T> startNode = new Node<T>(null, null);
    
    public T push(T data) {
        Node<T> temp = new Node<T>(data,startNode.nextNode);
        startNode.nextNode = temp;
        return data;
    }
    public T pop() {
        if (startNode.nextNode == null) {
            throw new EmptyStackException();  
        }
        else {
            T i = startNode.nextNode.data;
            startNode.nextNode = startNode.nextNode.nextNode;
            return i;
        }
        
        
    }
    public T peek() {
        if (startNode.nextNode == null) {
            throw new EmptyStackException();  
        }
        else {
            return startNode.nextNode.data;
        }
    }
    public boolean empty() {
        if (startNode.nextNode == null) {
            return true;
        }
        else {
            return false;
        }
    }
    public int search(Object o) {
        if (startNode.nextNode == null) {
            return -1;
        }
        Node<T> currNode = startNode.nextNode;
        int i = 1;
        while (currNode != null) {
            if (currNode.data.equals(o)) {
                return i;
            }
            currNode = currNode.nextNode;
            i++;
        }
        return -1;
    }   
    void printStack() {
        Node<T> temp = startNode;
        while (temp.nextNode != null) {
            temp = temp.nextNode;
            System.out.println(temp.data);
        }
    }
    public static void main(String[] args) {
        Stack<Integer> t = new Stack<>();
        t.pop();
    }
}
