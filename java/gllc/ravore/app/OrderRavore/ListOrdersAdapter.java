package gllc.ravore.app.OrderRavore;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 3/14/2016.
 */
public class ListOrdersAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    OrderViewHolder holder = new OrderViewHolder();
    public static ArrayList<Bracelet> orderArray;

    public ListOrdersAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        Log.i("MyActivity", "Came to Adapter Constructor");
        Log.i("MyActivity", "Orders: " + MyApplication.allOrders.size());
    }

    @Override
    public int getCount() {
        return MyApplication.allOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return MyApplication.allOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.show_all_orders, null);

        holder.beads = (ImageView) convertView.findViewById(R.id.leftOrderPic);
        holder.kandi = (ImageView) convertView.findViewById(R.id.rightOrderPic);
        holder.kandiCount = (TextView) convertView.findViewById(R.id.textView11);
        holder.beadCount = (TextView) convertView.findViewById(R.id.textView10);
        holder.status = (TextView) convertView.findViewById(R.id.textView15);
        holder.orderNumber = (TextView) convertView.findViewById(R.id.textView13);
        holder.totalPrice = (TextView) convertView.findViewById(R.id.textView16);

        holder.beads.setImageResource(R.drawable.beads_medium);
        holder.kandi.setImageResource(R.drawable.bg_small);
        holder.kandiCount.setText("Kandi (" + MyApplication.allOrders.get(position).getKandiCount() + ")");
        holder.beadCount.setText("Bead (" + MyApplication.allOrders.get(position).getBeadCount() + ")");
        holder.orderNumber.setText("Order #" + MyApplication.allOrders.get(position).getOrderNumber());
        holder.status.setText(MyApplication.allOrders.get(position).getStatus());
        holder.totalPrice.setText("Total:$"+String.format("%.2f", MyApplication.allOrders.get(position).getTotalPrice()));

        if (MyApplication.allOrders.get(position).getBeadCount() == 0){holder.beads.setVisibility(View.INVISIBLE);}
        if (MyApplication.allOrders.get(position).getKandiCount() == 0){holder.kandi.setVisibility(View.INVISIBLE);}

        return convertView;
    }


    static class OrderViewHolder {
        ImageView kandi;
        ImageView beads;
        TextView kandiCount;
        TextView beadCount;
        TextView orderNumber;
        TextView status;
        TextView totalPrice;
    }
}
