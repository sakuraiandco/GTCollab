package sakuraiandco.com.gtcollab.temp.rest;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.temp.domain.CourseMessage;
import sakuraiandco.com.gtcollab.temp.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.temp.rest.base.DAOListener;

/**
 * Created by kaliq on 10/15/2017.
 */

public class CourseMessageDAO extends BaseDAO<CourseMessage> {

    public CourseMessageDAO(DAOListener<CourseMessage> callback) {
        super(CourseMessage.BASE_URL, callback);
    }

    @Override
    public JSONObject toJSON(CourseMessage cm) {
        JSONObject o = new JSONObject();
        try {
            o.put("content", cm.getContent());
            o.put("course", cm.getCourseId());
        } catch (JSONException e) {
            e.printStackTrace(); // shouldn't happen
        }
        return o;
    }

    @Override
    public CourseMessage toDomain(JSONObject o) {
        try {
            return CourseMessage.builder()
                    .id(o.getInt("id"))
                    .content(o.getString("content"))
                    .courseId(o.getInt("course"))
                    .creator(new UserDAO(null).toDomain(o.getJSONObject("creator")))
                    .timestamp(DateTime.parse(o.getString("timestamp"))) // TODO: format?
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // TODO
    }
}
