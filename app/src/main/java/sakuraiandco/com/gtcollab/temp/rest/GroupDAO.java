package sakuraiandco.com.gtcollab.temp.rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.temp.domain.Group;
import sakuraiandco.com.gtcollab.temp.domain.User;
import sakuraiandco.com.gtcollab.temp.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.temp.rest.base.DAOListener;

import static sakuraiandco.com.gtcollab.temp.utils.NetworkUtils.postRequest;

/**
 * Created by kaliq on 10/15/2017.
 */

public class GroupDAO extends BaseDAO<Group> {

    public GroupDAO(DAOListener<Group> callback) {
        super(Group.BASE_URL, callback);
    }

    public void joinGroup(int id) {
        postRequest(baseURL + id + "/join", null, this);
    }

    public void leaveGroup(int id) {
        postRequest(baseURL + id + "/leave", null, this);
    }

    @Override
    public JSONObject toJSON(Group g) {
        JSONObject o = new JSONObject();
        JSONArray memberIds = new JSONArray();
        for (User u : g.getMembers()) {
            memberIds.put(u.getId());
        }
        try {
            o.put("id", g.getId());
            o.put("name", g.getName());
            o.put("members", memberIds);
        } catch (JSONException e) {
            e.printStackTrace(); // shouldn't happen
        }
        return o;
    }

    @Override
    public Group toDomain(JSONObject o) {
        try {
            JSONArray membersJSON = o.getJSONArray("members_data");
            UserDAO userDAO = new UserDAO(null);
            List<User> members = new ArrayList<>();
            for (int i = 0; i < membersJSON.length(); i++) {
                members.add(userDAO.toDomain(membersJSON.getJSONObject(i)));
            }
            return Group.builder()
                    .id(o.getInt("id"))
                    .name(o.getString("name"))
                    .creator(new UserDAO(null).toDomain(o.getJSONObject("creator")))
                    .members(members)
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // TODO
    }
}
