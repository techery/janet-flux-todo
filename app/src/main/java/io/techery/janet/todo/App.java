package io.techery.janet.todo;

import android.app.Application;
import android.content.Context;

import io.techery.janet.ActionPipe;
import io.techery.janet.CommandActionService;
import io.techery.janet.Janet;
import io.techery.janet.WriteActionPipe;
import io.techery.janet.todo.action.TodoAction;
import io.techery.janet.todo.action.TodoStoreChangeAction;
import io.techery.janet.todo.store.TodoStore;

public class App extends Application {

    private ActionPipe<TodoAction> todoActionPipe;
    private TodoStore todoStore;

    @Override
    public void onCreate() {
        super.onCreate();
        Janet janet = new Janet.Builder()
                .addService(new CommandActionService())
                .build();
        todoActionPipe = janet.createPipe(TodoAction.class);
        todoStore = new TodoStore(todoActionPipe, janet.createPipe(TodoStoreChangeAction.class));
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public TodoStore getTodoStore() {
        return todoStore;
    }

    public WriteActionPipe<TodoAction> todoActionPipe() {
        return todoActionPipe;
    }

}
