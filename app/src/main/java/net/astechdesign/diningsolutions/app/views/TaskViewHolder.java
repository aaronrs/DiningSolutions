package net.astechdesign.diningsolutions.app.views;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.Task;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    private final View mView;
    private final TextView mDateView;
    private final TextView mTimeView;
    private final TextView mTitleView;
    private final TextView mDescriptionView;
    private final ImageButton mDeleteBtn;
    private Task mTask;

    public TaskViewHolder(View view) {
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

    public View getView() {
        return mView;
    }

    public Task getTask() {
        return mTask;
    }
}
