package gllc.ravore.app.GetMatched;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;

import gllc.ravore.app.Messaging.MessagingFragment;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.Objects.Message;

/**
 * Created by bhangoo on 5/17/2016.
 */
public class MatchedMessagingAdapter extends ArrayAdapter<MatchedMessaging> {

    Context context;
    public static ArrayList<MatchedMessaging> messageArrayList;
    public static Firebase pullMessages;
    public static ChildEventListener firebaseChildListenerMessages;

    public MatchedMessagingAdapter(Context context, int textViewResourceId, ArrayList<MatchedMessaging> messages) {
        super(context, textViewResourceId, messages);
        this.context = context;
        messageArrayList = messages;

        pullMessages = new Firebase(MyApplication.useFirebase).child("UserInfo").child("Matches").child(pushKey);
        Query queryRef = pullMessages.orderByKey().limitToLast(100);
        queryRef.addChildEventListener(firebaseChildListenerMessages = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                MatchedMessaging message = snapshot.getValue(MatchedMessaging.class);
                messageArrayList.add(message);
                notifyDataSetChanged();

                MessagingFragment.listView.post(new Runnable() {
                    @Override
                    public void run() {
                        // Select the last row so it will scroll into view...
                        MessagingFragment.listView.setSelection(MessagingFragment.listView.getCount() - 1);
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
}
