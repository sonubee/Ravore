package gllc.ravore.app.Messaging;

import android.content.Context;
import android.graphics.Color;
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
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gllc.ravore.app.Main.LoginActivity;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.Objects.Message;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 2/14/2016.
 */
public class ListAllMessagesAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    private LayoutInflater layoutInflater;
    private Context context;
    ViewHolder holder = new ViewHolder();
    public static ArrayList<Bracelet> braceletsAdapter;

    public ListAllMessagesAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        braceletsAdapter = new ArrayList<>();
        braceletsAdapter.clear();
        //Log.i("MyActivity", "(Before)Size of Bracelets Adapter: " + braceletsAdapter.size());
        Log.i("MyActivity", "ALL GR: " + LoginActivity.allGivenAndReceivedBraceletsObjects.size());
        braceletsAdapter = (ArrayList<Bracelet>)LoginActivity.allGivenAndReceivedBraceletsObjects.clone();
        Log.i("MyActivity", "(After)Size of Bracelets Adapter: " + braceletsAdapter.size());
    }

    @Override
    public int getCount() {
        return braceletsAdapter.size();
    }

    @Override
    public Object getItem(int position) {
        return braceletsAdapter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String braceletId = braceletsAdapter.get(position).getBraceletId();

        convertView = layoutInflater.inflate(R.layout.multiple_messages, null);

        holder.senderName = (TextView) convertView.findViewById(R.id.nameSender);
        holder.senderName.setText("No Ravor Name Yet");
        holder.senderName.setVisibility(View.INVISIBLE);

        holder.picture = (ImageView) convertView.findViewById(R.id.imageOfOther);

        //Log.i("MyActivity", "Size of All G&R Bracelets Objects: " + braceletsAdapter.size());

            if (LoginActivity.android_id.equals(braceletsAdapter.get(position).getReceiverId())){
                if (braceletsAdapter.get(position).getGiverId().equals("NA")){
                    holder.picture.setImageResource(R.drawable.anon);}
                else {
                    for (int j=0; j < LoginActivity.allAnon.size(); j++){
                        if ((LoginActivity.allAnon.get(j).getUserId().equals(braceletsAdapter.get(position).getGiverId()))){
                            Picasso.with(context).load(LoginActivity.allAnon.get(j).getUrl()).into(holder.picture);}}}}

            if (LoginActivity.android_id.equals(braceletsAdapter.get(position).getGiverId())){
                if (braceletsAdapter.get(position).getReceiverId().equals("NA")){
                    Log.i("MyActivity", "ReceiverID: " + braceletsAdapter.get(position).getReceiverId());
                    Log.i("MyActivity", "For Bracelet ID: " + braceletsAdapter.get(position).getBraceletId());
                    holder.picture.setImageResource(R.drawable.anon);}
                else {
                    for (int j=0; j < LoginActivity.allAnon.size(); j++){
                        if ((LoginActivity.allAnon.get(j).getUserId().equals(braceletsAdapter.get(position).getReceiverId()))){
                            Picasso.with(context).load(LoginActivity.allAnon.get(j).getUrl()).into(holder.picture);}}}}

/*
        for (int i=0; i<LoginActivity.allBracelets.size(); i++){
            if ((LoginActivity.allBracelets.get(i).getGiverId().equals(LoginActivity.android_id)) || (LoginActivity.allBracelets.get(i).getReceiverId().equals(LoginActivity.android_id))){
                if (LoginActivity.allBracelets.get(i).getGiverId().equals(LoginActivity.android_id)){
                    for (int j=0; j < LoginActivity.allAnon.size(); j++){
                        if (LoginActivity.allAnon.get(j).getUserId().equals(LoginActivity.allBracelets.get(i).getReceiverId())){
                            Picasso.with(context).load(LoginActivity.allAnon.get(j).getUrl()).into(holder.picture);}}}
                else {
                    for (int j=0; j < LoginActivity.allAnon.size(); j++){
                        if (LoginActivity.allAnon.get(j).getUserId().equals(LoginActivity.allBracelets.get(i).getGiverId())){
                            Picasso.with(context).load(LoginActivity.allAnon.get(j).getUrl()).into(holder.picture);}}}}}
*/

        holder.date = (TextView)convertView.findViewById(R.id.textView7);
        String date = "";

        for (int i=0; i<LoginActivity.allBracelets.size(); i++){
            if (LoginActivity.allBracelets.get(i).getBraceletId().equals(braceletId)){
                date = LoginActivity.allBracelets.get(i).getDateCreated();}}

        holder.date.setText(date);
        holder.date.setTextColor(Color.GRAY);

        holder.braceletIdTextview = (TextView)convertView.findViewById(R.id.braceletIdTextView);
        holder.braceletIdTextview.setText("Ravore ID: "+braceletId);
        holder.braceletIdTextview.setTextColor(Color.CYAN);

        holder.lastMessage = (TextView)convertView.findViewById(R.id.lastMessage);
        holder.lastMessage.setVisibility(View.INVISIBLE);

        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // Get a reference to our posts


    }

    static class ViewHolder {
        TextView senderName;
        TextView date;
        TextView lastMessage;
        TextView braceletIdTextview;
        ImageView picture;
    }
}
