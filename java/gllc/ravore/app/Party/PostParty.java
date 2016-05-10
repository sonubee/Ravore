package gllc.ravore.app.Party;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Party;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 5/9/2016.
 */
public class PostParty extends Fragment {

    EditText enterPartyName, enterLocation, enterDate, enterTime, enterDetails;
    Button postPartyButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.post_party, container, false);

        enterPartyName = (EditText)view.findViewById(R.id.enterPartyName);
        enterLocation = (EditText)view.findViewById(R.id.enterLocation);
        enterDate = (EditText)view.findViewById(R.id.enterDate);
        enterTime = (EditText)view.findViewById(R.id.enterTime);
        enterDetails = (EditText)view.findViewById(R.id.enterDetails);
        postPartyButton = (Button)view.findViewById(R.id.postPartyButton);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        postPartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Party postParty = new Party(enterPartyName.getText().toString(), enterLocation.getText().toString(), enterDate.getText().toString(), enterTime.getText().toString(), enterDetails.getText().toString(), "NA");
                new Firebase(MyApplication.useFirebase).child("Events").child(MyApplication.pickedFestival.getName()).child("Parties").push().setValue(postParty);
            }
        });


    }
}
