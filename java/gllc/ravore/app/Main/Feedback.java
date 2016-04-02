package gllc.ravore.app.Main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import gllc.ravore.app.Automation.GetDateTimeInstance;
import gllc.ravore.app.Main.LoginActivity;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 2/8/2016.
 */
public class Feedback extends Fragment {

    Button feedbackButton;
    EditText feedbackMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback, container, false);
        feedbackButton = (Button)view.findViewById(R.id.send_feedback_button);
        feedbackMessage = (EditText)view.findViewById(R.id.feedback_message);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!feedbackMessage.getText().toString().equals("")) {
                    Map<String, String> uploadFeedback = new HashMap<>();
                    uploadFeedback.put("timestamp", GetDateTimeInstance.getTimeStamp());
                    uploadFeedback.put("userId", MyApplication.android_id);
                    uploadFeedback.put("feedback", feedbackMessage.getText().toString());

                    new Firebase(MyApplication.useFirebase+"Feedback").push().setValue(uploadFeedback);

                    feedbackMessage.setText("");

                    Toast.makeText(getContext(), "Feedback Sent!", Toast.LENGTH_SHORT).show();
                }
                else {Toast.makeText(getContext(), "Nothing Entered!", Toast.LENGTH_SHORT);}
            }
        });
    }


}
