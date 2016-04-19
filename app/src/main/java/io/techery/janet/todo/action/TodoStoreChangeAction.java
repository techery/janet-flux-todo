package io.techery.janet.todo.action;

import java.util.List;

import io.techery.janet.command.annotations.CommandAction;
import io.techery.janet.todo.model.Todo;

@CommandAction
public class TodoStoreChangeAction extends ValueCommandAction<List<Todo>> {

    public TodoStoreChangeAction(List<Todo> value) {
        super(value);
    }
}
