package sakuraiandco.com.gtcollab.rest;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.domain.MeetingInvitation;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

/**
 * Created by kaliq on 11/1/2017.
 */

public class MeetingInvitationDAO extends BaseDAO<MeetingInvitation> {

    public MeetingInvitationDAO(Listener<MeetingInvitation> callback) {
        super(MeetingInvitation.BASE_URL, callback);
    }

    @Override
    public JSONObject toJSON(MeetingInvitation mi) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("meeting", mi.getMeetingId());
        o.put("recipients", new JSONArray(mi.getRecipients()));
        return o;
    }

    @Override
    public MeetingInvitation toDomain(JSONObject o) throws JSONException {
        JSONArray recipientsJSON = o.getJSONArray("recipients");
        List<Integer> recipients = new ArrayList<>();
        for (int i = 0; i < recipientsJSON.length(); i++) {
            recipients.add(recipientsJSON.getInt(i));
        }
        return MeetingInvitation.builder()
                .id(o.getInt("id"))
                .meetingId(o.getInt("meeting"))
                .creator(new UserDAO(null).toDomain(o.getJSONObject("creator")))
                .recipients(recipients)
                .timestamp(DateTime.parse(o.getString("timestamp"))) // TODO: format?
                .build();
    }
}
