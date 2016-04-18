package gllc.ravore.app.OrderRavore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.PaymentRequest;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import gllc.ravore.app.MyApplication;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 2/8/2016.
 */
public class PurchaseScreenFragment extends Fragment {

    public static TextView totalAmount, enterShipping, kandiDisplay, beadDisplay, shippingDisplay, subtotalDisplay;
    public static EditText fullName, suiteApt;
    public static Button sendOrder;
    public static double totalPrice;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_kandi, container, false);



        //totalAmount = (TextView)view.findViewById(R.id.totalAmountToShow);
        enterShipping = (TextView)view.findViewById(R.id.shipping);
        fullName = (EditText)view.findViewById(R.id.first_name);
        suiteApt = (EditText)view.findViewById(R.id.suiteApt);
        sendOrder = (Button)view.findViewById(R.id.buyKandi);
        beadDisplay = (TextView)view.findViewById(R.id.beadDisplay);
        //kandiDisplay = (TextView)view.findViewById(R.id.kandiDisplay);

        subtotalDisplay = (TextView)view.findViewById(R.id.totalAmountToShow);
        shippingDisplay = (TextView)view.findViewById(R.id.shippingTotalDisplay);
        totalAmount = (TextView)view.findViewById(R.id.totalAmountDisplay);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int totalCart = 0;
        double shippingCost = 2.45;

        for (int i=0; i<ShoppingCartAdapter.beadAdapter.size(); i++){
            totalCart += ShoppingCartAdapter.cartQty.get(i);
            shippingCost += 0.05;
        }

        totalPrice = totalCart + shippingCost;

        subtotalDisplay.setText("Subtotal: $" + totalCart + ".00");
        //shippingDisplay.setText("$" + shippingCost);
        shippingDisplay.setText("Shipping: $" + String.format("%.2f", shippingCost));
        beadDisplay.setText("Total Beads: " + totalCart);
        //totalAmount.setText("$" + totalPrice);
        totalAmount.setText("Total: $" + String.format("%.2f", totalPrice));

        //totalAmount.setText("$" + String.format("%.2f", OrderRavoreActivity.totalPrice));


        OrderRavoreActivity.whichFragment = "PurchaseScreenFragment";

        enterShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    getActivity().startActivityForResult(intent, MyApplication.PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("MyActivity", "Shipping: " + enterShipping.getText().toString());

                if (fullName.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please Enter The Shipping Name", Toast.LENGTH_SHORT).show();
                } else if (enterShipping.getText().toString().equals("Shipping Address")) {
                    Toast.makeText(getContext(), "Please Enter Shipping Address", Toast.LENGTH_SHORT).show();
                } else {
                    String timeStamp = new SimpleDateFormat("MM/dd/yyyy").format(new Date());

                    OrderRavoreActivity.sendOrderMap.put("fullName", fullName.getText().toString());
                    OrderRavoreActivity.sendOrderMap.put("address", enterShipping.getText().toString());
                    OrderRavoreActivity.sendOrderMap.put("suiteApt", suiteApt.getText().toString());
                    OrderRavoreActivity.sendOrderMap.put("OS", "Android");
                    //OrderRavoreActivity.sendOrderMap.put("amount", String.valueOf(OrderRavoreActivity.totalPrice));
                    OrderRavoreActivity.sendOrderMap.put("amount", String.valueOf(totalPrice));
                    //OrderRavoreActivity.sendOrderMap.put("beadCount", String.valueOf(OrderRavoreActivity.beadCount));
                    OrderRavoreActivity.sendOrderMap.put("beadCount", String.valueOf(ShoppingCartAdapter.cartQty.size()));
                    //OrderRavoreActivity.sendOrderMap.put("kandiCount", String.valueOf(OrderRavoreActivity.kandiCount));
                    OrderRavoreActivity.sendOrderMap.put("kandiCount", String.valueOf(1));
                    OrderRavoreActivity.sendOrderMap.put("date", timeStamp);

                    final EditText input = new EditText(getContext());
                    input.setHint("(Enter Email Here)");
                    input.setTextColor(Color.BLUE);
                    input.setHintTextColor(Color.BLACK);
                    input.setIncludeFontPadding(true);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("(Optional) Receipt?")
                            .setMessage("Do you want us to email you a receipt and confirmation order?")
                            .setView(input)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (input.getText().toString().equals("")) {
                                        Toast.makeText(getContext(), "Please enter an email", Toast.LENGTH_SHORT).show();
                                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(input.getText().toString()).matches()) {
                                        Toast.makeText(getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                                    } else {
                                        OrderRavoreActivity.sendOrderMap.put("email", input.getText().toString());

                                        OrderRavoreActivity.emailAddy = input.getText().toString();

                                        PaymentRequest paymentRequest = new PaymentRequest()
                                                .clientToken(OrderRavoreActivity.tokenToUse);
                                        getActivity().startActivityForResult(paymentRequest.getIntent(getContext()), MyApplication.REQUEST_NONCE);
                                    }
                                }
                            })
                            .setNegativeButton("No Receipt", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    OrderRavoreActivity.sendOrderMap.put("email", "gsbllc2011@gmail.com");
                                    PaymentRequest paymentRequest = new PaymentRequest()
//
                                            .clientToken(OrderRavoreActivity.tokenToUse);
                                    getActivity().startActivityForResult(paymentRequest.getIntent(getContext()), MyApplication.REQUEST_NONCE);
                                }

                            })
                            .show();
                }
            }
        });

        //beadDisplay.setText("Beads (Set of 3): " + OrderRavoreActivity.beadCount);
        //kandiDisplay.setText("Kandi (Set of 3): " + OrderRavoreActivity.kandiCount);
    }

    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
