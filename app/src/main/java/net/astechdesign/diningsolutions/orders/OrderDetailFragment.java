package net.astechdesign.diningsolutions.orders;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.util.UUID;

public class OrderDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private Order mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = OrderRepo.get(getActivity(), (UUID)getArguments().getSerializable(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("Orders");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.order_detail_name)).setText(mItem.id.toString());
            ((TextView) rootView.findViewById(R.id.order_detail_phone)).setText(mItem.customerId.toString());
            ((TextView) rootView.findViewById(R.id.order_detail_email)).setText(mItem.created.toString());
        }

        return rootView;
    }
}
