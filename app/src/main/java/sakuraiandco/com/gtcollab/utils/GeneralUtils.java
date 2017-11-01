package sakuraiandco.com.gtcollab.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import sakuraiandco.com.gtcollab.LoginActivity;

import static sakuraiandco.com.gtcollab.constants.Arguments.AUTH_TOKEN;
import static sakuraiandco.com.gtcollab.constants.Arguments.AUTH_TOKEN_FILE;
import static sakuraiandco.com.gtcollab.constants.Arguments.CURRENT_USER;
import static sakuraiandco.com.gtcollab.constants.Arguments.DEVICE_REGISTRATION_ID;

/**
 * Created by kaliq on 10/31/2017.
 */

public class GeneralUtils {

    public static void forceDeviceTokenRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                    FirebaseInstanceId.getInstance().getToken();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void login(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(AUTH_TOKEN_FILE, 0);
        prefs.edit().remove(AUTH_TOKEN).remove(CURRENT_USER).remove(DEVICE_REGISTRATION_ID).apply();
        Intent loginActivityIntent = new Intent(context, LoginActivity.class);
        context.startActivity(loginActivityIntent);
    }

    public static void logout(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(AUTH_TOKEN_FILE, 0);
        prefs.edit().remove(AUTH_TOKEN).remove(CURRENT_USER).remove(DEVICE_REGISTRATION_ID).apply();
        Intent loginActivityIntent = new Intent(context, LoginActivity.class);
        context.startActivity(loginActivityIntent);
    }

    // TODO: define methods here for starting activities - so that you can specify the extras required as arguments
    // e.g. startCourseActivity(int courseId)
}
