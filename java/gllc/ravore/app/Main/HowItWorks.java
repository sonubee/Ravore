package gllc.ravore.app.Main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import gllc.ravore.app.Kandi.OrderRavore;

/**
 * Created by bhangoo on 3/31/2016.
 */
public class HowItWorks {

    public HowItWorks(final Context context, TextView HIW, TextView getRavore, final Context context2){

        HIW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"I Received A Bracelet", "I Want To Give A Bracelet", "No Account Needed?"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context2);
                builder.setTitle("How It Works");

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("I Received A Bracelet")) {
                            showHowToReceive(context2);
                        } else if (items[item].equals("I Want To Give A Bracelet")) {
                            showHowToGive(context2);
                        } else if (items[item].equals("No Account Needed?")) {
                            showNoAccount(context2);
                        }

                    }
                });
                builder.show();
            }
        });

        //getRavore.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Intent intent = new Intent(getBaseContext(), OrderRavore.class);
        //        startActivity(intent);
        //    }
        //});
    }

    public void showHowToReceive(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("How To Receive");
        builder.setMessage("On your Bracelet there is an ID.\n\nEnter that into the Login Screen and click the \"I Got Kandi!\" button.\n\nYou can now chat with the person who gave you the bracelet!");
        builder.show();
    }

    public void showHowToGive(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("How To Give A Bracelet");
        builder.setMessage("On the Bracelet there is an ID.\n\nEnter that into the Login Screen and click the \"Register Kandi\" button.\n\nNow Give that bracelet to someone to chat with them!");
        builder.show();
    }

    public void showNoAccount(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Account Needed");
        builder.setMessage("We use the bracelet to ID so you won't need an account to chat!\n\nHowever, you'll be missing out on what our free account has to offer :) Check us out!");
        builder.show();
    }
}
