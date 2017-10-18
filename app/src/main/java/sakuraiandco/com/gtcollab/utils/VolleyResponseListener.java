package sakuraiandco.com.gtcollab.utils;

import com.android.volley.Response;

import org.json.JSONObject;

/**
 * Created by kaliq on 10/15/2017.
 */

public interface VolleyResponseListener extends Response.Listener<JSONObject>, Response.ErrorListener {
}
