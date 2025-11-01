package src;

public class Node<T>{
    T data;
    Node<T> nextNode;
    Node(T d,Node<T> n) {
        data = d;
        nextNode = n;
    }
    Node(T d) {
        data = d;
        nextNode = null;
    }
    
    
}