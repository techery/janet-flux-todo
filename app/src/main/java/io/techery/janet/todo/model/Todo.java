package io.techery.janet.todo.model;

import org.immutables.value.Value;

@Value.Immutable
public abstract class Todo implements Comparable<Todo> {

    public abstract Long id();

    public abstract boolean completed();

    public abstract String text();

    @Override
    public int compareTo(Todo todo) {
        if (id() == todo.id()) {
            return 0;
        } else if (id() < todo.id()) {
            return -1;
        } else {
            return 1;
        }
    }
}
