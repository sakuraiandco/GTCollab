package sakuraiandco.com.gtcollab.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static sakuraiandco.com.gtcollab.utils.NotificationUtils.handleNotification;

/**
 * Created by kaliq on 10/31/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "FCM MESSAGE";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO: check if user is logged in - otherwise no notification sent?

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        Map<String,String> data = remoteMessage.getData();

        Log.d(TAG, "Message data payload: " + data); // TODO: remove

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            Log.d(TAG, "Message Notification Title: " + notification.getTitle());
            Log.d(TAG, "Message Notification Body: " + notification.getBody());
        }

        handleNotification(this, data);
    }

    @Override
    public void onDeletedMessages() {
        // TODO
    }

}
