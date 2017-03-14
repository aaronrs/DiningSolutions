package net.astechdesign.diningsolutions.reports;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.Record;

import java.util.List;
import java.util.Map;

public class MyRecordRecyclerViewAdapter extends RecyclerView.Adapter<MyRecordRecyclerViewAdapter.ViewHolder> {

    private final List<Record> mValues;

    public MyRecordRecyclerViewAdapter(List<Record> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Map<String, Object> data = holder.mItem.data;
        holder.mDateView.setText((String) data.get("date"));
        holder.mNameView.setText((String) data.get("product"));
        holder.mQuantityView.setText(Integer.toString((Integer) data.get("quantity")));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDateView;
        public final TextView mNameView;
        public final TextView mQuantityView;
        public Record mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.report_date);
            mNameView = (TextView) view.findViewById(R.id.report_product);
            mQuantityView = (TextView) view.findViewById(R.id.report_quantity);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
