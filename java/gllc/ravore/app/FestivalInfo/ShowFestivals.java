package gllc.ravore.app.FestivalInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import gllc.ravore.app.Messaging.ListAllMessagesAdapter;
import gllc.ravore.app.Messaging.MessagingActivity;
import gllc.ravore.app.OrderRavore.OrderRavoreActivity;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 4/29/2016.
 */
public class ShowFestivals extends Fragment {
    public static ListView listView;
    public static FestivalInfoAdapter festivalInfoAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview_festival_info, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        festivalInfoAdapter = new FestivalInfoAdapter(getContext());
        listView = (ListView) getActivity().findViewById(R.id.listViewFestivalInfo);

        listView.setAdapter(festivalInfoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
                startActivity(new Intent(getContext(), FestivalInfoActivity.class));
            }
        });
    }
}
