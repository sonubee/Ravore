package gllc.ravore.app.Favorites;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gllc.ravore.app.Main.LoginActivity;
import gllc.ravore.app.Objects.DJs;
import gllc.ravore.app.R;

public class FavoriteDJAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    private LayoutInflater layoutInflater;
    private Context context;
    ViewHolder holder = new ViewHolder();
    Firebase newFavValue;
    ArrayList<Switch> allSwitches;

    public FavoriteDJAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;

        allSwitches = new ArrayList<>();

    }

    @Override
    public int getCount() {
        return LoginActivity.allDjsArray.size();
    }

    @Override
    public Object getItem(int position) {

        return LoginActivity.allDjsArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        DJs dj = LoginActivity.allDjsArray.get(position);

        convertView = layoutInflater.inflate(R.layout.djswitch_layout, null);

        holder.djName = (TextView) convertView.findViewById(R.id.djName_textView);
        holder.aSwitch = (Switch) convertView.findViewById(R.id.dj_switch);
        holder.picture = (ImageView) convertView.findViewById(R.id.dj_image);


        Picasso.with(context).load(dj.getImageUrl()).into(holder.picture);
        holder.djName.setText(dj.getDjName());

        holder.aSwitch.setOnCheckedChangeListener(this);
        holder.aSwitch.setId(position);
        holder.aSwitch.setChecked(LoginActivity.favDjs.contains(dj.getDjName()));

        return convertView;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.i("MyActivity", "View ID: " + buttonView.getId());
        Log.i("MyActivity", "isChecked: " + isChecked);

        newFavValue = new Firebase(LoginActivity.useFirebase+"Users/Favorites/"+LoginActivity.android_id);
        newFavValue.child(LoginActivity.allDjsArray.get(buttonView.getId()).getDjName()).setValue(isChecked);

    }

    static class ViewHolder {
        TextView djName;
        Switch aSwitch;
        ImageView picture;
    }
}
