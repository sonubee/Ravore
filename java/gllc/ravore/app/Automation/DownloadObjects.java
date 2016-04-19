package gllc.ravore.app.Automation;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import gllc.ravore.app.Interfaces.GoToMainActivity;
import gllc.ravore.app.Messaging.ListAllMessagesAdapter;
import gllc.ravore.app.Messaging.MessagingActivity;
import gllc.ravore.app.Messaging.MessagingFragment;
import gllc.ravore.app.Messaging.ShowAllMessagesFragment;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Anon;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.Objects.Orders;
import gllc.ravore.app.Objects.Token;
import gllc.ravore.app.Objects.User;
import gllc.ravore.app.Objects.UserInfo;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 3/30/2016.
 */
public class DownloadObjects {

    GoToMainActivity goToMainActivity;

    public DownloadObjects (final Context context, final GoToMainActivity goToMainActivity){

        this.goToMainActivity = goToMainActivity;

        //Firebase downloadBracelets = new Firebase("https://testravore.firebaseio.com/Bracelets");
        //Query getBracelets = downloadBracelets.orderByChild("braceletId");

        //getBracelets.addChildEventListener(new ChildEventListener() {
        new Firebase(MyApplication.useFirebase+"Bracelets").orderByChild("braceletId").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                Bracelet bracelet = snapshot.getValue(Bracelet.class);

                MyApplication.braceletKey.put(bracelet.getBraceletId(), snapshot.getKey());

                MyApplication.allBracelets.add(bracelet);
                if (bracelet.getGiverId().equals(MyApplication.android_id) || bracelet.getReceiverId().equals(MyApplication.android_id)) {
                    MyApplication.allGivenAndReceivedBraceletsObjects.add(bracelet);
                }

                if (!MyApplication.isAlreadyUser) {
                    if (bracelet.getGiverId().equals(MyApplication.android_id) || bracelet.getReceiverId().equals(MyApplication.android_id)) {
                        goToMainActivity.GoToMain();
                        MyApplication.isAlreadyUser = true;

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Bracelet bracelet = dataSnapshot.getValue(Bracelet.class);

                try {
                    for (int i = 0; i < MyApplication.allBracelets.size(); i++) {
                        if (bracelet.getBraceletId().equals(MyApplication.allBracelets.get(i).getBraceletId())) {

                            boolean foundInAdapter = false;
                            Log.i("ChildChanged", "Child Changed from Bracelets");
                            Log.i("ChildChanged", "Bracelet ID: " + bracelet.getBraceletId());

                            try {
                                for (int j = 0; j < ListAllMessagesAdapter.braceletsAdapter.size(); j++) {
                                    if (bracelet.getBraceletId().equals(ListAllMessagesAdapter.braceletsAdapter.get(j).getBraceletId())) {
                                        Log.i("ChildChanged", "Found Inside Adapter");
                                        ListAllMessagesAdapter.braceletsAdapter.set(j, bracelet);
                                        Log.i("ChildChanged", "After Setting Bracelet");
                                        ShowAllMessagesFragment.adapterAllMessages.notifyDataSetChanged();
                                        foundInAdapter = true;
                                        Log.i("ChildChanged", "After Adapter");
                                    }
                                }
                            } catch (Exception e) {
                                Log.i("ChildChanged", "ListAllMessages Adapter Not Found - Probably Trying to Access Adapter from Login Screen");
                            }

                            Log.i("ChildChanged", "After Searching Adapter");

                            if (!foundInAdapter) {
                                if (bracelet.getGiverId().equals(MyApplication.android_id)) {
                                    MyApplication.allGivenAndReceivedBraceletsObjects.add(bracelet);
                                    ListAllMessagesAdapter.braceletsAdapter.add(bracelet);
                                    ShowAllMessagesFragment.adapterAllMessages.notifyDataSetChanged();
                                    Log.i("ChildChanged", "Adding Giver from Child Changed");
                                }
                                if (bracelet.getReceiverId().equals(MyApplication.android_id)) {
                                    MyApplication.allGivenAndReceivedBraceletsObjects.add(bracelet);
                                    ListAllMessagesAdapter.braceletsAdapter.add(bracelet);
                                    ShowAllMessagesFragment.adapterAllMessages.notifyDataSetChanged();
                                    Log.i("ChildChanged", "Adding Receiver from Child Changed");
                                }
                            }
                        }
                    }

                    if (MyApplication.selectedId.equals(bracelet.getBraceletId())) {
                        if (MyApplication.android_id.equals(bracelet.getGiverId())) {
                            for (int i = 0; i < MyApplication.allAnon.size(); i++) {
                                if (MyApplication.allAnon.get(i).getUserId().equals(bracelet.getReceiverId())) {
                                    Picasso.with(MessagingActivity.context).load(MyApplication.allAnon.get(i).getUrl()).placeholder(R.drawable.anon).into(MessagingFragment.receiverImage);
                                    MessagingFragment.braceletForMessaging = bracelet;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.i("ChildChanged", "In Exception for Child Changed for Bracelet: " + e.getLocalizedMessage());
                }

                for (int i = 0; i < MyApplication.allBracelets.size(); i++) {
                    if (bracelet.getBraceletId().equals(MyApplication.allBracelets.get(i).getBraceletId())) {
                        MyApplication.allBracelets.set(i, bracelet);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i("MyActivity", "Snapshot Key: " + dataSnapshot.getKey());
                Log.i("MyActivity", "Snapshot Value: " + dataSnapshot.getValue());
                Bracelet bracelet = dataSnapshot.getValue(Bracelet.class);

                for (int i = 0; i < MyApplication.allBracelets.size(); i++) {
                    if (bracelet.getBraceletId().equals(MyApplication.allBracelets.get(i).getBraceletId())) {
                        boolean foundInAdapter = false;

                        for (int j = 0; j < ListAllMessagesAdapter.braceletsAdapter.size(); j++) {
                            if (bracelet.getBraceletId().equals(ListAllMessagesAdapter.braceletsAdapter.get(j).getBraceletId())) {
                                ListAllMessagesAdapter.braceletsAdapter.remove(j);
                                ShowAllMessagesFragment.adapterAllMessages.notifyDataSetChanged();
                                foundInAdapter = true;
                            }
                        }

                        for (int k = 0; k < MyApplication.allGivenAndReceivedBraceletsObjects.size(); k++) {
                            if (bracelet.getBraceletId().equals(MyApplication.allGivenAndReceivedBraceletsObjects.get(k).getBraceletId())) {
                                MyApplication.allGivenAndReceivedBraceletsObjects.remove(k);
                            }
                        }

                        MyApplication.allBracelets.remove(i);
                    }
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
            // ....
        });

        //Firebase downloadAnonProfilePics = new Firebase(MyApplication.useFirebase+"Users/ProfilePics");
        //Query getProfilePics = downloadAnonProfilePics.orderByChild("url");

        new Firebase(MyApplication.useFirebase+"Users/ProfilePics").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Anon anonUser = snapshot.getValue(Anon.class);
                if (anonUser.getUserId() != null) {
                    MyApplication.allAnon.add(anonUser);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Anon anonUser = dataSnapshot.getValue(Anon.class);
                boolean changeImage = false;

                String url = "";

                for (int i = 0; i < MyApplication.allAnon.size(); i++) {

                    if (anonUser.getUserId().equals(MyApplication.allAnon.get(i).getUserId())) {
                        MyApplication.allAnon.set(i, anonUser);
                        try {
                            if (!anonUser.getUserId().equals(MyApplication.android_id) && ((MyApplication.selectedBracelet.getGiverId().equals(anonUser.getUserId())) || (MyApplication.selectedBracelet.getReceiverId().equals(anonUser.getUserId())))) {
                                url = MyApplication.cloudinary.url().format("jpg")
                                        .generate("v" + anonUser.getUrlVersion() + "/" + anonUser.getUserId());
                                changeImage = true;
                            }
                        } catch (Exception e) {
                            Log.i("MyActivity", "---Reached Exception at Login Screen for not having selected bracelet---");
                        }

                    }
                }
                if (changeImage) {
                    if (!anonUser.getUserId().equals(MyApplication.android_id) && ((MyApplication.selectedBracelet.getGiverId().equals(anonUser.getUserId())) || (MyApplication.selectedBracelet.getReceiverId().equals(anonUser.getUserId())))) {
                        if (MyApplication.currentUserIsGiver) {
                            Picasso.with(MessagingActivity.context).load(url).placeholder(R.drawable.anon).into(MessagingFragment.receiverImage);
                        }

                        if (!MyApplication.currentUserIsGiver) {
                            Picasso.with(MessagingActivity.context).load(url).placeholder(R.drawable.anon).into(MessagingFragment.giverImage);
                        }
                    }
                }
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

        //Firebase getAllUsers = new Firebase(MyApplication.useFirebase+"Users/AllUsers");
        new Firebase(MyApplication.useFirebase+"Users/AllUsers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserInfo aUser = dataSnapshot.getValue(UserInfo.class);
                MyApplication.allUsers.add(aUser);

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
        });

        //Firebase downloadOrders = new Firebase(MyApplication.useFirebase+"Orders");
        new Firebase(MyApplication.useFirebase+"Orders1").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Orders orders = dataSnapshot.getValue(Orders.class);
                if (orders.getDeviceId().equals(MyApplication.android_id)) {
                    MyApplication.allOrders.add(orders);
                }

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
        });

        new Firebase(MyApplication.useFirebase+"Users/PushToken").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Token newToken = dataSnapshot.getValue(Token.class);
                MyApplication.allTokens.add(newToken);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Token changedToken = dataSnapshot.getValue(Token.class);

                for (int i = 0; i < MyApplication.allTokens.size(); i++) {
                    if (MyApplication.allTokens.get(i).getUserId().equals(changedToken.getUserId())) {
                        MyApplication.allTokens.set(i, changedToken);
                    }
                }
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
        });
    }

}
