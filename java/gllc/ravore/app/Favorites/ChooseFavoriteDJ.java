package gllc.ravore.app.Favorites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

import gllc.ravore.app.Objects.DJs;
import gllc.ravore.app.R;


/**
 * Created by bhangoo on 12/1/2015.
 */
public class ChooseFavoriteDJ extends android.support.v4.app.Fragment {

    ArrayList<Switch> allDJSwitches;
    ArrayList<String> allFavoritesSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allDJSwitches = new ArrayList<>();
        allFavoritesSelected = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_favorite_djs_linear, container, false);
        Log.i("MyActivity", "Create View");
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ListView listView = (ListView)getActivity().findViewById(R.id.dj_favorites_listview);
        listView.setAdapter(new FavoriteDJAdapter(getActivity().getBaseContext()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                DJs dJs = (DJs) listView.getItemAtPosition(position);
                Toast.makeText(getActivity().getBaseContext(), "Selected :" + " " + dJs.getDjName(), Toast.LENGTH_LONG).show();}
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
