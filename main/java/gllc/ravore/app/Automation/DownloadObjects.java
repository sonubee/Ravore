package gllc.ravore.app.Automation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.squareup.picasso.Picasso;

import gllc.ravore.app.Interfaces.GoToMainActivity;
import gllc.ravore.app.Main.LoginActivity;
import gllc.ravore.app.Main.MainActivity;
import gllc.ravore.app.Messaging.ListAllMessagesAdapter;
import gllc.ravore.app.Messaging.MessagingActivity;
import gllc.ravore.app.Messaging.ShowAllMessages;
import gllc.ravore.app.Objects.Anon;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.Objects.Orders;
import gllc.ravore.app.Objects.Token;
import gllc.ravore.app.Objects.UserInfo;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 3/30/2016.
 */
public class DownloadObjects {

    GoToMainActivity goToMainActivity;

    public DownloadObjects (Context context, final GoToMainActivity goToMainActivity){

        this.goToMainActivity = goToMainActivity;

        Firebase downloadBracelets = new Firebase(LoginActivity.useFirebase+"Bracelets");
        Query getBracelets = downloadBracelets.orderByChild("braceletId");

        getBracelets.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                Bracelet bracelet = snapshot.getValue(Bracelet.class);

                LoginActivity.braceletKey.put(bracelet.getBraceletId(), snapshot.getKey());

                LoginActivity.allBracelets.add(bracelet);
                if (bracelet.getGiverId().equals(LoginActivity.android_id) || bracelet.getReceiverId().equals(LoginActivity.android_id)) {
                    LoginActivity.allGivenAndReceivedBraceletsObjects.add(bracelet);
                }

                if (!LoginActivity.isAlreadyUser) {
                    if (bracelet.getGiverId().equals(LoginActivity.android_id) || bracelet.getReceiverId().equals(LoginActivity.android_id)) {
                        goToMainActivity.GoToMain();
                        LoginActivity.isAlreadyUser = true;

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Bracelet bracelet = dataSnapshot.getValue(Bracelet.class);

                try {
                    for (int i = 0; i < LoginActivity.allBracelets.size(); i++) {
                        if (bracelet.getBraceletId().equals(LoginActivity.allBracelets.get(i).getBraceletId())) {

                            boolean foundInAdapter = false;
                            Log.i("ChildChanged", "Child Changed from Bracelets");
                            Log.i("ChildChanged", "Bracelet ID: " + bracelet.getBraceletId());

                            try {
                                for (int j = 0; j < ListAllMessagesAdapter.braceletsAdapter.size(); j++) {
                                    if (bracelet.getBraceletId().equals(ListAllMessagesAdapter.braceletsAdapter.get(j).getBraceletId())) {
                                        Log.i("ChildChanged", "Found Inside Adapter");
                                        ListAllMessagesAdapter.braceletsAdapter.set(j, bracelet);
                                        Log.i("ChildChanged", "After Setting Bracelet");
                                        ShowAllMessages.adapterAllMessages.notifyDataSetChanged();
                                        foundInAdapter = true;
                                        Log.i("ChildChanged", "After Adapter");
                                    }
                                }
                            } catch (Exception e){
                                Log.i("ChildChanged", "ListAllMessages Adapter Not Found - Probably Trying to Access Adapter from Login Screen");
                            }

                            Log.i("ChildChanged", "After Searching Adapter");

                            if (!foundInAdapter) {
                                if (bracelet.getGiverId().equals(LoginActivity.android_id)) {
                                    LoginActivity.allGivenAndReceivedBraceletsObjects.add(bracelet);
                                    ListAllMessagesAdapter.braceletsAdapter.add(bracelet);
                                    ShowAllMessages.adapterAllMessages.notifyDataSetChanged();
                                    Log.i("ChildChanged", "Adding Giver from Child Changed");
                                }
                                if (bracelet.getReceiverId().equals(LoginActivity.android_id)) {
                                    LoginActivity.allGivenAndReceivedBraceletsObjects.add(bracelet);
                                    ListAllMessagesAdapter.braceletsAdapter.add(bracelet);
                                    ShowAllMessages.adapterAllMessages.notifyDataSetChanged();
                                    Log.i("ChildChanged", "Adding Receiver from Child Changed");
                                }
                            }
                        }
                    }

                    if (LoginActivity.selectedId.equals(bracelet.getBraceletId())) {
                        if (LoginActivity.android_id.equals(bracelet.getGiverId())) {
                            for (int i = 0; i < LoginActivity.allAnon.size(); i++) {
                                if (LoginActivity.allAnon.get(i).getUserId().equals(bracelet.getReceiverId())) {
                                    Picasso.with(MessagingActivity.context).load(LoginActivity.allAnon.get(i).getUrl()).placeholder(R.drawable.anon).into(MessagingActivity.receiverImage);
                                    MessagingActivity.selectedBraceletFromLogin = bracelet;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.i("ChildChanged", "In Exception for Child Changed for Bracelet: " + e.getLocalizedMessage());
                }

                for (int i = 0; i < LoginActivity.allBracelets.size(); i++) {
                    if (bracelet.getBraceletId().equals(LoginActivity.allBracelets.get(i).getBraceletId())) {
                        LoginActivity.allBracelets.set(i, bracelet);
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

        Firebase downloadAnonProfilePics = new Firebase(LoginActivity.useFirebase+"Users/ProfilePics");
        //Query getProfilePics = downloadAnonProfilePics.orderByChild("url");

        downloadAnonProfilePics.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Anon anonUser = snapshot.getValue(Anon.class);
                Log.i("MyActivity", "User ID: " + anonUser.getUserId());
                Log.i("MyActivity", "URL: " + anonUser.getUrl());
                if (anonUser.getUserId() != null) {
                    LoginActivity.allAnon.add(anonUser);
                    //Log.i("MyActivity", "Loaded: " + anonUser.getFullPhotoVersion());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Anon anonUser = dataSnapshot.getValue(Anon.class);
                boolean changeImage = false;

                String url = "";
                Log.i("MyActivity", "New Data: " + dataSnapshot.getValue());

                for (int i = 0; i < LoginActivity.allAnon.size(); i++) {

                    Log.i("MyActivity", "Anon ID: " + LoginActivity.allAnon.get(i).getUserId());

                    if (anonUser.getUserId().equals(LoginActivity.allAnon.get(i).getUserId())) {

                        Log.i("MyActivity", "User ID Matched: " + LoginActivity.allAnon.get(i).getUserId());

                        Log.i("MyActivity", "Array Anon URL: " + LoginActivity.allAnon.get(i).getUrl());
                        Log.i("MyActivity", "Updated URL: " + anonUser.getUrl());

                        LoginActivity.allAnon.set(i, anonUser);
                        try {
                            if (!anonUser.getUserId().equals(LoginActivity.android_id) && ((LoginActivity.selectedBracelet.getGiverId().equals(anonUser.getUserId())) || (LoginActivity.selectedBracelet.getReceiverId().equals(anonUser.getUserId())))) {
/*
                                url = MainActivity.cloudinary.url().format("jpg")
                                        .transformation(new Transformation().width(70).height(70).crop("fill"))
                                        .generate("v" + anonUser.getUrlVersion() + "/" + anonUser.getUserId());
*/
                                url = MainActivity.cloudinary.url().format("jpg")
                                        .generate("v" + anonUser.getUrlVersion() + "/" + anonUser.getUserId());
                                changeImage = true;
                            }
                        } catch (Exception e) {
                            Log.i("MyActivity", "Reached Exception at Login Screen for not having selected bracelet");
                        }

                    }
                }
                if (changeImage) {
                    //ShowAllMessages.adapterAllMessages.notifyDataSetChanged();
                    Log.i("MyActivity", "Got into change image");
                    if (!anonUser.getUserId().equals(LoginActivity.android_id) && ((LoginActivity.selectedBracelet.getGiverId().equals(anonUser.getUserId())) || (LoginActivity.selectedBracelet.getReceiverId().equals(anonUser.getUserId())))) {
                        if (LoginActivity.currentUserIsGiver) {
                            Picasso.with(MessagingActivity.context).load(url).placeholder(R.drawable.anon).into(MessagingActivity.receiverImage);
                        }

                        if (!LoginActivity.currentUserIsGiver) {
                            Picasso.with(MessagingActivity.context).load(url).placeholder(R.drawable.anon).into(MessagingActivity.giverImage);
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

        Firebase getAllUsers = new Firebase(LoginActivity.useFirebase+"Users/AllUsers");
        getAllUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserInfo aUser = dataSnapshot.getValue(UserInfo.class);
                LoginActivity.allUsers.add(aUser);

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

        Firebase downloadOrders;

        if (LoginActivity.devStatus.equals("sandbox")){
            downloadOrders = new Firebase(LoginActivity.useFirebase+"OrdersTest");}
        else {
            downloadOrders = new Firebase(LoginActivity.useFirebase+"Orders");}

        downloadOrders.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Orders orders = dataSnapshot.getValue(Orders.class);
                Log.i("MyActivity", "Order Child Add");

                Log.i("MyActivity", "Order1: " + orders.getDeviceId());
                Log.i("MyActivity", "Order2: " + LoginActivity.android_id);

                if (orders.getDeviceId().equals(LoginActivity.android_id)) {
                    LoginActivity.allOrders.add(orders);
                    Log.i("MyActivity", "Added Order: " + orders.getDeviceId());
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

        Firebase getTokens = new Firebase(LoginActivity.useFirebase+"Users/PushToken");
        getTokens.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Token newToken = dataSnapshot.getValue(Token.class);
                LoginActivity.allTokens.add(newToken);

                Log.i("MyActivity", "Added: " + newToken.getToken());

                //gcmTokens.put(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
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
    }
}
