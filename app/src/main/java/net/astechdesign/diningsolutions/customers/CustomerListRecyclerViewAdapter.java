package net.astechdesign.diningsolutions.customers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.orders.OrderActivity;

import java.util.List;

public class CustomerListRecyclerViewAdapter
        extends RecyclerView.Adapter<CustomerListRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private final List<Customer> mValues;

    public CustomerListRecyclerViewAdapter(Context context, List<Customer> items) {
        this.context = context;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setItem(mValues.get(position));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderActivity.class);
                intent.putExtra(OrderActivity.CUSTOMER, holder.mItem);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mPhoneView;
        public final TextView mAddressView;
        public Customer mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mPhoneView = (TextView) view.findViewById(R.id.telephone);
            mAddressView = (TextView) view.findViewById(R.id.address);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }

        public void setItem(Customer item) {
            this.mItem = item;
            mNameView.setText(item.name);
            mPhoneView.setText(item.phone.number);
            mAddressView.setText(item.address.toString());
        }
    }
}
