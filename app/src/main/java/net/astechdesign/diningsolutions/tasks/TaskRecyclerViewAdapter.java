package net.astechdesign.diningsolutions.tasks;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.customers.CustomerListActivity;
import net.astechdesign.diningsolutions.model.Task;

import java.util.List;

public class TaskRecyclerViewAdapter
        extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {

    private final List<Task> mValues;
    private FragmentActivity activity;

    public TaskRecyclerViewAdapter(FragmentActivity activity, List<Task> items) {
        this.activity = activity;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setItem(mValues.get(position));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CustomerListActivity.class);
                intent.putExtra(CustomerListActivity.CUSTOMER_ID, holder.mTask.customerId);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mDateView;
        private final TextView mTimeView;
        private final TextView mTitleView;
        private final TextView mDescriptionView;
        private Task mTask;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.task_date);
            mTimeView = (TextView) view.findViewById(R.id.task_time);
            mTitleView = (TextView) view.findViewById(R.id.task_title);
            mDescriptionView = (TextView) view.findViewById(R.id.task_description);
        }

        public void setItem(Task item) {
            this.mTask = item;
            if (mTask.title.startsWith("Visit")) {
                mView.setBackgroundColor(Color.parseColor("#aaaaee"));
            } else if (mTask.title.startsWith("Delivery")) {
                mView.setBackgroundColor(Color.parseColor("#8888be"));
            }
            mDateView.setText(mTask.date.getDisplayDate());
            mTimeView.setText(mTask.date.getDisplayTime());
            mTitleView.setText(mTask.title);
            mDescriptionView.setText(mTask.description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
