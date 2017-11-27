package sakuraiandco.com.gtcollab.rest.factories;

import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.domain.Subject;
import sakuraiandco.com.gtcollab.rest.factories.base.ReadOnlyFactory;

/**
 * Created by kaliq on 11/26/2017.
 */

public class SubjectFactory extends ReadOnlyFactory<Subject> {

    @Override
    public Subject toDomain(JSONObject o) throws JSONException {
        return Subject.builder()
                .id(o.getInt("id"))
                .name(o.getString("name"))
                .code(o.getString("code"))
                .termName(o.getJSONObject("term").getString("name"))
                .coursesLoaded(o.getBoolean("courses_loaded"))
                .build();
    }
}
