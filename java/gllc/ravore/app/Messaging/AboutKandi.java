package gllc.ravore.app.Messaging;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gllc.ravore.app.MyApplication;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 4/15/2016.
 */
public class AboutKandi extends Fragment {

    TextView textView;
    Button saveKandiInfo;
    EditText tellStory , date;
    AutoCompleteTextView whereHappen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tell_story, container, false);
        textView = (TextView)view.findViewById(R.id.kandiIdForStory);
        saveKandiInfo = (Button)view.findViewById(R.id.saveKandiInfo);
        tellStory = (EditText)view.findViewById(R.id.tellStoryEdittext);
        whereHappen = (AutoCompleteTextView)view.findViewById(R.id.whereHappened);
        date = (EditText)view.findViewById(R.id.dateOfKandi);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, MyApplication.allEvents);
        whereHappen.setAdapter(adapter);

        textView.setText("Kandi #" + MyApplication.selectedId);

        saveKandiInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> saveKandiInfo = new HashMap<String, String>();
                saveKandiInfo.put("story", tellStory.getText().toString());
                saveKandiInfo.put("where", whereHappen.getText().toString());
                saveKandiInfo.put("date", date.getText().toString());

                String giverOrReceiver = "";

                if (MyApplication.currentUserIsGiver){giverOrReceiver = "giver";}
                else {giverOrReceiver = "receiver";}

                new Firebase(MyApplication.useFirebase+"KandiInfo").child(MyApplication.selectedId).child(giverOrReceiver).setValue(saveKandiInfo);
                new Firebase(MyApplication.useFirebase).child("IDs").child(MyApplication.selectedId).child("1").child("KandiInfo").child(giverOrReceiver).setValue(saveKandiInfo);
            }
        });
    }
}
