package sakuraiandco.com.gtcollab.rest.factories;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.domain.CourseMessage;
import sakuraiandco.com.gtcollab.rest.factories.base.BaseFactory;

/**
 * Created by kaliq on 11/26/2017.
 */

public class CourseMessageFactory extends BaseFactory<CourseMessage> {

    UserFactory userFactory = new UserFactory();

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
                .creator(userFactory.toDomain(o.getJSONObject("creator")))
                .timestamp(DateTime.parse(o.getString("timestamp"))) // TODO: format?
                .build();
    }
}
