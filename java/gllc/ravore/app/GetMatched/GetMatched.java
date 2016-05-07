package gllc.ravore.app.GetMatched;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gllc.ravore.app.MyApplication;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 5/6/2016.
 */
public class GetMatched extends AppCompatActivity {

    ImageView otherPersonImage;
    Button yes, no;

    int tempNum = 0;
    Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_matched);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);}

        otherPersonImage = (ImageView)findViewById(R.id.otherImageForMatch);
        yes = (Button)findViewById(R.id.yesMatchButton);
        no = (Button)findViewById(R.id.noMatchButton);

        Picasso.with(getBaseContext()).load(MyApplication.allAnon.get(tempNum).getFullPhotoUrl()).placeholder(R.drawable.placeholder).into(otherPersonImage);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Firebase(MyApplication.useFirebase).child("UserInfo").child(MyApplication.android_id).child("Matching").child(MyApplication.pickedFestival.getName()).child(MyApplication.allAnon.get(tempNum).getUserId()).setValue(true);
                tempNum++;

                while (MyApplication.allTried.contains(MyApplication.allAnon.get(tempNum).getUserId())) {
                    tempNum++;
                }

                Picasso.with(getBaseContext()).load(MyApplication.allAnon.get(tempNum).getFullPhotoUrl()).placeholder(R.drawable.placeholder).into(otherPersonImage);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Firebase(MyApplication.useFirebase).child("UserInfo").child(MyApplication.android_id).child("Matching").child(MyApplication.pickedFestival.getName()).child(MyApplication.allAnon.get(tempNum).getUserId()).setValue(false);
                tempNum++;

                while (MyApplication.allTried.contains(MyApplication.allAnon.get(tempNum).getUserId())) {
                    tempNum++;
                }

                Picasso.with(getBaseContext()).load(MyApplication.allAnon.get(tempNum).getFullPhotoUrl()).placeholder(R.drawable.placeholder).into(otherPersonImage);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.preference:

                final CharSequence[] items = {"Male", "Female", "None"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Filter");

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        new Firebase(MyApplication.useFirebase).child("UserInfo").child(MyApplication.android_id).child("Matching").child("Filter").setValue(items[item].toString().toLowerCase());
                    }
                });
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_matching, menu);
        this.menu = menu;
        return true;

        //return super.onCreateOptionsMenu(menu);
    }
}
