package sakuraiandco.com.gtcollab.rest;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.domain.MeetingNotification;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

/**
 * Created by kaliq on 11/1/2017.
 */

public class MeetingNotificationDAO extends BaseDAO<MeetingNotification> {

    public MeetingNotificationDAO(Listener<MeetingNotification> callback) {
        super(MeetingNotification.BASE_URL, callback);
    }

    @Override
    public JSONObject toJSON(MeetingNotification mn) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("meeting", mn.getMeetingId());
        o.put("message", mn.getMessage());
        o.put("messageExpanded", mn.getMessageExpanded());
        o.put("recipients", new JSONArray(mn.getRecipients()));
        return o;
    }

    @Override
    public MeetingNotification toDomain(JSONObject o) throws JSONException {
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
        return MeetingNotification.builder()
                .id(o.getInt("id"))
                .meetingId(o.getInt("meeting"))
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
