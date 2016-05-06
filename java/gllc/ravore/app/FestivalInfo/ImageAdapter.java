package gllc.ravore.app.FestivalInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.TextView;

import gllc.ravore.app.R;

/**
 * Created by bhangoo on 4/30/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private static LayoutInflater inflater=null;

    public ImageAdapter(Context c) {
        mContext = c;
        inflater = ( LayoutInflater )c.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.gridview_layout, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

        holder.tv.setText(orderItems[position]);
        holder.img.setImageResource(mThumbIds[position]);

/*

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(285, 285));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setAdjustViewBounds(true);

            imageView.setPadding(20, 20, 20, 20);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
        */
        return rowView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.festival_scaled, R.drawable.map,
            R.drawable.ticket, R.drawable.web,
            R.drawable.scaled_festival_map, R.drawable.match,
            R.drawable.preparty_scaled, R.drawable.uploads
    };

    private String[] orderItems = {
            "Pictures", "World Map" , "Get Tickets" , "See Website" , "Festival Map" , "Get Matched" , "Parties/Gatherings" , "Uploads"
    };

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
}