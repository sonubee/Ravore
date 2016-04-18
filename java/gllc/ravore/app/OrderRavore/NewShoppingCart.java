package gllc.ravore.app.OrderRavore;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import gllc.ravore.app.Messaging.ListAllMessagesAdapter;
import gllc.ravore.app.Messaging.SwipeDetector;
import gllc.ravore.app.Objects.Bead;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 4/16/2016.
 */
public class NewShoppingCart extends Fragment {



    public static ArrayList<Bead> beadArray = new ArrayList<>();

    SwipeDetector swipeDetector;
    public static ListView listViewShoppingCart;
    public static ShoppingCartAdapter adapterAllBeads;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        swipeDetector = new SwipeDetector();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.listview_shopping_cart, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getContext();
        adapterAllBeads = new ShoppingCartAdapter(getContext());
        adapterAllBeads.notifyDataSetChanged();

        listViewShoppingCart = (ListView)getActivity().findViewById(R.id.listViewShoppingCart);
        listViewShoppingCart.setAdapter(adapterAllBeads);
    }
}
