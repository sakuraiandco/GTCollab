package sakuraiandco.com.gtcollab.rest;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.domain.GroupNotification;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

/**
 * Created by kaliq on 11/1/2017.
 */

public class GroupNotificationDAO extends BaseDAO<GroupNotification> {

    public GroupNotificationDAO(Listener<GroupNotification> callback) {
        super(GroupNotification.BASE_URL, callback);
    }

    @Override
    public JSONObject toJSON(GroupNotification gn) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("group", gn.getGroupId());
        o.put("message", gn.getMessage());
        o.put("messageExpanded", gn.getMessageExpanded());
        o.put("recipients", new JSONArray(gn.getRecipients()));
        return o;
    }

    @Override
    public GroupNotification toDomain(JSONObject o) throws JSONException {
        JSONArray recipientsJSON = o.getJSONArray("recipients");
        List<Integer> recipients = new ArrayList<>();
        for (int i = 0; i < recipientsJSON.length(); i++) {
            recipients.add(recipientsJSON.getInt(i));
        }
        JSONArray recipientsReadByJSON = o.getJSONArray("recipients_read_by");
        List<Integer> recipientsReadBy = new ArrayList<>();
        for (int i = 0; i < recipientsReadByJSON.length(); i++) {
            recipients.add(recipientsReadByJSON.getInt(i));
        }
        return GroupNotification.builder()
                .id(o.getInt("id"))
                .groupId(o.getInt("group"))
                .title(o.getString("title"))
                .message(o.getString("message"))
                .messageExpanded(o.getString("message_expanded"))
                .creator(new UserDAO(null).toDomain(o.getJSONObject("creator")))
                .recipients(recipients)
                .recipientsReadBy(recipientsReadBy)
                .timestamp(DateTime.parse(o.getString("timestamp"))) // TODO: format?
                .build();
    }
}
