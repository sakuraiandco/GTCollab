package sakuraiandco.com.gtcollab.temp.rest;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.temp.domain.GroupMessage;
import sakuraiandco.com.gtcollab.temp.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.temp.rest.base.DAOListener;

/**
 * Created by kaliq on 10/15/2017.
 */

public class GroupMessageDAO extends BaseDAO<GroupMessage> {

    public GroupMessageDAO(DAOListener<GroupMessage> callback) {
        super(GroupMessage.BASE_URL, callback);
    }

    @Override
    public JSONObject toJSON(GroupMessage gm) {
        JSONObject o = new JSONObject();
        try {
            o.put("content", gm.getContent());
            o.put("group", gm.getGroupId());
        } catch (JSONException e) {
            e.printStackTrace(); // shouldn't happen
        }
        return o;
    }

    @Override
    public GroupMessage toDomain(JSONObject o) {
        try {
            return GroupMessage.builder()
                    .id(o.getInt("id"))
                    .content(o.getString("content"))
                    .groupId(o.getInt("group"))
                    .creator(new UserDAO(null).toDomain(o.getJSONObject("creator")))
                    .timestamp(DateTime.parse(o.getString("timestamp"))) // TODO: format?
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // TODO
    }
}
