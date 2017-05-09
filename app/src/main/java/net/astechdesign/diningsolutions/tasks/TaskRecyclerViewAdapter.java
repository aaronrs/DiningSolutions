package net.astechdesign.diningsolutions.tasks;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Task;
import net.astechdesign.diningsolutions.orders.OrderActivity;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;

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
                if (holder.mTask.customerId == null) return;
                Intent intent = new Intent(activity, OrderActivity.class);
                Customer customer = CustomerRepo.get(activity).get(holder.mTask.customerId);
                intent.putExtra(OrderActivity.CUSTOMER, customer);
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
        private final ImageButton mDeleteBtn;
        private Task mTask;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.task_date);
            mTimeView = (TextView) view.findViewById(R.id.task_time);
            mTitleView = (TextView) view.findViewById(R.id.task_title);
            mTitleView.setTypeface(Typeface.DEFAULT_BOLD);
            mDescriptionView = (TextView) view.findViewById(R.id.task_description);
            mDeleteBtn = (ImageButton) view.findViewById(R.id.btn_delete_task);
        }

        public void setItem(Task item) {
            this.mTask = item;
            if (mTask.title.equals("Visit")) {
                mView.setBackgroundColor(Color.parseColor("#E8E8E8"));
            } else if (mTask.title.equals("Delivery")) {
                mView.setBackgroundColor(Color.parseColor("#CCCCCC"));
            }
            mDateView.setText(mTask.date.getDisplayDate());
            mTimeView.setText(mTask.date.getDisplayTime());
            mTitleView.setText(mTask.title);
            mDescriptionView.setText(mTask.description);
            if (mTask.title.equals("Visit") || mTask.title.equals("Delivery")) {
                mDeleteBtn.setEnabled(false);
            } else {
                mDeleteBtn.setTag(mTask);
                mDeleteBtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
