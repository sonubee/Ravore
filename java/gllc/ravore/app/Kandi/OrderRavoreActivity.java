package gllc.ravore.app.Kandi;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import gllc.ravore.app.Main.LoginActivity;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Orders;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 3/5/2016.
 */
public class OrderRavoreActivity extends AppCompatActivity {

    BraintreeFragment mBraintreeFragment;
    ShoppingCartFragment orderFragment;
    AsyncHttpClient client;
    AlertDialog.Builder alertadd;

    EditText fullName;
    TextView shippingAddress, suiteApt;
    public static Double totalPrice = 0.0;
    public static int subTotalPrice = 0;
    public static Double shippingPrice = 0.0;
    int beadCount, kandiCount;

    final Map<String, String> sendOrder = new HashMap<>();

    Firebase sendOrderToFirebase = new Firebase(MyApplication.useFirebase+"Orders");

    public static String sandboxTokenBT = "sandbox_yrwnshf3_9j46c9m8t3mjfwwq";
    public static String productionTokenBT = "production_thxywyhz_69ppkf6h8fqh9cxb";
    public static String tokenToUse;

    public static String productionHeroku = "https://sheltered-wave-14675.herokuapp.com";
    public static String sandboxHeroku = "https://hidden-river-58763.herokuapp.com";
    public static String herokuToUse;

    public static String whichFragment = "ShoppingCartFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_screen);

