package sakuraiandco.com.gtcollab.utils;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.domain.User;

/**
 * Created by kaliq on 11/14/2017.
 */

public class GeneralUtils {

    public static List<Integer> getUserIDs(List<User> users) {
        List<Integer> userIDs = new ArrayList<>();
        for (User u : users) {
            userIDs.add(u.getId());
        }
        return userIDs;
    }

    public static List<String> getUserNames(List<User> users) {
        List<String> userIDs = new ArrayList<>();
        for (User u : users) {
            userIDs.add(u.getFullName());
        }
        return userIDs;
    }

    public static String joinStrings(List<String> strings, String delimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            stringBuilder.append(strings.get(i));
            if (i < strings.size() - 1) {
                stringBuilder.append(delimiter);
            }
        }
        return stringBuilder.toString();
    }

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
}
