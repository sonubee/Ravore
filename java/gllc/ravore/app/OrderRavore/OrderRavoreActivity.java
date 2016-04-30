package gllc.ravore.app.OrderRavore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.firebase.client.Firebase;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.localytics.android.Localytics;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.splunk.mint.Mint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import gllc.ravore.app.Automation.GetDateTimeInstance;
import gllc.ravore.app.Main.LoginActivity;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Orders;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 3/5/2016.
 */
public class OrderRavoreActivity extends AppCompatActivity {

    BraintreeFragment mBraintreeFragment;
    NewShoppingCart orderFragment;
    AsyncHttpClient client;
    AlertDialog.Builder alertadd;

    public static Double totalPrice = 0.0;
    public static int subTotalPrice = 0;
    public static Double shippingPrice = 0.0;
    public static int beadCount, kandiCount = 0;
    public static String emailAddy= "";

    public static Map<String, String> sendOrderMap = new HashMap<>();

    Firebase sendOrderToFirebase = new Firebase(MyApplication.useFirebase+"Orders1");

    public static String sandboxTokenBT = "sandbox_yrwnshf3_9j46c9m8t3mjfwwq";
    public static String productionTokenBT = "production_thxywyhz_69ppkf6h8fqh9cxb";
    public static String tokenToUse;

    public static String productionHeroku = "https://sheltered-wave-14675.herokuapp.com";
    public static String sandboxHeroku = "https://hidden-river-58763.herokuapp.com";
    public static String herokuToUse;

    public static String whichFragment = "ShoppingCartFragment";

    FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_screen);

        //whichFragment = "ShoppingCartFragment";
        setupVariables();
        setupFragments();
        wakeupServer();
    }

    public void setupVariables(){

//setup dialog
        alertadd = new AlertDialog.Builder(this);
//asynchttpclient
        client = new AsyncHttpClient();
//initilialize braintree
        try {
            mBraintreeFragment = BraintreeFragment.newInstance(this, tokenToUse);
        } catch (InvalidArgumentException e1) {
            e1.printStackTrace();
        }
//setup toolbar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);}
//set production or dev mode
        if (MyApplication.devStatus.equals("production")){
            herokuToUse=productionHeroku;
            tokenToUse = productionTokenBT;}

        if (MyApplication.devStatus.equals("sandbox")){
            herokuToUse=productionHeroku;
            tokenToUse = sandboxTokenBT;}

        if (MyApplication.devStatus.equals("production")){
            Mint.logEvent("Order Ravore Activity");}
    }

    public void setupFragments() {
        //setup first fragment
        //orderFragment = new ShoppingCartFragment();
        orderFragment = new NewShoppingCart();
        //make it happen
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, orderFragment).commit();
        //get second fragment ready
        //orderFragment = (ShoppingCartFragment)getSupportFragmentManager().
        //        findFragmentById(R.id.address_fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.i("MyActivity", "Options Selected");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i("MyActivity", "In Home");

                if (whichFragment.equals("ShoppingCartFragment")){
                    //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    /*
                    subTotalPrice =0;
                    totalPrice=0.0;
                    shippingPrice=0.35;
                    beadCount=0;
                    kandiCount=0;

                    ShoppingCartFragment.total.setText("$0");
                    ShoppingCartFragment.subtotal.setText("$0");
                    ShoppingCartFragment.shipping.setText("$0");
                    ShoppingCartFragment.beadCart.setText("Cart: 0");
                    ShoppingCartFragment.kandiCart.setText("Cart: 0");*/

                    ShoppingCartAdapter.cartQty.clear();
                    ShoppingCartAdapter.beadAdapter.clear();
                    finish();
                }

                else if (whichFragment.equals("PurchaseScreenFragment")){
                    whichFragment = "ShoppingCartFragment";
                    transaction.show(orderFragment);
                    getSupportFragmentManager().popBackStack();}

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void enterShippingInfo (View v){

        int totalCart = 0;

        for (int i=0; i<ShoppingCartAdapter.beadAdapter.size(); i++){totalCart += ShoppingCartAdapter.cartQty.get(i);}

        Log.i("--AllORActivity", "Size of Cart: " + totalCart);

        if (totalCart >= 5){

            PurchaseScreenFragment shippingFragment = new PurchaseScreenFragment();
            transaction = getSupportFragmentManager().beginTransaction();
            //transaction.replace(R.id.fragment_container, shippingFragment);
            transaction.add(R.id.fragment_container, shippingFragment);
            transaction.hide(orderFragment);

            transaction.addToBackStack(null);
            transaction.commit();

        }

        else {Toast.makeText(getApplicationContext(), "Please Order Minimum 5 for Effective Shipping Pricing", Toast.LENGTH_SHORT).show();}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MyApplication.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                String address = (String) place.getAddress();
                PurchaseScreenFragment.enterShipping.setText(address);
                PurchaseScreenFragment.enterShipping.setTextColor(Color.WHITE);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("--AllORActivity", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        if (requestCode == MyApplication.REQUEST_NONCE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                        BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                );
                String nonce = paymentMethodNonce.getNonce();
                Log.i("MyActivity", "Nonce: " + nonce);
                // Send the nonce to your server.
                postNonceToServer(nonce);
            }
        }
    }

    void postNonceToServer(String nonce) {
        Log.i("--AllORActivity", "Posting Nonce to Server");

        final RequestParams params = new RequestParams();
        params.put("payment_method_nonce", nonce);

        final ProgressDialog dialog = new ProgressDialog(OrderRavoreActivity.this);
        dialog.setMessage("Processing");
        dialog.show();

        //client.post(herokuToUse + "/checkout?payment_method_nonce=" + nonce + "&email=" + sendOrderMap.get("email") + "&amount=" + OrderRavoreActivity.totalPrice + "&devProd=" + MyApplication.devStatus,
        client.post(herokuToUse + "/checkout?payment_method_nonce=" + nonce + "&email=" + sendOrderMap.get("email") + "&amount=" + String.format("%.2f", PurchaseScreenFragment.totalPrice) + "&devProd=" + MyApplication.devStatus,
                new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("--AllORActivity", "In Failure");

                        Log.i("--AllORActivity", "Failure response: " + responseString);
                        dialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Did not go through. Try again.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        String orderNumber = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
                        //Orders newOrder = new Orders(kandiCount, beadCount, subTotalPrice, shippingPrice, totalPrice, orderNumber, "Android", sendOrderMap.get("address"), GetDateTimeInstance.getRegDate(), sendOrderMap.get("email"), sendOrderMap.get("fullName"), sendOrderMap.get("suiteApt"), "Processing", MyApplication.android_id);
                        Orders newOrder = new Orders(0, PurchaseScreenFragment.totalCart, PurchaseScreenFragment.totalCart, Math.round(PurchaseScreenFragment.shippingCost * 100.0) / 100.0, Math.round(PurchaseScreenFragment.totalPrice * 100.0) / 100.0, orderNumber, "Android", sendOrderMap.get("address"), GetDateTimeInstance.getRegDate(), sendOrderMap.get("email"), sendOrderMap.get("fullName"), sendOrderMap.get("suiteApt"), "Processing", MyApplication.android_id, PurchaseScreenFragment.cartMap.get("Cat"), PurchaseScreenFragment.cartMap.get("Dog"), PurchaseScreenFragment.cartMap.get("Walrus"), PurchaseScreenFragment.cartMap.get("Teddy Bear"), PurchaseScreenFragment.cartMap.get("Octopus"));

                        sendOrderToFirebase.push().setValue(newOrder);
                        Log.i("--AllORActivity", "Success Posting nonce");
                        dialog.dismiss();

                        if (MyApplication.cameFromLogin){
                            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                            startActivity(intent);
                            MyApplication.cameFromLogin = false;
                        }

                        Log.i("--AllORActivity", "Result: " + responseString);

                        finish();

                        Toast.makeText(getApplicationContext(), "Your order will arrive in 5 business days", Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void supportFAQ (View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderRavoreActivity.this);
        builder.setTitle("How It Works");
        builder.setMessage("1. Add your Kandi ID into app as a Giver\n\n2. Hand it to someone\n\n3. S/he adds as a Receiver! Now chat!\n\n\nOrders arrived in 5 Business Days.\nEmail: info@jobsmeplatform.com");
        builder.show();
    }

    public void wakeupServer(){
        client.get(herokuToUse + "/hello",
                new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("MyActivity", "Failed to Wake up Server");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("MyActivity", "Woke up Server");
                    }
                });
    }

    public void clickedBeadPic (View v){
        LayoutInflater factory = LayoutInflater.from(getApplicationContext());
        View view = factory.inflate(R.layout.full_photo, null);
        ImageView fullImageView = (ImageView) view.findViewById(R.id.fullPhotoImageview);
        fullImageView.setImageResource(R.drawable.beads_medium);
        alertadd.setView(view);

        alertadd.setNeutralButton("OK!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });

        alertadd.show();
    }

    public void clickedKandiPic (View v){

        LayoutInflater factory = LayoutInflater.from(getApplicationContext());
        View view = factory.inflate(R.layout.full_photo, null);
        ImageView fullImageView = (ImageView) view.findViewById(R.id.fullPhotoImageview);
        fullImageView.setImageResource(R.drawable.bg_medium);
        alertadd.setView(view);

        alertadd.setNeutralButton("OK!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });

        alertadd.show();
    }

}
