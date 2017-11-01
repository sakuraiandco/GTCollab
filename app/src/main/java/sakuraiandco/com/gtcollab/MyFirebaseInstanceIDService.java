package sakuraiandco.com.gtcollab;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import sakuraiandco.com.gtcollab.utils.VolleyResponseListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.AUTH_TOKEN_FILE;
import static sakuraiandco.com.gtcollab.constants.Arguments.DEVICE_REGISTRATION_ID;
import static sakuraiandco.com.gtcollab.rest.RESTServices.registerDevice;

/**
 * Created by kaliq on 10/31/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    public static final String TAG = "FCM DEVICE TOKEN";

    @Override
    public void onCreate() {
        onTokenRefresh(); // TODO: remove?
        super.onCreate();
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // Save token to local storage
        SharedPreferences prefs = getSharedPreferences(AUTH_TOKEN_FILE, 0);
        prefs.edit().putString(DEVICE_REGISTRATION_ID, refreshedToken).apply();

        // Register new device registration ID with server
        registerDevice(refreshedToken, new VolleyResponseListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Log.d(TAG, new String(error.networkResponse.data,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } // if user is not logged in, fail silently...
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Successfully registered device with server");
            }
        });
    }

}