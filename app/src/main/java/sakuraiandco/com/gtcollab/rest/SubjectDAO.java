package sakuraiandco.com.gtcollab.rest;

import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.domain.Subject;
import sakuraiandco.com.gtcollab.rest.base.ReadOnlyDAO;

/**
 * Created by kaliq on 10/15/2017.
 */

public class SubjectDAO extends ReadOnlyDAO<Subject> {

    public SubjectDAO(Listener<Subject> callback) {
        super(Subject.BASE_URL, callback);
    }

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
