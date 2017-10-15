package sakuraiandco.com.gtcollab.temp.rest;

import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.temp.constants.Constants;
import sakuraiandco.com.gtcollab.temp.utils.VolleyResponseListener;

import static sakuraiandco.com.gtcollab.temp.utils.NetworkUtils.getRequest;
import static sakuraiandco.com.gtcollab.temp.utils.NetworkUtils.postRequest;

/**
 * Created by kaliq on 10/15/2017.
 */

public class RESTServices {

    public static void authUser(String username, String password, VolleyResponseListener callback) {
        JSONObject body = new JSONObject();
        try {
            body.put("username", username);
            body.put("password", password);
        } catch (JSONException e) { // shouldn't happen with String
            e.printStackTrace();
        }
        postRequest(Constants.AUTH_URL, body, callback);
    }

    public static void getServerStatus(VolleyResponseListener callback) {
        getRequest(Constants.SERVER_STATUS_URL, callback);
    }

}
