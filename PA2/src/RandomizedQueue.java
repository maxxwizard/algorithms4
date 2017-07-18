import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    // we'll use a resizing array to implement this data structure
    // when it gets full, double the array size
    // when it decreases to 1/4 capacity, cut the array size in half and compact it

    private static final int STARTING_ARRAY_SIZE = 2;

    private Item[] array;
    private int numItems;
    private int last;       // index of next available slot

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.array = (Item[]) new Object[STARTING_ARRAY_SIZE];
        this.numItems = 0;
        last = 0;
    }

    // is the queue empty?
    public boolean isEmpty() {
        return this.numItems == 0;
    }

    // return the number of items on the queue
    public int size() {
        return this.numItems;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }

        // insert the item into the next available index
        this.array[last] = item;
        this.numItems++;

        // resize the array if it is full
        if (this.numItems == this.array.length) {
            resize(2*this.array.length);
        }

        advanceLastToNextAvailable();
    }

    private void advanceLastToNextAvailable() {
        do {
            // move forward and wrap around if necessary
            last++;
            if (last == this.array.length) {
                last = 0;
            }
        } while (this.array[last] != null);
    }

    private void resize(int capacity) {
        // StdOut.println("resizing array to " + capacity);

        Item[] oldArray = this.array;
        Item[] newArray = (Item[]) new Object[capacity];

        // iterate the old array and copy over items if they exist
        for (int i = 0, j = 0; i < oldArray.length; i++) {
            if (oldArray[i] != null) {
                newArray[j++] = oldArray[i];
                last = j;
            }
        }

        // set the new array as the proper one
        this.array = newArray;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        // find a random item
        int randomIndex = findValidRandomIndex();
        Item item = this.array[randomIndex];
        this.array[randomIndex] = null;
        this.numItems--;

        // put the last cursor at the vacated index
        last = randomIndex;

        // if array is 1/4 full, cut it in half
        if (numItems < (this.array.length / 4)) {
            resize(this.array.length / 2);
        }

        return item;
    }

    private int findValidRandomIndex() {
        int randomIndex;
        do {
            // ensure item is at the random index
            randomIndex = StdRandom.uniform(this.array.length);
        } while (this.array[randomIndex] == null);
        return randomIndex;
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        // find a random item
        int randomIndex = findValidRandomIndex();

        return this.array[randomIndex];
    }

    // return an independent iterator over items in random order
    // iterator contains a copy of the array where the items are in random order
    // thus next() and hasNext() runs in constant time
    public Iterator<Item> iterator() {
        return new ArrayIterator(this.array, this.numItems);
    }

    private class ArrayIterator implements Iterator<Item> {
        private Item[] randomizedArray;
        private int currentIndex;

        public ArrayIterator(Item [] array, int numItems) {
            randomizedArray = (Item[]) new Object[numItems];
            // clone the array by creating a new one with only valid items
            for (int i = 0, j = 0; i < array.length; i++) {
                if (array[i] != null) {
                    randomizedArray[j++] = array[i];
                }
            }

            // shuffle the array
            StdRandom.shuffle(randomizedArray);

            // set our pointer to start
            currentIndex = 0;
        }

        public boolean hasNext() {
            return currentIndex < randomizedArray.length;
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }

            // return the current item and move pointer forward
            return randomizedArray[currentIndex++];
        }
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < this.array.length; i++) {
            s += (this.array[i] == null) ? "null " : this.array[i].toString() + " ";
        }
        return s;
    }

    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        /*
        rq.enqueue(1);
        StdOut.println(rq);
        rq.dequeue();
        StdOut.println(rq);
        */

        int num_items_test = 100;

        StdOut.println("enqueueing " + num_items_test);
        for (int i = 0; i < num_items_test; i++) {
            int uniform = StdRandom.uniform(10);
            if (uniform < num_items_test / 2) {
                rq.enqueue(i);
            } else {
                if (!rq.isEmpty())
                    rq.dequeue();
            }
        }

        StdOut.println("random iterations");
        for (int i = 0; i < num_items_test; i++) {
            rq.enqueue(i);
        }
        for (int i = 0; i < 2; i++) {
            for (Integer item : rq) {
                StdOut.print(item.toString() + " ");
            }
            StdOut.println();
        }
    }
}