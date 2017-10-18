package sakuraiandco.com.gtcollab.rest;

import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.constants.Constants;
import sakuraiandco.com.gtcollab.utils.VolleyResponseListener;

import static sakuraiandco.com.gtcollab.utils.NetworkUtils.getRequest;
import static sakuraiandco.com.gtcollab.utils.NetworkUtils.postRequest;

/**
 * Created by kaliq on 10/15/2017.
 */

public class RESTServices {

    public static final String AUTH_URL = Constants.BASE_URL + "/api-token-auth/";
    public static final String SERVER_STATUS_URL = Constants.BASE_URL + "/server-status/";

    public static void authUser(String username, String password, VolleyResponseListener callback) {
        JSONObject body = new JSONObject();
        try {
            body.put("username", username);
            body.put("password", password);
        } catch (JSONException e) { // shouldn't happen with String
            e.printStackTrace();
        }
        postRequest(AUTH_URL, body, callback);
    }

    public static void getServerStatus(VolleyResponseListener callback) {
        getRequest(SERVER_STATUS_URL, callback);
    }

}
