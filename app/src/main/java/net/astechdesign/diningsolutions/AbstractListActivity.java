package net.astechdesign.diningsolutions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.DSDTime;

import java.util.Date;
import java.util.List;

public abstract class AbstractListActivity<T, U extends RecyclerView.ViewHolder> extends AppCompatActivity {

    public final String ARG_ITEM_ID = "item_id";

    private static final String DATE_PICKER = "date_picker";
    private static final String TIME_PICKER = "time_picker";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    protected boolean mTwoPane;
    protected DialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(getRecyclerView());
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(getContainerView()) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (optionItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(getListMenu(), menu);
        return true;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(getAdapter()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<U> {

        private final List<T> mValues;

        public SimpleItemRecyclerViewAdapter(List<T> items) {
            mValues = items;
        }

        @Override
        public U onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(getListContentId(), parent, false);
            return getNewViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final U holder, int position) {
            final T value = mValues.get(position);
            setHolderContent(holder, value);
            holder.itemView.setOnClickListener(getItemOnClickListener(value));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

    public void getDate(View v) {
        FragmentManager fm = getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(new DSDDate());
        dialog.setTargetFragment(dialogFragment, DatePickerFragment.REQUEST_DATE);
        dialog.show(fm, DATE_PICKER);

        Toast toast = Toast.makeText(this, "get date picker", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void getTime(View v) {
        FragmentManager fm = getSupportFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(new DSDTime(new Date()));
        dialog.setTargetFragment(dialogFragment, TimePickerFragment.REQUEST_TIME);
        dialog.show(fm, TIME_PICKER);

        Toast toast = Toast.makeText(this, "get date picker", Toast.LENGTH_SHORT);
        toast.show();
    }

    protected abstract boolean optionItemSelected(MenuItem item);

    protected abstract String getDialogTitle();

    protected abstract int getListMenu();

    protected abstract int getContainerView();

    protected abstract int getRecyclerView();

    protected abstract int getContentView();

    protected abstract List<T> getAdapter();

    protected abstract View.OnClickListener getItemOnClickListener(T value);

    protected abstract U getNewViewHolder(View view);

    protected abstract void setHolderContent(U holder, T value);

    protected abstract String getArgItemId();

    protected abstract int getListContentId();
}
