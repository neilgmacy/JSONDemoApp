package org.mcguiggan.jsondemoapp.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import org.mcguiggan.jsondemoapp.R;
import org.mcguiggan.jsondemoapp.data.SocialDataManager;
import org.mcguiggan.jsondemoapp.model.User;

import java.util.List;

public class MainActivity extends ActionBarActivity implements SocialDataManager.SocialDataManagerListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SocialDataManager.getInstance().requestUserList(this);
    }

    @Override
    public void onUserDataLoaded(List<User> userList) {
        //Nothing is currently shown on screen apart from the Hello Jackson message.
        //To check that this works, set a breakpoint here,
        //or to follow through step-by-step, set a breakpoint at the start of SocialDataParser#parseUsersJson()
        Log.d(TAG, "received parsed UserList");
    }
}
