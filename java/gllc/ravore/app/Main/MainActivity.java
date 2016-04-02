package gllc.ravore.app.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

//import com.localytics.android.Localytics;
import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.model.HelpLiveo;
import br.liveo.navigationliveo.NavigationLiveo;
import gllc.ravore.app.GCM.PushReceiver;
import gllc.ravore.app.OrderRavore.OrderRavoreActivity;
import gllc.ravore.app.OrderRavore.ListOrdersFragment;
//import gllc.ravore.app.Messaging.IntentReceiver;
import gllc.ravore.app.Messaging.MessagingActivity;
import gllc.ravore.app.Messaging.MessagingAdapter;
import gllc.ravore.app.Messaging.ShowAllMessagesFragment;
import gllc.ravore.app.R;

public class MainActivity extends NavigationLiveo implements OnItemClickListener{

    private HelpLiveo mHelpLiveo;

    @Override
    public void onInt(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        materialDesignSetup();
    }

    @Override
    public void onItemClick(int position) {
        Fragment mFragment;
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();

        switch (position) {

            case 0:
                mFragment = new ShowAllMessagesFragment();
                break;

            case 1:
                Intent intent = new Intent(getBaseContext(), OrderRavoreActivity.class);
                startActivity(intent);
                mFragment = new ShowAllMessagesFragment();
                break;

            case 2:
                mFragment = new Feedback();
                break;

            case 3:
                mFragment = new ListOrdersFragment();
                break;

            default:
                mFragment = new ShowAllMessagesFragment();
                break;
        }

        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.replace(R.id.container, mFragment).commit();

        setElevationToolBar(position != 2 ? 15 : 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MyActivity", "On Destroy from MainActivity");
    }


    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MyActivity", "On Pause from MainActivity");

        PushReceiver.displayNotifications=true;
        PushReceiver.unreadCount.clear();
        PushReceiver.chatIdKey.clear();

    }

    @Override
    protected void onResume() {
        super.onResume();
        PushReceiver.displayNotifications=false;
        PushReceiver.unreadCount.clear();
        PushReceiver.chatIdKey.clear();

        Log.i("MyActivity", "Do Not Display Notifications");
    }

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {
            Log.i("MyActivity", "Options Selected");

            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };

    private View.OnClickListener onClickFooter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
        }
    };

    public void materialDesignSetup() {
        //this.userName.setText("Check Out Other Features Below!");
        //this.userEmail.setText("Or Keep Chatting!");

        this.userBackground.setImageResource(R.drawable.kandi);

        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add(getString(R.string.inbox), R.mipmap.ic_drafts_black_24dp);
//        mHelpLiveo.addSubHeader(getString(R.string.categories)); //Item subHeader
        //mHelpLiveo.add(getString(R.string.starred), R.mipmap.ic_star_black_24dp);
        mHelpLiveo.add(getString(R.string.sent_mail), R.drawable.bracelet2);
        mHelpLiveo.add(getString(R.string.drafts), R.drawable.feedback);
//        mHelpLiveo.addSeparator(); // Item separator
        mHelpLiveo.add(getString(R.string.trash), R.mipmap.ic_add_white_24dp);
//        mHelpLiveo.add(getString(R.string.spam), R.mipmap.ic_report_black_24dp, 120);

        //with(this, Navigation.THEME_DARK). add theme dark
        //with(this, Navigation.THEME_LIGHT). add theme light

        with(this) // default theme is dark
                .startingPosition(0) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())
                        //.footerItem(R.string.settings, R.mipmap.ic_settings_black_24dp)
                .setOnClickUser(onClickPhoto)
                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)
                .build();
    }

}