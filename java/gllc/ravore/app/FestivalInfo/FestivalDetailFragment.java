package gllc.ravore.app.FestivalInfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.squareup.picasso.Picasso;

import gllc.ravore.app.GetMatched.GetMatched;
import gllc.ravore.app.Interfaces.LoadMap;
import gllc.ravore.app.Messaging.ShowAllMessagesFragment;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.OrderRavore.OrderRavoreActivity;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 4/29/2016.
 */
public class FestivalDetailFragment extends Fragment {

    TextView dateFestival, locationFestival, priceFestival, campingFestival, nameFestival;
    //ImageView imageFestival;
    LoadMap loadMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.festival_info_fragment, container, false);

        dateFestival = (TextView)view.findViewById(R.id.dateFestival);
        locationFestival = (TextView)view.findViewById(R.id.locationFestival);
        priceFestival = (TextView)view.findViewById(R.id.priceFestival);
        campingFestival = (TextView)view.findViewById(R.id.campingFestival);
        nameFestival = (TextView)view.findViewById(R.id.nameFestival);
        //imageFestival = (ImageView)view.findViewById(R.id.imageFestival);
        loadMap = (LoadMap)getActivity();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new Firebase(MyApplication.useFirebase).child("UserInfo").child(MyApplication.android_id).child("Matching").child(MyApplication.pickedFestival.getName()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MyApplication.allTried.add(dataSnapshot.getKey());
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

        dateFestival.setText(MyApplication.pickedFestival.getDate());
        locationFestival.setText(MyApplication.pickedFestival.getLocation());
        priceFestival.setText(MyApplication.pickedFestival.getPrice());
        campingFestival.setText("Camping: " + MyApplication.pickedFestival.getCamping());
        nameFestival.setText(MyApplication.pickedFestival.getName());

        //Picasso.with(getContext()).load(MyApplication.pickedFestival.getImageUrl()).into(imageFestival);

        GridView gridview = (GridView) getActivity().findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(getContext()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();

                switch (position) {

                    case 0:
                        String tempString = MyApplication.pickedFestival.getName().replace(' ', '+');
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + tempString + "&tbm=isch")));
                        break;

                    case 1:
                        //OPEN MAP FRAGMENT HERE
                        loadMap.loadMap();
                        break;

                    case 2:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MyApplication.pickedFestival.getTicketsSite())));
                        break;

                    case 3:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MyApplication.pickedFestival.getWebsite())));
                        break;

                    case 5:
                        Log.i("--AllFDetailFragment", "Clicked Match");

                        if (MyApplication.gender.equals("NA")){
                            Toast.makeText(getContext(), "Please Select Your Gender from the Profile First", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        else {
                            boolean found = false;
                            for (int i=0; i < MyApplication.goingFestivals.size(); i++){
                                if (MyApplication.goingFestivals.get(i).equals(MyApplication.pickedFestival.getName())){
                                    found = true;
                                    Intent intent = new Intent(getContext(), GetMatched.class);
                                    startActivity(intent);
                                }
                            }

                            if (!found){Toast.makeText(getContext(), "You Need To Attend the Festival To Get Matched", Toast.LENGTH_SHORT).show();}
                            break;
                        }


                }
            }
        });

    }
}
