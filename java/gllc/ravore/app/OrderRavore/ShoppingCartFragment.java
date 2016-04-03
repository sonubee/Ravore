package gllc.ravore.app.OrderRavore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import gllc.ravore.app.R;

/**
 * Created by bhangoo on 3/5/2016.
 */
public class ShoppingCartFragment extends Fragment {

    public static TextView total, subtotal, shipping, kandiCart, beadCart;
    Button subtractBead, addBead, addKandi, subtractKandi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actual_order_screen, container, false);

        total = (TextView)view.findViewById(R.id.totalPrice);
        subtotal = (TextView)view.findViewById(R.id.subtotalPrice);
        shipping = (TextView)view.findViewById(R.id.shippingPrice);
        beadCart = (TextView)view.findViewById(R.id.beadsCart);
        kandiCart = (TextView)view.findViewById(R.id.kandiCart);
        subtractBead = (Button)view.findViewById(R.id.subtractBeadButton);
        addBead = (Button)view.findViewById(R.id.addBeadButton);
        addKandi = (Button)view.findViewById(R.id.addBraceletButton);
        subtractKandi = (Button)view.findViewById(R.id.subtractKandiButton);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        OrderRavoreActivity.whichFragment = "ShoppingCartFragment";

        subtractBead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OrderRavoreActivity.beadCount == 1 && OrderRavoreActivity.kandiCount == 0){
                    OrderRavoreActivity.beadCount--;
                    OrderRavoreActivity.shippingPrice = 0.0;
                    OrderRavoreActivity.subTotalPrice = 0;}

                else if (OrderRavoreActivity.beadCount == 0 && OrderRavoreActivity.kandiCount == 0){
                    OrderRavoreActivity.shippingPrice = 0.0;
                    OrderRavoreActivity.subTotalPrice = 0;}

                else if (OrderRavoreActivity.beadCount > 0) {
                    OrderRavoreActivity.beadCount--;
                    OrderRavoreActivity.subTotalPrice -= 3;
                    OrderRavoreActivity.shippingPrice -= 0.15;
                }

                OrderRavoreActivity.totalPrice = OrderRavoreActivity.subTotalPrice + OrderRavoreActivity.shippingPrice;

                ShoppingCartFragment.total.setText("$"+String.format("%.2f", OrderRavoreActivity.totalPrice));
                ShoppingCartFragment.subtotal.setText("$"+OrderRavoreActivity.subTotalPrice);
                ShoppingCartFragment.shipping.setText("$"+String.format("%.2f", OrderRavoreActivity.shippingPrice));
                ShoppingCartFragment.beadCart.setText("Cart: " + OrderRavoreActivity.beadCount);
            }
        });

        addBead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OrderRavoreActivity.beadCount == 0 && OrderRavoreActivity.kandiCount == 0){OrderRavoreActivity.shippingPrice = 0.30;}

                OrderRavoreActivity.beadCount++;
                OrderRavoreActivity.subTotalPrice += 3;
                OrderRavoreActivity.shippingPrice += 0.15;
                OrderRavoreActivity.totalPrice = OrderRavoreActivity.subTotalPrice + OrderRavoreActivity.shippingPrice;


                ShoppingCartFragment.total.setText("$"+String.format("%.2f", OrderRavoreActivity.totalPrice));
                ShoppingCartFragment.subtotal.setText("$"+OrderRavoreActivity.subTotalPrice);
                ShoppingCartFragment.shipping.setText("$"+String.format("%.2f", OrderRavoreActivity.shippingPrice));
                ShoppingCartFragment.beadCart.setText("Cart: " + OrderRavoreActivity.beadCount);

            }
        });

        subtractKandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (OrderRavoreActivity.beadCount == 0 && OrderRavoreActivity.kandiCount == 1){
                    OrderRavoreActivity.kandiCount--;
                    OrderRavoreActivity.shippingPrice = 0.0;
                    OrderRavoreActivity.subTotalPrice = 0;
                }

                else if (OrderRavoreActivity.beadCount == 0 && OrderRavoreActivity.kandiCount == 0){
                    OrderRavoreActivity.shippingPrice = 0.0;
                    OrderRavoreActivity.subTotalPrice = 0;
                }

                else if (OrderRavoreActivity.kandiCount > 0) {
                    OrderRavoreActivity.kandiCount--;
                    OrderRavoreActivity.subTotalPrice -= 5;
                    OrderRavoreActivity.shippingPrice -= 0.35;
                }

                OrderRavoreActivity.totalPrice = OrderRavoreActivity.subTotalPrice + OrderRavoreActivity.shippingPrice;

                ShoppingCartFragment.total.setText("$" + String.format("%.2f", OrderRavoreActivity.totalPrice));
                ShoppingCartFragment.subtotal.setText("$"+ OrderRavoreActivity.subTotalPrice);
                ShoppingCartFragment.shipping.setText("$"+String.format("%.2f", OrderRavoreActivity.shippingPrice));
                ShoppingCartFragment.kandiCart.setText("Cart: " + OrderRavoreActivity.kandiCount);

            }
        });

        addKandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OrderRavoreActivity.beadCount == 0 && OrderRavoreActivity.kandiCount == 0) {
                    OrderRavoreActivity.shippingPrice = 0.30;
                }

                OrderRavoreActivity.subTotalPrice += 5;
                OrderRavoreActivity.shippingPrice += 0.35;
                OrderRavoreActivity.totalPrice = OrderRavoreActivity.subTotalPrice + OrderRavoreActivity.shippingPrice;
                OrderRavoreActivity.kandiCount++;

                ShoppingCartFragment.total.setText("$" + String.format("%.2f", OrderRavoreActivity.totalPrice));
                ShoppingCartFragment.subtotal.setText("$" + OrderRavoreActivity.subTotalPrice);
                ShoppingCartFragment.shipping.setText("$" + String.format("%.2f", OrderRavoreActivity.shippingPrice));
                ShoppingCartFragment.kandiCart.setText("Cart: " + OrderRavoreActivity.kandiCount);
            }
        });
    }
}
