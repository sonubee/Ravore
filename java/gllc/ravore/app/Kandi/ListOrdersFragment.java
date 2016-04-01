package gllc.ravore.app.Kandi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import gllc.ravore.app.R;

/**
 * Created by bhangoo on 3/14/2016.
 */
public class ListOrdersFragment extends Fragment {


    public static ListOrdersAdapter adapterAllOrders;
    public static Context context;
    public static ListView orderScreenListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_all_orders, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        context=getContext();
        Log.i("MyActivity", "SAM Activity Created");
        adapterAllOrders = new ListOrdersAdapter(getActivity().getBaseContext());

        adapterAllOrders.notifyDataSetChanged();

        orderScreenListView = (ListView)getActivity().findViewById(R.id.listViewAllOrders);
        orderScreenListView.setAdapter(adapterAllOrders);

        //setHasOptionsMenu(false);
    }


}
