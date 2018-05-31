public interface Stack<E> {

    void push(E element);

    E pop() throws StackUnderflowException;

    E top() throws StackUnderflowException;

    boolean isEmpty();
}
