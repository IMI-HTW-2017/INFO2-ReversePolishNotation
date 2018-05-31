public class LinkedListStack<E> implements Stack<E> {

    private Node top;

    @Override
    public void push(E element) {
        if (isEmpty()) {
            top = new Node(element, null);
        } else {
            top = new Node(element, top);
        }
    }

    @Override
    public E pop() throws StackUnderflowException {
        if (isEmpty())
            throw new StackUnderflowException();

        E data = top.data;
        top = top.next;
        return data;
    }

    @Override
    public E top() throws StackUnderflowException {
        if (isEmpty())
            throw new StackUnderflowException();

        return top.data;
    }

    @Override
    public boolean isEmpty() {
        return top == null;
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "[ ]";

        StringBuilder sb = new StringBuilder("[ ");
        sb.append(top.data);
        Node node = top.next;

        while (node != null) {
            sb.append(", ").append(node.data);
            node = node.next;
        }

        return sb.append(" ]").toString();
    }

    private class Node {
        E data;
        Node next;

        Node(E data, Node next) {
            this.data = data;
            this.next = next;
        }
    }
}
