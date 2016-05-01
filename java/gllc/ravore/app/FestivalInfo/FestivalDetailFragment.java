package gllc.ravore.app.FestivalInfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import gllc.ravore.app.Messaging.ShowAllMessagesFragment;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 4/29/2016.
 */
public class FestivalDetailFragment extends Fragment {

    TextView dateFestival, locationFestival, priceFestival, webFestival, campingFestival, nameFestival, googleImagesFestival;
    ImageView imageFestival;

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
        webFestival = (TextView)view.findViewById(R.id.webFestival);
        campingFestival = (TextView)view.findViewById(R.id.campingFestival);
        nameFestival = (TextView)view.findViewById(R.id.nameFestival);
        imageFestival = (ImageView)view.findViewById(R.id.imageFestival);
        googleImagesFestival = (TextView)view.findViewById(R.id.googleImagesFestival);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dateFestival.setText(MyApplication.pickedFestival.getDate());
        locationFestival.setText(MyApplication.pickedFestival.getLocation());
        priceFestival.setText(MyApplication.pickedFestival.getPrice());
        webFestival.setText("Click Here for Website");
        campingFestival.setText("Camping: " + MyApplication.pickedFestival.getCamping());
        nameFestival.setText(MyApplication.pickedFestival.getName());

        Picasso.with(getContext()).load(MyApplication.pickedFestival.getImageUrl()).into(imageFestival);

        webFestival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MyApplication.pickedFestival.getWebsite())));
            }
        });

        googleImagesFestival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempString = MyApplication.pickedFestival.getName().replace(' ', '+');
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + tempString + "&tbm=isch")));
            }
        });

        GridView gridview = (GridView) getActivity().findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(getContext()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();

                switch (position) {

                    case 0:
                        String tempString = MyApplication.pickedFestival.getName().replace(' ', '+');
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + tempString + "&tbm=isch")));
                        break;

                    case 3:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MyApplication.pickedFestival.getWebsite())));
                        break;
                }
            }
        });

    }
}
