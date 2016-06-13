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
import android.widget.ImageView;
import android.widget.Toast;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.model.HelpLiveo;
import br.liveo.navigationliveo.NavigationLiveo;
import gllc.ravore.app.Automation.ClearNotifications;
import gllc.ravore.app.Automation.UploadImage;
import gllc.ravore.app.FestivalInfo.ShowFestivals;
import gllc.ravore.app.Messaging.LoadProfilePhoto;
import gllc.ravore.app.Pushy.PushReceiver;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.OrderRavore.OrderRavoreActivity;
import gllc.ravore.app.OrderRavore.ListOrdersFragment;
import gllc.ravore.app.Messaging.ShowAllMessagesFragment;
import gllc.ravore.app.Pushy.RegisterPushy;
import gllc.ravore.app.R;
import me.pushy.sdk.Pushy;

public class MainActivity extends NavigationLiveo implements OnItemClickListener{

    private HelpLiveo mHelpLiveo;

    @Override
    public void onInt(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        materialDesignSetup();

        Pushy.listen(this);
        long interval = ( 1000 * 60 * 3 ); // Every 3 minutes
        Pushy.setHeartbeatInterval(interval, this);
        new RegisterPushy(getApplicationContext(), this).execute();
    }

    @Override
    public void onItemClick(int position) {
            Fragment mFragment;
            FragmentManager mFragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = mFragmentManager.beginTransaction();

            switch (position) {

                case 0:
                    mFragment = new Profile();

                    break;

                case 1:
                    mFragment = new ShowFestivals();
                    break;

                case 2:
                    mFragment = new ShowAllMessagesFragment();
                    break;

                case 3:
                    Intent intent = new Intent(getBaseContext(), OrderRavoreActivity.class);
                    startActivity(intent);
                    mFragment = new ShowAllMessagesFragment();
                    break;

                case 4:
                    mFragment = new ListOrdersFragment();
                    break;

                case 5:
                    mFragment = new Feedback();
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
        Log.i("--AllMyActivity", "On Destroy from MainActivity");
    }


    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("AllMyActivity", "On Pause from MainActivity");

        MyApplication.displayNotifications=true;
        PushReceiver.unreadCount.clear();
        PushReceiver.chatIdKey.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("--AllMyActivity", "Do Not Display Notifications");
        Log.i("--AllMyActivity", "On Resume from MainActivity");

        new ClearNotifications(getBaseContext());

        MyApplication.displayNotifications=false;
        PushReceiver.unreadCount.clear();
        PushReceiver.chatIdKey.clear();
    }

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {
            Log.i("--AllMyActivity", "Options Selected");

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

        this.userBackground.setImageResource(R.drawable.wheel);

        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add("Profile", R.drawable.ic_launcher);
        mHelpLiveo.add(getString(R.string.spam), R.drawable.ferris);
        mHelpLiveo.add(getString(R.string.inbox), R.mipmap.ic_drafts_black_24dp);
//        mHelpLiveo.addSubHeader(getString(R.string.categories)); //Item subHeader
        //mHelpLiveo.add(getString(R.string.starred), R.mipmap.ic_star_black_24dp);
        mHelpLiveo.add(getString(R.string.sent_mail), R.drawable.bracelet2);
        mHelpLiveo.add(getString(R.string.trash), R.mipmap.ic_add_white_24dp);
        mHelpLiveo.add(getString(R.string.drafts), R.drawable.feedback);
        //mHelpLiveo.add("About", R.drawable.about);
//        mHelpLiveo.addSeparator(); // Item separator

        //with(this, Navigation.THEME_DARK). add theme dark
        //with(this, Navigation.THEME_LIGHT). add theme light

        with(this) // default theme is dark
                .startingPosition(2) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())
                        //.footerItem(R.string.settings, R.mipmap.ic_settings_black_24dp)
                .setOnClickUser(onClickPhoto)
                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("--AllMessagingActivity", "Request Code: " + requestCode + " : " + MyApplication.REQUEST_CAMERA + " : " + MyApplication.SELECT_FILE);
        Log.i("--AllMessagingActivity", "Result Code: " + resultCode + " Result Ok : " + RESULT_OK);

        if (resultCode == RESULT_OK) {
            ImageView imageView;

            //if (MyApplication.currentUserIsGiver){imageView = (ImageView)findViewById(R.id.giver_image);}
            //if (MyApplication.currentUserIsGiver){imageView = MessagingFragment.giverImage;}
            //else {imageView = (ImageView)findViewById(R.id.receiver_image);}
            //else {imageView = MessagingFragment.receiverImage;}

            imageView = (ImageView)findViewById(R.id.ravorImage);

            if (requestCode == MyApplication.REQUEST_CAMERA) {
                Log.i("--AllMessagingActivity", "Before LoadProfile");
                new LoadProfilePhoto(imageView, this);
            }

            else if (requestCode == MyApplication.SELECT_FILE) {new LoadProfilePhoto(data.getData(), imageView, this);}

            Log.i("--AllMessagingActivity", "Before Uploading Image");
            new UploadImage(requestCode, this).execute();
        }
        else {
            Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();}
    }
}