package io.techery.janet.todo.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import io.techery.janet.ActionPipe;
import io.techery.janet.CommandActionBase;
import io.techery.janet.ReadActionPipe;
import io.techery.janet.todo.action.TodoAction;
import io.techery.janet.todo.action.TodoStoreChangeAction;
import io.techery.janet.todo.model.Todo;
import rx.Observable;

public class TodoStore {

    private final List<Todo> state = new ArrayList<>();
    private final ActionPipe<TodoStoreChangeAction> storeChangeActionPipe;
    private Todo lastDeleted;

    public TodoStore(ReadActionPipe<TodoAction> readActionPipe, ActionPipe<TodoStoreChangeAction> storeChangeActionPipe) {
        this.storeChangeActionPipe = storeChangeActionPipe;
        readActionPipe.observeSuccess()
                .subscribe(this::onAction);
    }

    private void onAction(TodoAction action) {
        switch (action.getAction()) {
            case ADD: {
                Iterator<Todo> iter = state.iterator();
                while (iter.hasNext()) {
                    Todo todo = iter.next();
                    if (action.getResult().id()
                            .equals(todo.id())) {
                        iter.remove();
                        break;
                    }
                }
                state.add(action.getResult());
                break;
            }
            case DELETE: {
                lastDeleted = action.getResult();
                state.remove(lastDeleted);
                break;
            }
            case CLEAR_COMPLETED: {
                Iterator<Todo> iter = state.iterator();
                while (iter.hasNext()) {
                    Todo todo = iter.next();
                    if (todo.completed()) {
                        iter.remove();
                    }
                }
                break;
            }
            case UNDO_DELETED: {
                if (lastDeleted != null) {
                    state.add(lastDeleted);
                }
                break;
            }
        }
        if (action.getAction() != TodoAction.Action.DELETE) {
            lastDeleted = null;
        }
        notifyStateChanged();
    }

    private void notifyStateChanged() {
        storeChangeActionPipe.send(new TodoStoreChangeAction(getState()));
    }

    public List<Todo> getState() {
        Collections.sort(state);
        return state;
    }

    public Observable<List<Todo>> observeChages() {
        return storeChangeActionPipe.observeSuccess().map(CommandActionBase::getResult);
    }

    public boolean canUndo() {
        return lastDeleted != null;
    }

}
