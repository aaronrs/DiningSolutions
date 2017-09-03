package net.astechdesign.diningsolutions.tasks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.app.OrderManager;
import net.astechdesign.diningsolutions.app.model.CurrentCustomer;
import net.astechdesign.diningsolutions.app.views.TaskViewHolder;
import net.astechdesign.diningsolutions.model.Task;
import net.astechdesign.diningsolutions.orders.OrderActivity;

import java.util.List;

public class TaskRecyclerViewAdapter
        extends RecyclerView.Adapter<TaskViewHolder> {

    private final List<Task> mValues;
    private Context activity;

    public TaskRecyclerViewAdapter(Context activity, List<Task> items) {
        this.activity = activity;
        mValues = items;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_content, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, final int position) {
        holder.setItem(mValues.get(position));

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getTask().customerId == null) return;
                Intent intent = new Intent(activity, OrderActivity.class);
                CurrentCustomer.set(holder.getTask().customerId);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

}
