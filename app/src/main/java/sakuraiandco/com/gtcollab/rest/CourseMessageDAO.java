package sakuraiandco.com.gtcollab.rest;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.domain.CourseMessage;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.rest.base.DAOListener;

/**
 * Created by kaliq on 10/15/2017.
 */

public class CourseMessageDAO extends BaseDAO<CourseMessage> {

    public CourseMessageDAO(DAOListener<CourseMessage> callback) {
        super(CourseMessage.BASE_URL, callback);
    }

    @Override
    public JSONObject toJSON(CourseMessage cm) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("content", cm.getContent());
        o.put("course", cm.getCourseId());
        return o;
    }

    @Override
    public CourseMessage toDomain(JSONObject o) throws JSONException {
        return CourseMessage.builder()
                .id(o.getInt("id"))
                .content(o.getString("content"))
                .courseId(o.getInt("course"))
                .creator(new UserDAO(null).toDomain(o.getJSONObject("creator")))
                .timestamp(DateTime.parse(o.getString("timestamp"))) // TODO: format?
                .build();
    }
}
