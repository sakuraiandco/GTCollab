package sakuraiandco.com.gtcollab.rest.factories;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.domain.Course;
import sakuraiandco.com.gtcollab.rest.factories.base.ReadOnlyFactory;

/**
 * Created by kaliq on 11/26/2017.
 */

public class CourseFactory extends ReadOnlyFactory<Course> {

    @Override
    public Course toDomain(JSONObject o) throws JSONException {
        JSONArray sectionsJSON = o.getJSONArray("sections");
        JSONArray meetingTimesJSON = o.getJSONArray("meeting_times");
        JSONArray membersJSON = o.getJSONArray("members");

        List<String> sections = new ArrayList<>();
        List<Course.MeetingTime> meetingTimes = new ArrayList<>();
        List<Integer> members = new ArrayList<>();

        for (int i = 0; i < sectionsJSON.length(); i++) {
            JSONObject sectionJSON = sectionsJSON.getJSONObject(i);
            sections.add(sectionJSON.getString("name"));
        }

        for (int i = 0; i < membersJSON.length(); i++) {
            members.add(membersJSON.getInt(i));
        }

        for (int i = 0; i < meetingTimesJSON.length(); i++) {
            JSONObject meetingTimeJSON = meetingTimesJSON.getJSONObject(i);
            meetingTimes.add(Course.MeetingTime.builder()
                    .meetDays(meetingTimeJSON.getString("meet_days"))
                    .startTime(DateTime.parse(meetingTimeJSON.getString("start_time"), ISODateTimeFormat.hourMinuteSecond()).toLocalTime())
                    .endTime(DateTime.parse(meetingTimeJSON.getString("end_time"), ISODateTimeFormat.hourMinuteSecond()).toLocalTime())
                    .build());
        }

        return Course.builder()
                .id(o.getInt("id"))
                .name(o.getString("name"))
                .subjectCode(o.getJSONObject("subject").getString("code"))
                .courseNumber(o.getString("course_number"))
                .sections(sections)
                .meetingTimes(meetingTimes)
                .members(members)
                .numMembers(o.getJSONArray("members").length())
                .isCancelled(o.getBoolean("is_cancelled"))
                .build();
    }
}
