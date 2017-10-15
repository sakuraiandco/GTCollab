package sakuraiandco.com.gtcollab;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 10/14/17.
 */

public class Course {
    private int id;
    private String name;
    // TODO: consider changing to regular array later
    // TODO: consider adding "joined" field to disable/enable join class button
    private JSONArray members;
    private Context context;
    private ContextSingleton contextSingleton;


    Course (int id, String name, JSONArray members, Context context) {
        this.id = id;
        this.name = name;
        this.members = members;
        this.context = context;
        this.contextSingleton = ContextSingleton.getInstance(context.getApplicationContext());
    }

    Course(JSONObject courseInfo, Context context) {
        // NOTE: not chaining as this() needs to be first statement in constructor and don't want to throw exception
        try {
            this.id = courseInfo.getInt("id");
            this.name = courseInfo.getString("name");
            this.members = courseInfo.getJSONArray("members");
            this.context = context;
            this.contextSingleton = ContextSingleton.getInstance(context.getApplicationContext());
        } catch (JSONException error) {
            Log.e("error", error.toString());
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Context getContext() {
        return context;
    }

    public JSONArray getMembers() {
        return members;
    }

    public void join(User user) {
        String path = String.format("courses/%d/join", id);
        final Request request = Singleton.getRequestHandler().getRequest(path, "POST");

        contextSingleton.getRequestQueue().add(request);
    }

    public void join(User user, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String path = String.format("courses/%d/join", id);
        final Request request = Singleton.getRequestHandler().getRequest(path, "POST");

        contextSingleton.getRequestQueue().add(request);
    }
}
