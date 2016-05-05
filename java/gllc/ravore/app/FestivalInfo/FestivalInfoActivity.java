package gllc.ravore.app.FestivalInfo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import gllc.ravore.app.Interfaces.LoadMap;
import gllc.ravore.app.Main.Feedback;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 4/29/2016.
 */

public class FestivalInfoActivity extends AppCompatActivity implements LoadMap {

    FestivalDetailFragment festivalDetailFragment;
    MapFragment mapFragment;
    private GoogleMap mMap;
    Feedback feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout_festival_info);

        festivalDetailFragment = new FestivalDetailFragment();
        mapFragment = new MapFragment();
        feedback = new Feedback();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_festival_info, festivalDetailFragment).commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadMap(){
        Log.i("--AllFIActivity", "Loaded Interface");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, feedback).commit();

    }
}
