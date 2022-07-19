package managers;

import tasks.Task;

import java.util.Objects;

public class Node {
    public Task data;
    public Node next;
    public Node prev;

    public Node(Task data, Node next, Node prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public Task getData() {
        return data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, next, prev);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return Objects.equals(data, node.data);
    }
}
