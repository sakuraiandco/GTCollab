package sakuraiandco.com.gtcollab;

import org.json.JSONArray;

/**
 * Created by Alex on 10/14/17.
 */

public class Course {
    private final int id;
    private final String name;
    // TODO: consider changing to regular array later
    // TODO: consider adding "joined" field to disable/enable join class button
    private final JSONArray members;

    Course (int id, String name, JSONArray members) {
        this.id = id;
        this.name = name;
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public JSONArray getMembers() {
        return members;
    }
}
