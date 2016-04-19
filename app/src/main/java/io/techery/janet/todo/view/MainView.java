package io.techery.janet.todo.view;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import io.techery.janet.todo.R;
import io.techery.janet.todo.model.Todo;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import trikita.anvil.Anvil;
import trikita.anvil.RenderableView;

import static trikita.anvil.BaseDSL.MATCH;
import static trikita.anvil.BaseDSL.WRAP;
import static trikita.anvil.BaseDSL.above;
import static trikita.anvil.BaseDSL.alignParentBottom;
import static trikita.anvil.BaseDSL.alignParentEnd;
import static trikita.anvil.BaseDSL.alignParentStart;
import static trikita.anvil.BaseDSL.below;
import static trikita.anvil.BaseDSL.centerHorizontal;
import static trikita.anvil.BaseDSL.centerVertical;
import static trikita.anvil.BaseDSL.dip;
import static trikita.anvil.BaseDSL.init;
import static trikita.anvil.BaseDSL.padding;
import static trikita.anvil.BaseDSL.size;
import static trikita.anvil.BaseDSL.toLeftOf;
import static trikita.anvil.BaseDSL.toRightOf;
import static trikita.anvil.DSL.adapter;
import static trikita.anvil.DSL.button;
import static trikita.anvil.DSL.checkBox;
import static trikita.anvil.DSL.checked;
import static trikita.anvil.DSL.editText;
import static trikita.anvil.DSL.hint;
import static trikita.anvil.DSL.id;
import static trikita.anvil.DSL.listView;
import static trikita.anvil.DSL.maxLines;
import static trikita.anvil.DSL.onClick;
import static trikita.anvil.DSL.relativeLayout;
import static trikita.anvil.DSL.singleLine;
import static trikita.anvil.DSL.text;
import static trikita.anvil.DSL.textView;
import static trikita.anvil.RenderableAdapter.withItems;

public class MainView extends RenderableView {

    private final List<Todo> items = new ArrayList<>();
    private EditText input;
    private Action0 onButtonAddClick;
    private Action0 onButtonClearClick;
    private Action2<Todo, Boolean> onCheckTodo;
    private Action1<Todo> onButtonDeleteClick;

    public MainView(Context context) {
        super(context);
    }

    public void updateItems(List<Todo> items) {
        this.items.clear();
        this.items.addAll(items);
        adapter.notifyDataSetChanged();
    }

    public Editable getInputText() {
        return input.getText();
    }

    public void onButtonAddClick(Action0 action) {
        this.onButtonAddClick = action;
    }

    public void onButtonClearClick(Action0 action) {
        this.onButtonClearClick = action;
    }

    public void onButtonDeleteClick(Action1<Todo> action) {
        this.onButtonDeleteClick = action;
    }

    public void onCheckTodo(Action2<Todo, Boolean> action) {
        this.onCheckTodo = action;
    }

    private final BaseAdapter adapter = withItems(items, (index, item) -> {
        CharSequence text;
        if (item.completed()) {
            SpannableString spanString = new SpannableString(item.text());
            spanString.setSpan(new StrikethroughSpan(), 0, spanString.length(), 0);
            text = spanString;
        } else {
            text = item.text();
        }
        relativeLayout(() -> {
            size(MATCH, MATCH);
            checkBox(() -> {
                id(R.id.checkbox);
                centerVertical();
                centerHorizontal();
                alignParentStart();
                checked(item.completed());
                onClick(v -> {
                    if (onCheckTodo != null) {
                        onCheckTodo.call(item, ((CompoundButton) v).isChecked());
                    }
                });
            });
            textView(() -> {
                toLeftOf(R.id.button_delete);
                toRightOf(R.id.checkbox);
                centerVertical();
                text(text);
            });
            button(() -> {
                id(R.id.button_delete);
                alignParentEnd();
                text("Delete");
                onClick(v -> {
                    if (onButtonDeleteClick != null) onButtonDeleteClick.call(item);
                });
            });
        });
    });

    @Override
    public void view() {
        relativeLayout(() -> {
            size(MATCH, MATCH);
            relativeLayout(() -> {
                id(R.id.input_container);
                size(MATCH, WRAP);
                editText(() -> {
                    init(() -> input = Anvil.currentView());
                    id(R.id.input);
                    alignParentStart();
                    toLeftOf(R.id.button_add);
                    hint("What needs to be done?");
                    maxLines(1);
                    singleLine(true);
                });
                button(() -> {
                    id(R.id.button_add);
                    alignParentEnd();
                    text("Add");
                    onClick(v -> {
                        if (onButtonAddClick != null) onButtonAddClick.call();
                    });
                });
            });
            listView(() -> {
                size(MATCH, MATCH);
                below(R.id.input_container);
                above(R.id.button_clear);
                adapter(adapter);
            });
            button(() -> {
                id(R.id.button_clear);
                text("Clear completed");
                padding(dip(10));
                alignParentBottom();
                centerHorizontal();
                onClick(v -> {
                    if (onButtonClearClick != null) onButtonClearClick.call();
                });
            });
        });
    }
}
