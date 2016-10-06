package net.astechdesign.diningsolutions;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;

import java.util.List;

public class CustomerListFragment extends Fragment {

    private RecyclerView mCustomerRecyclerView;
    private CustomerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);

        mCustomerRecyclerView = (RecyclerView) view.findViewById(R.id.customer_recycler_view);
        mCustomerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    private void updateUI() {
        CustomerRepo repo = CustomerRepo.get(getActivity());
        List<Customer> customers = repo.getmCustomers();

        mAdapter = new CustomerAdapter(customers);
        mCustomerRecyclerView.setAdapter(mAdapter);
    }

    private class CustomerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mNameTextView;
        private final TextView mPhoneTextView;

        private Customer mCustomer;

        public CustomerHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mNameTextView = (TextView)itemView.findViewById(R.id.list_name);
            mPhoneTextView = (TextView)itemView.findViewById(R.id.list_phone);
        }

        public void bindCustomer(Customer customer) {
            mCustomer = customer;
            mNameTextView.setText(mCustomer.name);
            mPhoneTextView.setText(mCustomer.phone.number);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), CustomerActivity.class);
            startActivity(intent);
        }
    }

    private class CustomerAdapter extends RecyclerView.Adapter<CustomerHolder> {

        private List<Customer> mCustomers;

        public CustomerAdapter(List<Customer> mCustomers) {
            this.mCustomers = mCustomers;
        }

        @Override
        public CustomerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_customer, parent, false);
            return new CustomerHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomerHolder holder, int position) {
            Customer customer = mCustomers.get(position);
            holder.bindCustomer(customer);
        }

        @Override
        public int getItemCount() {
            return mCustomers.size();
        }
    }
}
