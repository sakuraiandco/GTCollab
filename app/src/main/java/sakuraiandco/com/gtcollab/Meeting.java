package sakuraiandco.com.gtcollab;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public Meeting(JSONObject meetingJSON) {
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
}
