package sakuraiandco.com.gtcollab.rest.factories;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.domain.Meeting;
import sakuraiandco.com.gtcollab.rest.factories.base.BaseFactory;

/**
 * Created by kaliq on 11/26/2017.
 */

public class MeetingFactory extends BaseFactory<Meeting> {

    UserFactory userFactory = new UserFactory();

    @Override
    public JSONObject toJSON(Meeting m) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("id", m.getId());
        o.put("name", m.getName());
        o.put("location", m.getLocation());
        o.put("description", m.getDescription());
        o.put("start_date", m.getStartDate().toString());
        o.put("start_time", m.getStartTime().toString("HH:mm"));
        o.put("course", m.getCourseId());
        o.put("duration_minutes", m.getDurationMinutes());
        o.put("members", new JSONArray(m.getMembers()));
        return o;
    }

    @Override
    public Meeting toDomain(JSONObject o) throws JSONException {
        JSONArray membersJSON = o.getJSONArray("members");
        List<Integer> members = new ArrayList<>();
        for (int i = 0; i < membersJSON.length(); i++) {
            members.add(membersJSON.getInt(i));
        }
        return Meeting.builder()
                .id(o.getInt("id"))
                .name(o.getString("name"))
                .location(o.getString("location"))
                .description(o.getString("description"))
                .startDate(DateTime.parse(o.getString("start_date"), ISODateTimeFormat.yearMonthDay()).toLocalDate())
                .startTime(DateTime.parse(o.getString("start_time"), ISODateTimeFormat.hourMinuteSecond()).toLocalTime())
                .durationMinutes(o.getInt("duration_minutes"))
                .courseId(o.getInt("course"))
                .creator(userFactory.toDomain(o.getJSONObject("creator")))
                .members(members)
                .build();
    }
}
