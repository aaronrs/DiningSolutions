package net.astechdesign.diningsolutions.orders;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.Order;

import static net.astechdesign.diningsolutions.orders.OrderDetailFragment.ORDER;

public class OrderFragment extends Fragment {

    private Order mOrder;
    private OrderActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (OrderActivity) getActivity();
        mOrder = mActivity.getOrder();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_detail, container);

        OrderDetailFragment fragment = new OrderDetailFragment();

        Bundle args = new Bundle();
        args.putSerializable(ORDER, mOrder);
        if (mOrder != null) {
            ((TextView) view.findViewById(R.id.order_invoice_number)).setText(mOrder.invoiceNumber);

            TextView invoiceDateView = (TextView) view.findViewById(R.id.order_invoice_date);
            invoiceDateView.setText(mOrder.created.getDisplayDate());
            invoiceDateView.setTag(mOrder.created.getDisplayDate());
        fragment.setArguments(args);
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.order_container, fragment)
                .commit();
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
