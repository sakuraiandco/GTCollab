package sakuraiandco.com.gtcollab.rest.factories;

import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.factories.base.BaseFactory;

/**
 * Created by kaliq on 11/26/2017.
 */

public class UserFactory extends BaseFactory<User> {

    @Override
    public JSONObject toJSON(User u) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("id", u.getId());
        o.put("username", u.getUsername());
        o.put("password", u.getPassword());
        o.put("first_name", u.getFirstName());
        o.put("last_name", u.getLastName());
        o.put("email", u.getEmail());
        // TODO: change to use real data when profile implemented
        o.put("profile", new JSONObject());
        return o;
    }

    @Override
    public User toDomain(JSONObject o) throws JSONException {
        return User.builder()
                .id(o.getInt("id"))
                .username(o.getString("username"))
                .password("") // never store password
                .firstName(o.getString("first_name"))
                .lastName(o.getString("last_name"))
                .email(o.getString("email"))
                .build();
    }
}
