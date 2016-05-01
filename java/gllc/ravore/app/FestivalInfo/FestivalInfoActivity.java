package gllc.ravore.app.FestivalInfo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import gllc.ravore.app.R;

/**
 * Created by bhangoo on 4/29/2016.
 */

public class FestivalInfoActivity extends AppCompatActivity {

    FestivalDetailFragment festivalDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout_festival_info);

        festivalDetailFragment = new FestivalDetailFragment();

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
}
