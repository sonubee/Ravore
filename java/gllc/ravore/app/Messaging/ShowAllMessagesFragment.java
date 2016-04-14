package gllc.ravore.app.Messaging;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;

import gllc.ravore.app.Automation.AddBracelet;
import gllc.ravore.app.Main.LoginActivity;
import gllc.ravore.app.Main.MainActivity;
import gllc.ravore.app.OrderRavore.OrderRavoreActivity;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Message;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 2/14/2016.
 */
public class ShowAllMessagesFragment extends Fragment {

    ArrayList<ArrayList<Message>> allSendAndReceivedMessagesDoubleArray;
    public static ListView listView;
    public static ListAllMessagesAdapter adapterAllMessages;
    private boolean mSearchCheck;
    public static Context context;
    SwipeDetector swipeDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allSendAndReceivedMessagesDoubleArray = new ArrayList<>();
        swipeDetector = new SwipeDetector();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_all_messages, container, false);
        Log.i("MyActivity", "Create View");
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context=getContext();
        Log.i("MyActivity", "SAM Activity Created");
        adapterAllMessages = new ListAllMessagesAdapter(getActivity().getBaseContext());

        adapterAllMessages.notifyDataSetChanged();
        Log.i("MyActivity", "Adapter Count: " + adapterAllMessages.getCount());

        //adapterAllMessages.notifyDataSetChanged();
        listView = (ListView)getActivity().findViewById(R.id.listViewAllMessages);
        listView.setAdapter(adapterAllMessages);
        listView.setOnTouchListener(swipeDetector);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, final int position, long id) {

                MyApplication.selectedId = MyApplication.allGivenAndReceivedBraceletsObjects.get(position).getBraceletId();
                MyApplication.setSelectedBracelet(MyApplication.selectedId);

                if (swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == SwipeDetector.Action.RL) {
                        //Toast.makeText(getActivity(), "Swipe Left", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getContext(), MessagingActivity.class));

                    } else {
                        //Toast.makeText(getActivity(), "Swipe Right", Toast.LENGTH_SHORT).show();
                        if (MyApplication.allGivenAndReceivedBraceletsObjects.get(position).getGiverId().equals(MyApplication.android_id)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Delete Kandi");
                            builder.setMessage("Are you sure you want to delete " + MyApplication.allGivenAndReceivedBraceletsObjects.get(position).getBraceletId() + "? This cannot be recovered unless you use a new Ravore!!");
                            builder.setPositiveButton("Yes, Delete!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("MyActivity", "Delete: " + MyApplication.allGivenAndReceivedBraceletsObjects.get(position).getGiverId());
                                    String key = MyApplication.braceletKey.get(MyApplication.allGivenAndReceivedBraceletsObjects.get(position).getBraceletId());
                                    new Firebase(MyApplication.useFirebase+"Bracelets/"+key).removeValue();
                                    new Firebase(MyApplication.useFirebase+"Messages/"+MyApplication.allGivenAndReceivedBraceletsObjects.get(position).getBraceletId()).removeValue();
                                    Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.show();
                        }
                    }
                } else {
                    if (MyApplication.allGivenAndReceivedBraceletsObjects.get(position).getGiverId().equals(MyApplication.android_id)) {
                        MyApplication.currentUserIsGiver = true;
                    } else {
                        MyApplication.currentUserIsGiver = false;
                    }

                    startActivity(new Intent(getContext(), MessagingActivity.class));
                }
            }
        });

        setHasOptionsMenu(true);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("--AllSAMessagesFragment", "OnDestroyView from ShowAllMessages");
        listView.setAdapter(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);

        mSearchCheck = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case R.id.menu_add:

                final CharSequence[] items = {"Give A Bracelet", "I Received A Bracelet"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add A Bracelet");

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("I Received A Bracelet")) {
                            addBraceletAsReceiver();
                        } else if (items[item].equals("Give A Bracelet")) {
                            addBraceletAsGiver();}}
                });
                builder.show();
                break;

            case R.id.menu_buy:
                //mSearchCheck = true;
                Intent intent = new Intent(getContext(), OrderRavoreActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void addBraceletAsGiver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Enter ID Here");
        builder.setTitle("Add as Giver");
        builder.setMessage("Enter the Ravore ID Below!");
        builder.setView(input);
        builder.setPositiveButton("Add Bracelet!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new AddBracelet("giver", input.getText().toString(), context, "Main", null);}});

        builder.show();
    }

    public void addBraceletAsReceiver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Enter ID Here");
        builder.setTitle("Add as Receiver");
        builder.setMessage("Enter the Ravore ID Below!");
        builder.setView(input);
        builder.setPositiveButton("Add Bracelet!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new AddBracelet("receiver", input.getText().toString(), context, "Main", null);
            }
        });
            builder.show();
        }

        private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (mSearchCheck){
                // implement your search here
            }
            return false;}};




}