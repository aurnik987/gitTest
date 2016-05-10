import java.util.Iterator;

/**
 * Snaek Linked List implementation of the SinglyLinkedList interface
 *
 * @author Shashank Singh
 * @author Nikhil Auradkar
 * @author Severus Snaek
 * @version 1.0
 */
public class SnaekLinkedList implements SinglyLinkedList {

    // DO NOT MODIFY THESE VARIABLE NAMES
    private SnaekNode head;
    private SnaekNode tail;
    private int size;

    //constructor
    public SnaekLinkedList() {
        this.size = 0;
    }

    //addFront and addEnd
    public void addFront(Position newPos) {
        if(newPos == null) {
            return;
        }

        if(newPos != null) {
            if(size == 0) {
                head = new SnaekNode(newPos);
                tail = new SnaekNode(newPos);
            } else {
                head = new SnaekNode(newPos, head);
            }
        }

        size++;
    }
    public void addEnd(Position newPos) {
        /*if(newPos == null) {
            return;
        }

        if(newPos != null) {
            if(size == 0) {
                head = new SnaekNode(newPos);
                tail = new SnaekNode(newPos);
            } else {
                SnaekNode newTail = new SnaekNode(newPos);
                tail.setNext(newTail);
                tail = newTail;
            }
        }

        size++;*/
        if(newPos.equals(null)) {
            return;
        } else if (size > 0) {
            SnaekNode tmp = tail;
            tail = new SnaekNode(newPos);
            tmp.setNext(tail);
            size++;
        } else {
            SnaekNode s = new SnaekNode(newPos);
            head = s;
            tail = s;
            size++;
        }

    }

    //removeFront and removeEnd
    public Position removeFront() {
        if(size == 0) {
            return null;
        }
        Position tempHead = head.getPosition();
        head = head.getNext();

        size--;
        return tempHead;
    }
    public Position removeEnd() {
        if(size == 0) {
            return null;
        }
        Position tempTail = tail.getPosition();

        //just the head
        if(head.getNext() == null) {
            removeFront();
            return tempTail;
        }
        
        SnaekNode tmpNode = head;
        while(tmpNode.getNext().getNext() != null) {
            tmpNode = tmpNode.getNext();
        }

        tail = tmpNode;
        size--;
        return tempTail;

    }

    //getFront and getEnd
    public Position getFront() {
        return head.getPosition();
    }
    public Position getEnd() {
        return tail.getPosition();
    }

    //miscellaneous
    public boolean contains(Position other) {
        SnaekNode tmpNode = head;

        while(tmpNode.getNext() != null) {
            Position nodePos = tmpNode.getPosition();
            if (other.getX() == nodePos.getX()) {
                if (other.getY() == nodePos.getY()) {
                    return true;
                }
            }

            tmpNode = tmpNode.getNext();
        }
        return false;
    }

    public void clear() {
        head = null;
        tail = null;

        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        if(size == 0) {
            return true;
        }
        return false;
    }

    public SnaekNode getHead() {
        return head;
    }


    @Override
    public Iterator<Position> iterator() {
        return new Iterator<Position>() {
            private SnaekNode iter = head;

            @Override
            public boolean hasNext() {
                return iter != null;
            }

            @Override
            public Position next() {
                Position p = iter.getPosition();
                iter = iter.getNext();
                return p;
            }
        };
    }
}
