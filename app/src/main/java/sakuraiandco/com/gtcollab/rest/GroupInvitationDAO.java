package sakuraiandco.com.gtcollab.rest;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.domain.GroupInvitation;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

/**
 * Created by kaliq on 11/1/2017.
 */

public class GroupInvitationDAO extends BaseDAO<GroupInvitation> {

    public GroupInvitationDAO(Listener<GroupInvitation> callback) {
        super(GroupInvitation.BASE_URL, callback);
    }

    @Override
    public JSONObject toJSON(GroupInvitation gi) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("group", gi.getGroupId());
        o.put("recipients", new JSONArray(gi.getRecipients()));
        return o;
    }

    @Override
    public GroupInvitation toDomain(JSONObject o) throws JSONException {
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
        return GroupInvitation.builder()
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
