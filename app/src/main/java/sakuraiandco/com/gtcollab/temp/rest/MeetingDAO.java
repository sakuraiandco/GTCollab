package sakuraiandco.com.gtcollab.temp.rest;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.temp.domain.Meeting;
import sakuraiandco.com.gtcollab.temp.domain.User;
import sakuraiandco.com.gtcollab.temp.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.temp.rest.base.DAOListener;

import static sakuraiandco.com.gtcollab.temp.utils.NetworkUtils.postRequest;

/**
 * Created by kaliq on 10/15/2017.
 */

public class MeetingDAO extends BaseDAO<Meeting> {

    public MeetingDAO(DAOListener<Meeting> callback) {
        super(Meeting.BASE_URL, callback);
    }

    public void joinMeeting(int id) {
        postRequest(baseURL + id + "/join", null, this);
    }

    public void leaveMeeting(int id) {
        postRequest(baseURL + id + "/leave", null, this);
    }

    @Override
    public JSONObject toJSON(Meeting m) {
        JSONObject o = new JSONObject();
        JSONArray memberIds = new JSONArray();
        for (User u : m.getMembers()) {
            memberIds.put(u.getId());
        }
        try {
            o.put("id", m.getId());
            o.put("name", m.getName());
            o.put("description", m.getDescription());
            o.put("start_date", m.getStartDate().toString());
            o.put("start_time", m.getStartTime().toString("HH:mm"));
            o.put("duration_minutes", m.getDurationMinutes());
            o.put("members", memberIds);
        } catch (JSONException e) {
            e.printStackTrace(); // shouldn't happen
        }
        return o;
    }

    @Override
    public Meeting toDomain(JSONObject o) {
        try {
            JSONArray membersJSON = o.getJSONArray("members_data");
            UserDAO userDAO = new UserDAO(null);
            List<User> members = new ArrayList<>();
            for (int i = 0; i < membersJSON.length(); i++) {
                members.add(userDAO.toDomain(membersJSON.getJSONObject(i)));
            }
            return Meeting.builder()
                    .id(o.getInt("id"))
                    .name(o.getString("name"))
                    .description(o.getString("description"))
                    .startDate(DateTime.parse(o.getString("start_date"), ISODateTimeFormat.yearMonthDay()).toLocalDate())
                    .startTime(DateTime.parse(o.getString("start_time"), ISODateTimeFormat.hourMinuteSecond()).toLocalTime())
                    .durationMinutes(o.getInt("duration_minutes"))
                    .creator(userDAO.toDomain(o.getJSONObject("creator")))
                    .members(members)
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // TODO
    }
}