        setupFragments();
        setupVariables();
        wakeupServer();
    }

    public void setupVariables(){
//initialization variables
        beadCount = 0;
        kandiCount= 0;
        shippingPrice=0.0;
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


    }

    public void setupFragments() {
        //setup first fragment
        orderFragment = new ShoppingCartFragment();
        //make it happen
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, orderFragment).commit();
        //get second fragment ready
        orderFragment = (ShoppingCartFragment)getSupportFragmentManager().
                findFragmentById(R.id.address_fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.i("MyActivity", "Options Selected");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i("MyActivity", "In Home");

                if (whichFragment.equals("ShoppingCartFragment")){
                    //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    subTotalPrice =0;
                    totalPrice=0.0;
                    shippingPrice=0.35;
                    beadCount=0;
                    kandiCount=0;

                    ShoppingCartFragment.total.setText("$0");
                    ShoppingCartFragment.subtotal.setText("$0");
                    ShoppingCartFragment.shipping.setText("$0");
                    ShoppingCartFragment.beadCart.setText("Cart: 0");
                    ShoppingCartFragment.kandiCart.setText("Cart: 0");
                    ShoppingCartFragment.clearButton.setVisibility(View.INVISIBLE);
                    finish();
                }

                else if (whichFragment.equals("PurchaseScreenFragment")){
                    //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    whichFragment = "ShoppingCartFragment";
                    getSupportFragmentManager().popBackStack();}

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addBracelet (View v) {
        if (beadCount == 0 && kandiCount == 0){shippingPrice = 0.30;}

        subTotalPrice += 5;
        shippingPrice += 0.35;
        totalPrice = subTotalPrice + shippingPrice;
        kandiCount++;

        ShoppingCartFragment.total.setText("$" + String.format("%.2f", totalPrice));
        ShoppingCartFragment.subtotal.setText("$"+subTotalPrice);
        ShoppingCartFragment.shipping.setText("$"+String.format("%.2f", shippingPrice));
        ShoppingCartFragment.kandiCart.setText("Cart: " + kandiCount);
        //ShoppingCartFragment.clearButton.setVisibility(View.VISIBLE);
    }

    public void subtractKandi (View v) {

        if (beadCount == 0 && kandiCount == 1){
            kandiCount--;
            shippingPrice = 0.0;
            subTotalPrice = 0;
        }

        else if (beadCount == 0 && kandiCount == 0){
            shippingPrice = 0.0;
            subTotalPrice = 0;
        }

        else if (kandiCount > 0) {
            kandiCount--;
            subTotalPrice -= 5;
            shippingPrice -= 0.35;
        }

            totalPrice = subTotalPrice + shippingPrice;

            ShoppingCartFragment.total.setText("$" + String.format("%.2f", totalPrice));
            ShoppingCartFragment.subtotal.setText("$"+subTotalPrice);
            ShoppingCartFragment.shipping.setText("$"+String.format("%.2f", shippingPrice));
            ShoppingCartFragment.kandiCart.setText("Cart: " + kandiCount);
            //ShoppingCartFragment.clearButton.setVisibility(View.VISIBLE);

    }

    public void addBead (View v) {
        if (beadCount == 0 && kandiCount == 0){shippingPrice = 0.30;}

        beadCount++;
        subTotalPrice += 3;
        shippingPrice += 0.15;
        totalPrice = subTotalPrice + shippingPrice;


        ShoppingCartFragment.total.setText("$"+String.format("%.2f", totalPrice));
        ShoppingCartFragment.subtotal.setText("$"+subTotalPrice);
        ShoppingCartFragment.shipping.setText("$"+String.format("%.2f", shippingPrice));
        ShoppingCartFragment.beadCart.setText("Cart: " + beadCount);
        //ShoppingCartFragment.clearButton.setVisibility(View.VISIBLE);
    }

    public void subtractBead (View v) {

        if (beadCount == 1 && kandiCount == 0){
            beadCount--;
            shippingPrice = 0.0;
            subTotalPrice = 0;}

        else if (beadCount == 0 && kandiCount == 0){
            shippingPrice = 0.0;
            subTotalPrice = 0;}

        else if (beadCount > 0) {
            beadCount--;
            subTotalPrice -= 3;
            shippingPrice -= 0.15;
        }

            totalPrice = subTotalPrice + shippingPrice;

            ShoppingCartFragment.total.setText("$"+String.format("%.2f", totalPrice));
            ShoppingCartFragment.subtotal.setText("$"+subTotalPrice);
            ShoppingCartFragment.shipping.setText("$"+String.format("%.2f", shippingPrice));
            ShoppingCartFragment.beadCart.setText("Cart: " + beadCount);

    }

    public void enterShippingInfo (View v){

        if (totalPrice > 1){
            PurchaseScreenFragment shippingFragment = new PurchaseScreenFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, shippingFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        else {
            Toast.makeText(getApplicationContext(), "Add Something to the Cart", Toast.LENGTH_SHORT).show();
        }
        //getSupportFragmentManager().beginTransaction()
        //        .add(R.id.fragment_container, shippingFragment).commit();

    }

    public void enterShipping (View v){


        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, MyApplication.PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyApplication.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                String address = (String) place.getAddress();
                shippingAddress = (TextView)findViewById(R.id.shipping);
                shippingAddress.setText(address);
                shippingAddress.setTextColor(Color.WHITE);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("MyActivity", status.getStatusMessage());

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

    public void sendOrder (View v) {

        shippingAddress = (TextView)findViewById(R.id.shipping);
        fullName = (EditText)findViewById(R.id.first_name);
        suiteApt = (EditText)findViewById(R.id.suiteApt);


        Log.i("MyActivity", "Shipping: " + shippingAddress.getText().toString());

        if (fullName.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Please Enter The Shipping Name", Toast.LENGTH_SHORT).show();
        }

        else if (shippingAddress.getText().toString().equals("Shipping Address")){
            Toast.makeText(getApplicationContext(), "Please Enter Shipping Address", Toast.LENGTH_SHORT).show();
        }

        else {
            String timeStamp = new SimpleDateFormat("MM/dd/yyyy").format(new Date());

            sendOrder.put("fullName", fullName.getText().toString());
            sendOrder.put("address", shippingAddress.getText().toString());
            sendOrder.put("suiteApt", suiteApt.getText().toString());
            sendOrder.put("OS", "Android");
            sendOrder.put("amount", String.valueOf(totalPrice));
            sendOrder.put("beadCount", String.valueOf(beadCount));
            sendOrder.put("kandiCount", String.valueOf(kandiCount));
            sendOrder.put("date", timeStamp);

            final EditText input = new EditText(getApplicationContext());
            input.setHint("(Enter Email Here)");
            input.setTextColor(Color.BLUE);
            input.setHintTextColor(Color.BLACK);
            input.setIncludeFontPadding(true);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("(Optional) Receipt?")
                    .setMessage("Do you want us to email you a receipt and confirmation order?")
                    .setView(input)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (input.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please enter an email", Toast.LENGTH_SHORT).show();
                            }

                            else{
                                sendOrder.put("email", input.getText().toString());


                                PaymentRequest paymentRequest = new PaymentRequest()
                                        .clientToken(tokenToUse);
                                startActivityForResult(paymentRequest.getIntent(getApplicationContext()), MyApplication.REQUEST_NONCE);
                            }
                        }

                    })
                    .setNegativeButton("No Receipt", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            sendOrder.put("email", "gsbllc2011@gmail.com");

                            PaymentRequest paymentRequest = new PaymentRequest()
//
                                    .clientToken(tokenToUse);
                            startActivityForResult(paymentRequest.getIntent(getApplicationContext()), MyApplication.REQUEST_NONCE);
                        }

                    })
                    .show();
        }

    }

    void postNonceToServer(String nonce) {
        Log.i("MyActivity", "Posting Nonce to Server");

        final RequestParams params = new RequestParams();
        params.put("payment_method_nonce", nonce);

        final ProgressDialog dialog = new ProgressDialog(OrderRavoreActivity.this);
        dialog.setMessage("Processing");
        dialog.show();


        client.post(herokuToUse + "/checkout?payment_method_nonce=" + nonce + "&email=" + sendOrder.get("email") + "&amount=" + OrderRavoreActivity.totalPrice + "&devProd=" + MyApplication.devStatus,
                new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("MyActivity", "In Failure");

                        Log.i("MyActivity", "Failure response: " + responseString);
                        dialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Did not go through. Try again.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        String timeStamp = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
                        String orderNumber = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
                        Orders newOrder = new Orders(kandiCount, beadCount, subTotalPrice, shippingPrice, totalPrice, orderNumber, "Android", shippingAddress.getText().toString(), timeStamp, sendOrder.get("email"), fullName.getText().toString(), suiteApt.getText().toString(), "Processing", MyApplication.android_id);

                        sendOrderToFirebase.push().setValue(newOrder);
                        Log.i("MyActivity", "Success Posting nonce");
                        dialog.dismiss();

                        if (MyApplication.cameFromLogin){
                            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                            startActivity(intent);
                            MyApplication.cameFromLogin = false;
                        }

                        Log.i("MyActivity", "Result: " + responseString);

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
