package sakuraiandco.com.gtcollab.rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.rest.base.DAOListener;

import static sakuraiandco.com.gtcollab.utils.NetworkUtils.postRequest;

/**
 * Created by kaliq on 10/15/2017.
 */

public class GroupDAO extends BaseDAO<Group> {

    private UserDAO userDAO;

    public GroupDAO(DAOListener<Group> callback) {
        super(Group.BASE_URL, callback);
        this.userDAO = new UserDAO(null);
    }

    public void joinGroup(int id) {
        postRequest(baseURL + id + "/join/", null, this);
    }

    public void leaveGroup(int id) {
        postRequest(baseURL + id + "/leave/", null, this);
    }

    @Override
    public JSONObject toJSON(Group g) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("id", g.getId());
        o.put("name", g.getName());
        o.put("members", new JSONArray(g.getMembers()));
        o.put("course", g.getCourseId());
        return o;
    }

    @Override
    public Group toDomain(JSONObject o) throws JSONException {
        JSONArray membersJSON = o.getJSONArray("members");
        List<Integer> members = new ArrayList<>();
        for (int i = 0; i < membersJSON.length(); i++) {
            members.add(membersJSON.getInt(i));
        }
        return Group.builder()
                .id(o.getInt("id"))
                .name(o.getString("name"))
                .courseId(o.getInt("course"))
                .creator(new UserDAO(null).toDomain(o.getJSONObject("creator")))
                .members(members)
                .build();
    }
}
