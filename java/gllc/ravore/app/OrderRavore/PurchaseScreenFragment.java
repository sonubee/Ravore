package gllc.ravore.app.OrderRavore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gllc.ravore.app.R;

/**
 * Created by bhangoo on 2/8/2016.
 */
public class PurchaseScreenFragment extends Fragment {

    public static TextView totalAmount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_kandi, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        totalAmount = (TextView)getActivity().findViewById(R.id.totalAmountToShow);
        totalAmount.setText("$"+String.format("%.2f", OrderRavoreActivity.totalPrice));

        OrderRavoreActivity.whichFragment = "PurchaseScreenFragment";
    }
}
