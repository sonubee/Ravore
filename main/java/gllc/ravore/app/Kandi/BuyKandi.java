package gllc.ravore.app.Kandi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import gllc.ravore.app.R;

/**
 * Created by bhangoo on 2/8/2016.
 */
public class BuyKandi extends Fragment {

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
        totalAmount.setText("$"+String.format("%.2f", OrderRavore.totalPrice));

        OrderRavore.whichFragment = "BuyKandi";
    }
}
