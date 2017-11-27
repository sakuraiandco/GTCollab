package sakuraiandco.com.gtcollab.rest;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.domain.StandardNotification;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

/**
 * Created by kaliq on 11/1/2017.
 */

public class StandardNotificationDAO extends BaseDAO<StandardNotification> {

    public StandardNotificationDAO(Listener<StandardNotification> callback) {
        super(StandardNotification.BASE_URL, callback);
    }

    @Override
    public JSONObject toJSON(StandardNotification sn) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("title", sn.getTitle());
        o.put("message", sn.getMessage());
        o.put("messageExpanded", sn.getMessageExpanded());
        o.put("recipients", new JSONArray(sn.getRecipients()));
        return o;
    }

    @Override
    public StandardNotification toDomain(JSONObject o) throws JSONException {
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
        return StandardNotification.builder()
                .id(o.getInt("id"))
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
