import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

/*
 * Linked list implementation of a double-ended queue
 */
public class Deque<Item> implements Iterable<Item> {

    private Node head;
    private Node tail;
    private int numOfItems;

    private class Node {
        private Item item;
        private Node previous;
        private Node next;

        public Node(Item i) {
            setItem(i);
            setPrevious(null);
            setNext(null);
        }

        Item getItem() {
            return item;
        }

        void setItem(Item item) {
            this.item = item;
        }

        Node getPrevious() {
            return previous;
        }

        void setPrevious(Node previous) {
            this.previous = previous;
        }

        Node getNext() {
            return next;
        }

        void setNext(Node next) {
            this.next = next;
        }
    }

    // construct an empty deque
    public Deque() {
        head = null;
        tail = null;
        numOfItems = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (head == null && tail == null);
    }

    // return the number of items on the deque
    public int size() {
        return numOfItems;
    }

    // add the item to the front
    public void addFirst(Item item) {

        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }

        if (head == null) {
            // first item in our deque
            tail = new Node(item);
            head = tail;
        } else {
            // second or higher item in our deque
            Node newHead = new Node(item);
            newHead.setNext(head);
            head.setPrevious(newHead);
            head = newHead;
        }

        numOfItems++;
    }

    // add the item to the end
    public void addLast(Item item) {

        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }

        if (tail == null) {
            // first item in our deque
            head = new Node(item);
            tail = head;
        } else if (tail.getPrevious() == null) {
            // second item in our deque
            Node newTail = new Node(item);
            newTail.setPrevious(head);
            head.setNext(newTail);
            tail = newTail;
        } else {
            // third or higher item in our deque
            Node newTail = new Node(item);
            newTail.setPrevious(tail);
            tail.setNext(newTail);
            tail = newTail;
        }

        numOfItems++;

    }

    // remove and return the item from the front
    public Item removeFirst() {
        // 0-item scenario
        if (head == null) {
            // empty queue
            throw new java.util.NoSuchElementException();
        }

        Item removedHead = head.getItem();

        // 1-item scenario
        if (head == tail) {
            head = tail = null;
        } else if (head.getNext() == tail) {
            // 2-item scenario
            tail.setPrevious(null);
            head = tail;
        } else {
            // 3-item scenario
            Node newHead = head.getNext();
            newHead.setPrevious(null);
            head = newHead;
        }

        numOfItems--;

        return removedHead;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (head == null) {
            // empty queue
            throw new java.util.NoSuchElementException();
        }

        Item removedTail = tail.getItem();

        // 1-item scenario
        if (head == tail) {
            head = tail = null;
        } else if (tail.getPrevious() == head) {
            // 2-item scenario
            head.setNext(null);
            tail =  head;
        } else {
            // 3-item scenario
            Node newTail = tail.getPrevious();
            newTail.setNext(null);
            tail = newTail;
        }

        numOfItems--;

        return removedTail;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = head;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null) {
                throw new java.util.NoSuchElementException();
            }
            Item item = current.getItem();
            current = current.getNext();
            return item;
        }
    }

    private void print() {
        for (Item item : this) {
            StdOut.print(item.toString() + " ");
        }
        StdOut.println();
    }

    // unit testing (optional)
    public static void main(String[] args) {

        int testLimit = 10;

        Deque<String> deque = new Deque<>();
        for (int i = 0; i < testLimit; i++) {
            deque.addFirst(Integer.toString(i));
            deque.print();
        }
        for (int i = 0; i < testLimit; i++) {
            deque.removeFirst();
            deque.print();
        }

        StdOut.println("--------------\n");

        Deque<String> deque2 = new Deque<>();
        for (int i = 0; i < testLimit; i++) {
            deque2.addLast(Integer.toString(i));
            deque2.print();
        }
        for (int i = 0; i < testLimit; i++) {
            deque2.removeLast();
            deque2.print();
        }

    }
}