package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {


    public static class Node {
        public Node prev;
        public Node next;
        public Task task;

        public Node(Node prev, Node next, Task task) {
            this.prev = prev;
            this.next = next;
            this.task = task;
        }
    }

    private Node first;
    private Node last;


    public List<Task> getHistory() { // getTasks
        ArrayList<Task> list = new ArrayList<>();
        Node current = first;
        while (current != null) {
            list.add(current.task);
            current = current.next;
        }
        return list;
    }

    public void linkLast(Task task) {
        Node node = new Node(last, null, task);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
    }


    private final Map<Integer, Node> nodes = new HashMap<>();

    public void removeNode(Node nodeRemoved) {
        if (nodeRemoved != null) {
            if (nodeRemoved.prev == null) {
                first = nodeRemoved.next;
                if (first != null) {
                    first.prev = null;
                }
            } else {
                nodeRemoved.prev.next = nodeRemoved.next;
                if (nodeRemoved.next != null) {
                    nodeRemoved.next.prev = nodeRemoved.prev;
                } else {
                    last = nodeRemoved.prev;
                    last.next = null;
                }
            }
        }
    }


    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            linkLast(task);
            nodes.put(task.getId(), last);
        }
    }


    public List<Task> getTasks() {
        return getHistory();
    }

    @Override
    public void remove(int id) {
        Node nodeRemoved = nodes.remove(id);
        removeNode(nodeRemoved);
    }
}
