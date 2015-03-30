package org.mcguiggan.jsondemoapp.data;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.mcguiggan.jsondemoapp.BuildConfig;
import org.mcguiggan.jsondemoapp.model.User;

import java.io.IOException;
import java.util.List;

/**
 * Used for making a request to the server to load the JSON data.
 * This only requests data from the server, but could easily be extended to manage a local
 * in-memory and/or disk-based cache as well.
 */
public class SocialDataManager {

    private static final String TAG = SocialDataManager.class.getSimpleName();

    private static SocialDataManager sInstance; //Singleton instance

    private SocialDataManager() {
        //This default constructor is hidden, use getInstance() to instantiate the singleton SocialDataManager.
    }

    /**
     * This method is used to access the Singleton instance of the SocialDataManager class,
     * and also creates it if necessary.
     *
     * @return the Singleton instance of the SocialDataManager class.
     */
    public static SocialDataManager getInstance() {
        if (sInstance == null) {
            sInstance = new SocialDataManager();
        }

        return sInstance;
    }

    /**
     * Make a request to the server to get the social data.
     *
     * @param listener To receive the loaded data object.
     */
    public void requestData(final SocialDataManagerListener listener) {
        //Create your client.
        OkHttpClient client = new OkHttpClient();

        //Construct your request.
        Request request = new Request.Builder()
                .url(BuildConfig.SOCIAL_DATA_ENDPOINT) //Configured in the app's build.gradle file.
                .build();

        //Use the client to make the request, with a Callback to handle the response.
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //do proper error handling here
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    //do proper error handling here
                    Log.e(TAG, "Unexpected code " + response);
                }

                if (listener != null) {
                    Log.d(TAG, "Loaded from server");
                    String responseString = response.body().string();
                    Log.d(TAG, responseString);
                    List<User> userList = SocialDataParser.parseUsersJson(responseString);
                    listener.onDataLoaded(userList);
                }
            }
        });
    }

    /**
     * Interface to be implemented by any class that requests data from the SocialDataManager.
     */
    public interface SocialDataManagerListener {

        /**
         * Callback for when the social data has been fetched.
         *
         * @param userList The list of users that has been loaded.
         */
        void onDataLoaded(List<User> userList);
    }
}
