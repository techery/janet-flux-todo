package io.techery.janet.todo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

import io.techery.janet.WriteActionPipe;
import io.techery.janet.todo.action.TodoAction;
import io.techery.janet.todo.model.ImmutableTodo;
import io.techery.janet.todo.model.Todo;
import io.techery.janet.todo.store.TodoStore;
import io.techery.janet.todo.view.MainView;

public class TodoActivity extends RxAppCompatActivity {

    private MainView mainView;
    private WriteActionPipe<TodoAction> todoActionPipe;
    private TodoStore todoStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mainView = new MainView(this));

        App app = App.get(this);
        todoActionPipe = app.todoActionPipe();

        todoStore = app.getTodoStore();
        todoStore.observeChages()
                .startWith(todoStore.getState())
                .compose(bindToLifecycle())
                .subscribe(this::updateItems);

        mainView.onButtonAddClick(this::addTodo);
        mainView.onButtonClearClick(this::clearCompleted);
        mainView.onButtonDeleteClick(this::deleteTodo);
        mainView.onCheckTodo(this::completeTodo);
    }

    private void updateItems(List<Todo> items) {
        mainView.updateItems(items);
        if (todoStore.canUndo()) {
            Snackbar snackbar = Snackbar.make(mainView, "Element deleted", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", view -> {
                todoActionPipe.send(TodoAction.undoDeleted());
            });
            snackbar.show();
        }
    }

    private void addTodo() {
        Editable editable = mainView.getInputText();
        todoActionPipe.send(TodoAction.create(editable.toString()));
        editable.clear();
    }

    private void clearCompleted() {
        todoActionPipe.send(TodoAction.clearCompleted());
    }

    private void deleteTodo(Todo todo) {
        todoActionPipe.send(TodoAction.delete(todo));
    }

    private void completeTodo(Todo todo, Boolean completed) {
        todoActionPipe.send(TodoAction.add(ImmutableTodo.copyOf(todo).withCompleted(completed)));
    }


}
