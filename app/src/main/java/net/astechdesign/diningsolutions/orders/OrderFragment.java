package net.astechdesign.diningsolutions.orders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.util.List;
import java.util.UUID;

public class OrderFragment extends Fragment {


    private static final String EXTRA_ORDER_ID = "net.astechdesign.diningsolutions.order_id";

    private Order mOrder;
    private TextView mNameField;
    private TextView mInvoiceNumberField;
    private ListView mOrderList;
    private TextView mDeliveryDateField;
    private List<OrderItem> mOrderItems;
    private ListAdapter mOrderAdapter = new OrderItemListAdapter();
    private Button mAddProductButton;

    public static Fragment newInstance(UUID id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_ORDER_ID, id);
        OrderFragment orderFragment = new OrderFragment();
        orderFragment.setArguments(bundle);
        return orderFragment;
    }

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            UUID orderId = (UUID)  savedInstanceState.getSerializable(EXTRA_ORDER_ID);
            mOrder = OrderRepo.get(getActivity()).getOrder(orderId);
        } else {
            mOrder = OrderRepo.get(getActivity()).getmOrders().get(0);
        }

        mOrderItems = mOrder.orderItems;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        mNameField = (TextView) view.findViewById(R.id.customer_name);
        mDeliveryDateField = (TextView) view.findViewById(R.id.delivery_date);
        mInvoiceNumberField = (TextView) view.findViewById(R.id.invoice_number);
        mOrderList = (ListView) view.findViewById(R.id.list_item_order);
        mAddProductButton = (Button) view.findViewById(R.id.add_product_button);
        mAddProductButton.setEnabled(false);

        ((TextView) view.findViewById(R.id.total_quantity)).setText(Integer.toString(mOrder.totalQuantity()));
        ((TextView) view.findViewById(R.id.total_cost)).setText(Double.toString(mOrder.totalCost()));

        mNameField.setText("Aaron Southwell");
        mDeliveryDateField.setText(new DSDDate("2016-01-01").toString());
        mInvoiceNumberField.setText("00001233");

        mOrderList.setAdapter(mOrderAdapter);
        mNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                mCustomer.name = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

//        mDateButton = (Button)view.findViewById(R.id.customer_created);
//        java.text.DateFormat dateFormat = DateFormat.getMediumDateFormat(getActivity());
//        mDateButton.setText(dateFormat.format(mOrder.created));
//        mDateButton.setEnabled(false);

//        mCurrentCheckBox = (CheckBox) view.findViewById(R.id.customer_current);
//        mCurrentCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                mCustomer.setCurrent(isChecked);
//            }
//        });

        return view;
    }

    private class OrderItemListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mOrderItems.size();
        }

        @Override
        public OrderItem getItem(int position) {
            return mOrderItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.order_item, parent, false);
            OrderItem item = getItem(position);
            setText(view, R.id.product_name, item.name);
            setText(view, R.id.batch_number, item.batch);
            setText(view, R.id.quantity, item.quantity);
            setText(view, R.id.price, item.price);
            setText(view, R.id.cost, item.price * item.quantity);
            return view;
        }

        private void setText(View view, int viewId, Object text) {
            ((TextView) view.findViewById(viewId)).setText(text.toString());
        }
    }
}