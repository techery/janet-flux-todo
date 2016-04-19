package io.techery.janet.todo.action;

import io.techery.janet.CommandActionBase;

abstract class ValueCommandAction<T> extends CommandActionBase<T> {

    private final T value;

    public ValueCommandAction(T value) {
        this.value = value;
    }

    @Override
    protected final void run(CommandCallback<T> callback) throws Throwable {
        callback.onSuccess(value);
    }
}
