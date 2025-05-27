public class BucketList { // Lista encadeada pra tratar colisões
    private Node head;
    private int size;

    public BucketList() {
        this.head = null;
        this.size = 0;
    }

    public void add(String element) {
        Node newNode = new Node(element);
        if (head == null) {
            head = newNode;
        } else {
            Node curr = head;
            while (curr.next != null) {
                curr = curr.next;
            }
            curr.next = newNode;
        }
        size++;
    }


    public boolean contains(String element) {
        Node curr = head;
        while (curr != null) {
            if (curr.value.equals(element)) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    public boolean Removes(String element) {
        Node curr = head;
        Node previous = null;
        while (curr != null) {
            if (curr.value.equals(element)) {
                if(previous != null){
                    previous.next = curr.next;
                }
                else {
                    head = curr.next;
                }
                return true;
            }
            previous = curr;
            curr = curr.next;
        }
        return false;
    }


    public int size() {
        return size;
    }
}


