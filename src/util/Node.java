package util;

public class Node<T> {
    public T task;
    public Node<T> prev;
    public Node<T> next;

    public Node(Node<T> prev, Node<T> next, T task) {
        this.prev = prev;
        this.next = next;
        this.task = task;
    }
}