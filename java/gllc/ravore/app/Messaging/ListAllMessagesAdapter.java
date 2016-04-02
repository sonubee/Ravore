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
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.Objects.Message;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 2/14/2016.
 */
public class ListAllMessagesAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    ViewHolder holder = new ViewHolder();
    public static ArrayList<Bracelet> braceletsAdapter;

    public ListAllMessagesAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        braceletsAdapter = (ArrayList<Bracelet>)MyApplication.allGivenAndReceivedBraceletsObjects.clone();

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

            if (MyApplication.android_id.equals(braceletsAdapter.get(position).getReceiverId())){
                if (braceletsAdapter.get(position).getGiverId().equals("NA")){
                    holder.picture.setImageResource(R.drawable.anon);}
                else {
                    for (int j=0; j < MyApplication.allAnon.size(); j++){
                        if ((MyApplication.allAnon.get(j).getUserId().equals(braceletsAdapter.get(position).getGiverId()))){
                            Picasso.with(context).load(MyApplication.allAnon.get(j).getUrl()).into(holder.picture);}}}}

            if (MyApplication.android_id.equals(braceletsAdapter.get(position).getGiverId())){
                if (braceletsAdapter.get(position).getReceiverId().equals("NA")){
                    holder.picture.setImageResource(R.drawable.anon);}
                else {
                    for (int j=0; j < MyApplication.allAnon.size(); j++){
                        if ((MyApplication.allAnon.get(j).getUserId().equals(braceletsAdapter.get(position).getReceiverId()))){
                            Picasso.with(context).load(MyApplication.allAnon.get(j).getUrl()).into(holder.picture);}}}}


        holder.date = (TextView)convertView.findViewById(R.id.textView7);
        String date = "";

        for (int i=0; i<MyApplication.allBracelets.size(); i++){
            if (MyApplication.allBracelets.get(i).getBraceletId().equals(braceletId)){
                date = MyApplication.allBracelets.get(i).getDateCreated();}}

        holder.date.setText(date);
        holder.date.setTextColor(Color.GRAY);

        holder.braceletIdTextview = (TextView)convertView.findViewById(R.id.braceletIdTextView);
        holder.braceletIdTextview.setText("Ravore ID: "+braceletId);
        holder.braceletIdTextview.setTextColor(Color.CYAN);

        holder.lastMessage = (TextView)convertView.findViewById(R.id.lastMessage);
        holder.lastMessage.setVisibility(View.INVISIBLE);

        return convertView;
    }

    static class ViewHolder {
        TextView senderName;
        TextView date;
        TextView lastMessage;
        TextView braceletIdTextview;
        ImageView picture;
    }
}
