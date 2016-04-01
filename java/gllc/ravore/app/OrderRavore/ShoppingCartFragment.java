package gllc.ravore.app.OrderRavore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gllc.ravore.app.R;

/**
 * Created by bhangoo on 3/5/2016.
 */
public class ShoppingCartFragment extends Fragment {

    public static TextView total, subtotal, shipping, kandiCart, beadCart, clearButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actual_order_screen, container, false);
        Log.i("MyActivity", "FIIIIIIIIIIIIIIIIIINDMEEEE");
        Log.i("MyActivity", "FIIIIIIIIIIIIIIIIIINDMEEEE");
        Log.i("MyActivity", "FIIIIIIIIIIIIIIIIIINDMEEEE");
        Log.i("MyActivity", "FIIIIIIIIIIIIIIIIIINDMEEEE");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        total = (TextView)getActivity().findViewById(R.id.totalPrice);
        subtotal = (TextView)getActivity().findViewById(R.id.subtotalPrice);
        shipping = (TextView)getActivity().findViewById(R.id.shippingPrice);
        beadCart = (TextView)getActivity().findViewById(R.id.beadsCart);
        kandiCart = (TextView)getActivity().findViewById(R.id.kandiCart);
        clearButton = (TextView)getActivity().findViewById(R.id.clearButton);

        clearButton.setVisibility(View.INVISIBLE);

        Log.i("MyActivity", "Got to Order Ravore Fragment");

/*
        total.setText("$0");
        subtotal.setText("$0");
        shipping.setText("$0");
        beadCart.setText("Cart: 0");
        kandiCart.setText("Cart: 0");
        clearButton.setVisibility(View.INVISIBLE);

        total.setVisibility(View.VISIBLE);
*/
        OrderRavoreActivity.whichFragment = "ShoppingCartFragment";
    }
}
