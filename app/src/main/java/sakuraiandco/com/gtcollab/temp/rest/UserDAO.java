package sakuraiandco.com.gtcollab.temp.rest;

import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.temp.domain.User;
import sakuraiandco.com.gtcollab.temp.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.temp.rest.base.DAOListener;

/**
 * Created by kaliq on 10/15/2017.
 */

public class UserDAO extends BaseDAO<User> {

    public UserDAO(DAOListener<User> callback) {
        super(User.BASE_URL, callback);
    }

    @Override
    public JSONObject toJSON(User u) {
        JSONObject o = new JSONObject();
        try {
            o.put("id", u.getId());
            o.put("username", u.getUsername());
            o.put("password", u.getPassword());
            o.put("first_name", u.getFirstName());
            o.put("last_name", u.getLastName());
            o.put("email", u.getEmail());
        } catch (JSONException e) {
            e.printStackTrace(); // shouldn't happen
        }
        return o;
    }

    @Override
    public User toDomain(JSONObject o) {
        try {
            return User.builder()
                    .id(o.getInt("id"))
                    .username(o.getString("username"))
                    .password("") // never store password
                    .firstName(o.getString("first_name"))
                    .lastName(o.getString("last_name"))
                    .email(o.getString("email"))
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // TODO
    }
}
