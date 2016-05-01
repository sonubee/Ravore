package gllc.ravore.app.FestivalInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gllc.ravore.app.MyApplication;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 4/29/2016.
 */
public class FestivalInfoAdapter extends BaseAdapter{

    ArrayList<String> festvials = new ArrayList<>();
    private LayoutInflater layoutInflater;
    ViewHolder holder = new ViewHolder();
    Context context;

    public FestivalInfoAdapter(Context context){
        festvials.add("EDC");
        festvials.add("Lightning in a Bottle");
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return MyApplication.allEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return MyApplication.allEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.event_list_row_layout, null);

        holder.festivalName = (TextView)convertView.findViewById(R.id.festivalName);
        holder.festivalDate = (TextView)convertView.findViewById(R.id.festivalDate);
        holder.festivalLocation = (TextView)convertView.findViewById(R.id.festivalLocation);
        holder.festivalImage = (ImageView)convertView.findViewById(R.id.festivalImage);

        holder.festivalName.setText(MyApplication.allEvents.get(position).getName());
        holder.festivalDate.setText(MyApplication.allEvents.get(position).getDate());
        holder.festivalLocation.setText(MyApplication.allEvents.get(position).getLocation());

        Picasso.with(context).load(MyApplication.allEvents.get(position).getImageUrl()).into(holder.festivalImage);



        return convertView;
    }

    static class ViewHolder {
        TextView festivalName;
        TextView festivalDate;
        TextView festivalLocation;
        ImageView festivalImage;
    }
}
