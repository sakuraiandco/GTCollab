package sakuraiandco.com.gtcollab.temp;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alex on 10/14/17.
 */

public class Meeting {

    private int id;
    private String name;
    private String location;
    private String description;

    // TODO: consider converting to date/time data types
    private String startDate;
    private String startTime;
    // duration in minutes
    private int durationMinutes;
    private int creatorID;
    private JSONArray members;
    private Context context;
    private ContextSingleton contextSingleton;


    public Meeting(int id, String name, String location, String description, String startDate, String startTime, int durationMinutes, int creatorID, Context context) {
        this(id, name, location, description, startDate, startTime, durationMinutes, creatorID, new JSONArray(), context);
    }

    public Meeting(int id, String name, String location, String description, String startDate, String startTime, int durationMinutes, int creatorID, JSONArray members, Context context) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.startDate = startDate;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.creatorID = creatorID;
        this.members = members;
        this.context = context;
        this.contextSingleton = ContextSingleton.getInstance(context.getApplicationContext());
    }

    public Meeting(JSONObject meetingJSON, Context context) {
        try {
            this.id = meetingJSON.getInt("id");
            this.name = meetingJSON.getString("name");
            this.location = meetingJSON.getString("location");
            this.description = meetingJSON.getString("description");

            this.startDate = meetingJSON.getString("start_date");
            this.startTime = meetingJSON.getString("start_time");
            this.durationMinutes = meetingJSON.getInt("duration_minutes");
            this.creatorID = meetingJSON.getInt("id");
            this.members = meetingJSON.getJSONArray("members");
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

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getCreatorID() {
        return creatorID;
    }

    public JSONArray getMembers() {
        return members;
    }


    public void create(int courseID) {
        Map<String, String> params = new HashMap<>(5);
        params.put("name", name);
        params.put("location", location);
        params.put("description", description);
        params.put("start_date", startDate);
        params.put("start_time", startTime);
        params.put("duration_minutes", Integer.toString(durationMinutes));
        params.put("course", Integer.toString(courseID));

        String path = "meetings";
        final Request request = Singleton.getRequestHandler().getRequest(path, "POST", params);
        contextSingleton.getRequestQueue().add(request);
    }

    public static void create(String name, String location, String description, String startDate, String startTime, int duration, int courseID, RequestQueue queue) {
        Map<String, String> params = new HashMap<>(5);
        params.put("name", name);
        params.put("location", location);
        params.put("description", description);
        params.put("start_date", startDate);
        params.put("start_time", startTime);
        params.put("duration_minutes", Integer.toString(duration));
        params.put("course", Integer.toString(courseID));

        String path = "meetings";
        final Request request = Singleton.getRequestHandler().getRequest(path, "POST", params);
        queue.add(request);
    }
}
