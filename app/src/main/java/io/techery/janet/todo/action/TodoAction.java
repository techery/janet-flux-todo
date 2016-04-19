package io.techery.janet.todo.action;

import io.techery.janet.command.annotations.CommandAction;
import io.techery.janet.todo.model.ImmutableTodo;
import io.techery.janet.todo.model.Todo;

@CommandAction
public class TodoAction extends ValueCommandAction<Todo> {

    public enum Action {
        ADD, DELETE, CLEAR_COMPLETED, UNDO_DELETED
    }

    private final Action action;

    private TodoAction(Todo value, Action action) {
        super(value);
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public static TodoAction create(String text) {
        return new TodoAction(ImmutableTodo.builder()
                .id(System.currentTimeMillis())
                .text(text)
                .completed(false)
                .build(), Action.ADD);
    }

    public static TodoAction delete(Todo todo) {
        return new TodoAction(todo, Action.DELETE);
    }

    public static TodoAction clearCompleted() {
        return new TodoAction(null, Action.CLEAR_COMPLETED);
    }

    public static TodoAction undoDeleted() {
        return new TodoAction(null, Action.UNDO_DELETED);
    }

    public static TodoAction completeTodo(Todo todo, Boolean completed) {
        return new TodoAction(ImmutableTodo.copyOf(todo).withCompleted(completed), Action.ADD);
    }
}
