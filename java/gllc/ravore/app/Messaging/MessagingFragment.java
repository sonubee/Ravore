package gllc.ravore.app.Messaging;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gllc.ravore.app.Automation.GetBracelet;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 4/15/2016.
 */
public class MessagingFragment extends Fragment {

    String selectedId = MyApplication.selectedId;
    public static Bracelet braceletForMessaging;
    public static TextView braceletNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        braceletForMessaging = GetBracelet.getBracelet(selectedId);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.messaging, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        braceletNum = (TextView)getActivity().findViewById(R.id.braceletNumber);
        braceletNum.setText("Kandi# " + braceletForMessaging.getBraceletId());
    }
}
