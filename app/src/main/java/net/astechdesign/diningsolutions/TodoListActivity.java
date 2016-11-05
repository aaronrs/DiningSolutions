package net.astechdesign.diningsolutions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.astechdesign.diningsolutions.model.Todo;
import net.astechdesign.diningsolutions.repositories.TodoRepo;

import java.util.List;

/**
 * An activity representing a list of Todos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TodoDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class TodoListActivity extends AbstractListActivity<Todo, TodoListActivity.TodoViewHolder> {

    private static final String ADD_TODO = "add_todo";

    @Override
    protected boolean optionItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_todo:
                FragmentManager fm = getSupportFragmentManager();
                dialogFragment = new NewTodoFragment();
                dialogFragment.show(fm, getDialogTitle());
                return true;
            default:
                return false;
        }
    }

    @Override
    protected String getDialogTitle() {
        return ADD_TODO;
    }

    @Override
    protected List<Todo> getAdapter() {
        return TodoRepo.get();
    }

    @Override
    protected View.OnClickListener getItemOnClickListener(final Todo value) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(getArgItemId(), value.id.toString());
                    TodoDetailFragment fragment = new TodoDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.todo_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, TodoDetailActivity.class);
                    intent.putExtra(TodoDetailFragment.ARG_ITEM_ID, value.id);

                    context.startActivity(intent);
                }
            }
        };
    }

    @Override
    protected TodoViewHolder getNewViewHolder(View view) {
        return new TodoViewHolder(view);
    }

    @Override
    protected void setHolderContent(TodoListActivity.TodoViewHolder holder, Todo value) {
        holder.mIdView.setText(value.id.toString());
        holder.mContentView.setText(value.content);
    }

    @Override
    protected String getArgItemId() {
        return TodoDetailFragment.ARG_ITEM_ID;
    }

    @Override
    protected int getListContentId() {
        return R.layout.todo_list_content;
    }

    @Override
    protected int getListMenu() {
        return R.menu.menu_todo_list;
    }

    @Override
    protected int getContainerView() {
        return R.id.todo_detail_container;
    }

    @Override
    protected int getRecyclerView() {
        return R.id.todo_list;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_todo_list;
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Todo mItem;

        public TodoViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}
