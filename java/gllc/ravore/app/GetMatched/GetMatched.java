package gllc.ravore.app.GetMatched;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import gllc.ravore.app.Automation.GetDateTimeInstance;
import gllc.ravore.app.Messaging.MessagingActivity;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Match;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 5/6/2016.
 */
public class GetMatched extends AppCompatActivity {

    ImageView otherPersonImage;
    Button yes, no;

    int tempNum = 1;
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

                new Firebase(MyApplication.useFirebase).child("UserInfo").child(MyApplication.allAnon.get(tempNum).getUserId()).child("Matching").child(MyApplication.pickedFestival.getName()).child(MyApplication.android_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i("--AllGetMatched", "Key: " + dataSnapshot.getKey());
                        Log.i("--AllGetMatched", "Value: " + dataSnapshot.getValue());

                        if (!(dataSnapshot.getValue() == null) && dataSnapshot.getValue().equals(true)) {
                            Toast.makeText(getBaseContext(), "Match!!!", Toast.LENGTH_SHORT).show();

                            Match newMatch = new Match(MyApplication.android_id, MyApplication.allAnon.get(tempNum).getUserId(), MyApplication.pickedFestival.getName(), GetDateTimeInstance.getRegDate());

                            new Firebase(MyApplication.useFirebase).child("UserInfo").child(MyApplication.android_id).child("Matches").push().setValue(newMatch);

                            MyApplication.selectedId = "1230";
                            startActivity(new Intent(getBaseContext(), MessagingActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

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
