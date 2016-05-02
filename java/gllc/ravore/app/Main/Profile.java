package gllc.ravore.app.Main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;

import gllc.ravore.app.Automation.AddBracelet;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 5/1/2016.
 */

public class Profile extends Fragment {

    Context context = getActivity();
    TextView ravorName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile, container, false);

        ravorName = (TextView)view.findViewById(R.id.ravorName);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("gllc.ravore.app.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        String ravorNameString = sharedPref.getString("RavorName", "Ravor Name (Click Here)");

        ravorName.setText(ravorNameString);

        ravorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                input.setHint("Enter Ravor Name Here");
                builder.setTitle("Ravor Name");
                builder.setMessage("Enter the Ravore Name Below!");
                builder.setView(input);
                builder.setPositiveButton("Save Name!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Firebase(MyApplication.useFirebase).child("UserInfo").child(MyApplication.android_id).child("ravorName").setValue(input.getText().toString());

                        Context context = getActivity();
                        SharedPreferences sharedPref = context.getSharedPreferences("gllc.ravore.app.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("RavorName", input.getText().toString());
                        editor.commit();

                        ravorName.setText(input.getText().toString());
                    }
                });

                builder.show();
            }
        });

        //SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPref.edit();
        //editor.putString("Ravor Name", "NA");
        //editor.apply();
    }
}
