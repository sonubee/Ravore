package gllc.ravore.app.FestivalInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gllc.ravore.app.R;

/**
 * Created by bhangoo on 4/29/2016.
 */
public class FestivalInfoAdapter extends BaseAdapter{

    ArrayList<String> festvials = new ArrayList<>();
    private LayoutInflater layoutInflater;
    ViewHolder holder = new ViewHolder();

    public FestivalInfoAdapter(Context context){
        festvials.add("EDC");
        festvials.add("Lightning in a Bottle");
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return festvials.size();
    }

    @Override
    public Object getItem(int position) {
        return festvials.get(position);
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

        holder.festivalName.setText(festvials.get(position));
        holder.festivalDate.setText("Date Here");
        holder.festivalLocation.setText("Location Here");
        holder.festivalImage.setImageResource(R.drawable.placeholder);

        return convertView;
    }

    static class ViewHolder {
        TextView festivalName;
        TextView festivalDate;
        TextView festivalLocation;
        ImageView festivalImage;
    }
}
