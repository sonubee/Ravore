package gllc.ravore.app.OrderRavore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Bead;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 4/16/2016.
 */
public class ShoppingCartAdapter extends BaseAdapter{

    private LayoutInflater layoutInflater;
    ShoppingCartViewHolder holder = new ShoppingCartViewHolder();
    public static ArrayList<Bead> beadAdapter = new ArrayList<>();
    Context context;
    public static ArrayList<Integer> cartQty = new ArrayList<>();

    Bead bear = new Bead("Teddy Bear", "path");
    Bead cat = new Bead("Cat", "path");
    Bead dog = new Bead("Dog", "path");
    Bead walrus = new Bead("Walrus", "path");
    Bead octopus = new Bead("Octopus", "path");

    HashMap<String, Object> getImage = new HashMap<>();

    public ShoppingCartAdapter (Context context){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;

        beadAdapter.add(bear);
        beadAdapter.add(cat);
        beadAdapter.add(dog);
        beadAdapter.add(walrus);
        beadAdapter.add(octopus);

        for (int i=0; i < beadAdapter.size(); i++){cartQty.add(i, 0);}

        Bitmap cat = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat);
        Bitmap dog = BitmapFactory.decodeResource(context.getResources(), R.drawable.dog);
        Bitmap bear = BitmapFactory.decodeResource(context.getResources(), R.drawable.bear);
        Bitmap walrus = BitmapFactory.decodeResource(context.getResources(), R.drawable.walrus);
        Bitmap octopus = BitmapFactory.decodeResource(context.getResources(), R.drawable.octo);

        getImage.put("Cat", cat);
        getImage.put("Dog", dog);
        getImage.put("Teddy Bear", bear);
        getImage.put("Walrus", walrus);
        getImage.put("Octopus", octopus);
    }

    @Override
    public int getCount() {
        return beadAdapter.size();
    }

    @Override
    public Object getItem(int position) {
        return beadAdapter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.sell_beads_row_layout, null);

        holder.beadName = (TextView)convertView.findViewById(R.id.beadNameTextView);
        holder.cartQty = (TextView)convertView.findViewById(R.id.cartCartQty);
        holder.price = (TextView)convertView.findViewById(R.id.priceOfBead);
        holder.add = (TextView)convertView.findViewById(R.id.addBeadListView);
        holder.subtract = (TextView)convertView.findViewById(R.id.subtractBeadListView);
        holder.picture = (ImageView)convertView.findViewById(R.id.picForBead);

        holder.beadName.setText(beadAdapter.get(position).getBeadName());
        holder.cartQty.setText("Cart: " + cartQty.get(position));
        holder.price.setText("$1.00");
        holder.picture.setImageBitmap((Bitmap) getImage.get(beadAdapter.get(position).getBeadName()));

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCart = cartQty.get(position);
                currentCart++;
                cartQty.set(position, currentCart);
                notifyDataSetChanged();
            }
        });

        holder.subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCart = cartQty.get(position);

                if (!(currentCart == 0)){
                    currentCart--;
                    cartQty.set(position, currentCart);
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }

    static class ShoppingCartViewHolder {
        TextView beadName;
        TextView cartQty;
        TextView price;
        TextView add;
        TextView subtract;
        ImageView picture;
    }
}
