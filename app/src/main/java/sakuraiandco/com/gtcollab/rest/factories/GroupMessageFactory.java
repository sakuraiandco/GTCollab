package sakuraiandco.com.gtcollab.rest.factories;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.domain.GroupMessage;
import sakuraiandco.com.gtcollab.rest.factories.base.BaseFactory;

/**
 * Created by kaliq on 11/26/2017.
 */

public class GroupMessageFactory extends BaseFactory<GroupMessage> {

    UserFactory userFactory = new UserFactory();

    @Override
    public JSONObject toJSON(GroupMessage gm) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("content", gm.getContent());
        o.put("group", gm.getGroupId());
        return o;
    }

    @Override
    public GroupMessage toDomain(JSONObject o) throws JSONException {
        return GroupMessage.builder()
                .id(o.getInt("id"))
                .content(o.getString("content"))
                .groupId(o.getInt("group"))
                .creator(userFactory.toDomain(o.getJSONObject("creator")))
                .timestamp(DateTime.parse(o.getString("timestamp"))) // TODO: format?
                .build();
    }
}
