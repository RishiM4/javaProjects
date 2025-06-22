public class App {
   
    public static void main(String[] args) {
        CustomLinkedList linked_list = new CustomLinkedList();

        linked_list.insert(0, "The");
        linked_list.insert(1, "mouse");
        linked_list.insert(1, "cat");
        linked_list.insert(2, "ate");
        linked_list.printList();
        linked_list.delete(0);
        linked_list.printList();

        /*
        .size()
        .append(Integer index, String data)
        .delete(Integer index)
        .get(Integer index)
        .printList()
        */
        
        
    }
}