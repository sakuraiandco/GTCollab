package sakuraiandco.com.gtcollab.rest;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.domain.GroupMessage;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

/**
 * Created by kaliq on 10/15/2017.
 */

public class GroupMessageDAO extends BaseDAO<GroupMessage> {

    public GroupMessageDAO(Listener<GroupMessage> callback) {
        super(GroupMessage.BASE_URL, callback);
    }

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
                .creator(new UserDAO(null).toDomain(o.getJSONObject("creator")))
                .timestamp(DateTime.parse(o.getString("timestamp"))) // TODO: format?
                .build();
    }
}
