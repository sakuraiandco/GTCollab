package sakuraiandco.com.gtcollab.rest.factories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.rest.factories.base.BaseFactory;

/**
 * Created by kaliq on 11/26/2017.
 */

public class GroupFactory extends BaseFactory<Group> {

    UserFactory userFactory = new UserFactory();

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
                .creator(userFactory.toDomain(o.getJSONObject("creator")))
                .members(members)
                .build();
    }
}
