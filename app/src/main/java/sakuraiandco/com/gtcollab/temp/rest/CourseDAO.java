package sakuraiandco.com.gtcollab.temp.rest;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.temp.domain.Course;
import sakuraiandco.com.gtcollab.temp.rest.base.DAOListener;
import sakuraiandco.com.gtcollab.temp.rest.base.ReadOnlyDAO;

import static sakuraiandco.com.gtcollab.temp.utils.NetworkUtils.postRequest;

/**
 * Created by kaliq on 10/15/2017.
 */

public class CourseDAO extends ReadOnlyDAO<Course> {

    public CourseDAO(DAOListener<Course> callback) {
        super(Course.BASE_URL, callback);
    }

    public void joinCourse(int id) {
        postRequest(baseURL + id + "/join", null, this);
    }

    public void leaveCourse(int id) {
        postRequest(baseURL + id + "/leave", null, this);
    }

    @Override
    public Course toDomain(JSONObject o) {
        try {
            JSONArray sectionsJSON = o.getJSONArray("sections");
            JSONArray meetingTimesJSON = o.getJSONArray("meeting_times");

            List<String> sections = new ArrayList<>();
            List<Course.MeetingTime> meetingTimes = new ArrayList<>();

            for (int i = 0; i < sectionsJSON.length(); i++) {
                JSONObject sectionJSON = sectionsJSON.getJSONObject(i);
                sections.add(sectionJSON.getString("name"));
            }

            for (int i = 0; i < meetingTimesJSON.length(); i++) {
                JSONObject meetingTimeJSON = meetingTimesJSON.getJSONObject(i);
                meetingTimes.add(Course.MeetingTime.builder()
                        .meetDays(meetingTimeJSON.getString("meet_days"))
                        .startTime(DateTime.parse(meetingTimeJSON.getString("startTime"), ISODateTimeFormat.hourMinuteSecond()).toLocalTime())
                        .endTime(DateTime.parse(meetingTimeJSON.getString("endTime"), ISODateTimeFormat.hourMinuteSecond()).toLocalTime())
                        .build());
            }

            return Course.builder()
                    .id(o.getInt("id"))
                    .name(o.getString("name"))
                    .subject_code(o.getJSONObject("subject").getString("code"))
                    .course_number(o.getString("course_number"))
                    .sections(sections)
                    .meetingTimes(meetingTimes)
                    .numMembers(o.getJSONArray("members").length())
                    .isCancelled(o.getBoolean("is_cancelled"))
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // TODO
    }
}
