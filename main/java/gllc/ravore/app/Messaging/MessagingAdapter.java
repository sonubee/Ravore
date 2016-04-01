package gllc.ravore.app.Messaging;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;

import gllc.ravore.app.Main.LoginActivity;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.Objects.Message;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 2/3/2016.
 */
public class MessagingAdapter extends ArrayAdapter<Message> {

    Context context;
    public static ArrayList<Message> messageArrayList;
    public static Firebase pullMessages;
    public static ChildEventListener listener1;
    String selectedId = LoginActivity.selectedId;
    Bracelet selectedBraceletFromLogin = new Bracelet();


    public MessagingAdapter(Context context, int textViewResourceId, ArrayList<Message> messages) {
        super(context, textViewResourceId, messages);
        this.context = context;
        messageArrayList = messages;

        pullMessages = new Firebase(LoginActivity.useFirebase+"Messages/"+ LoginActivity.selectedId);
        Query queryRef = pullMessages.orderByKey().limitToLast(100);
        queryRef.addChildEventListener(listener1 =  new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                Message message = snapshot.getValue(Message.class);
                messageArrayList.add(message);
                notifyDataSetChanged();

                MessagingActivity.listView.post(new Runnable() {
                    @Override
                    public void run() {
                        // Select the last row so it will scroll into view...
                        MessagingActivity.listView.setSelection(MessagingActivity.listView.getCount() - 1);
                    }
                });
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
            // ....
        });

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.message_text_layout, parent, false);

        for (int i=0; i<LoginActivity.allBracelets.size(); i++){
            if (selectedId.equals(LoginActivity.allBracelets.get(i).getBraceletId())){
                selectedBraceletFromLogin = LoginActivity.allBracelets.get(i);
            }
        }

        TextView textView = (TextView) rowView.findViewById(R.id.message_text);
        String message = messageArrayList.get(position).getMessage();

        if (messageArrayList.get(position).getSender().equals(selectedBraceletFromLogin.getReceiverId())){

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)textView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            textView.setLayoutParams(params);}

        textView.setText(message);

        if (messageArrayList.get(position).getSender().equals(selectedBraceletFromLogin.getGiverId())) {textView.setTextColor(Color.GREEN);}
        else {textView.setTextColor(Color.CYAN);}

        textView.setTextSize(15);



        return rowView;
    }
}
