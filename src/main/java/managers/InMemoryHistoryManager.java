package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_LIST_SIZE = 10;
    private List<Task> history;
    private Map<Integer, Node> historyMap;

    private Node head;
    private Node tail;
    private int size;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
        historyMap = new HashMap<>();

        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            Node removableNode = historyMap.get(id);
            Node previousNode = removableNode.prev;
            Node nextNode = removableNode.next;

            // удаление начала списка, если в мап больше одной записи
            if (previousNode == null && nextNode != null) {
                head = nextNode;
                nextNode.prev = null;
                // удаление хвоста, если в мап больше одной записи
            } else if (previousNode != null && nextNode == null) {
                tail = previousNode;
                previousNode.next = null;
                // удаление из середины списка, если в мап больше одной записи
            } else if (previousNode != null && nextNode != null) {
                previousNode.next = nextNode;
                nextNode.prev = previousNode;
                // удаление, если в мап одна запись
            } else if (previousNode == null && nextNode == null) {
                historyMap.clear();
                head = null;
                tail = null;
            }

            historyMap.remove(id);
            size--;
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();

        Node node = head;
        while (node != null) {
            historyList.add(node.data);
            node = node.next;
        }

        return historyList;
    }

    public void linkLast(Task task) {
        Node newNode;
        // если мапа пустая
        if (head == null) {
            newNode = new Node(task, null, null);
            head = newNode;
            tail = head;
        } else {
            // если уже содержит - заменить, размер тот же
            if (historyMap.containsKey(task.getId())) {
                remove(task.getId());
            }
            // при удалении может стать пустой
            if (head != null && tail != null) {
                Node oldTail = tail;
                Node newTail = new Node(task, null, oldTail);
                oldTail.next = newTail;
                tail = newTail;
            } else {
                newNode = new Node(task, null, null);
                head = newNode;
                tail = head;
            }
        }
        historyMap.put(task.getId(), tail);
        size++;
        if (size > MAX_LIST_SIZE) {
            remove(head.data.getId());
        }
    }

    public void removeNode(Node node) {
        remove(node.getData().getId());
    }


}
