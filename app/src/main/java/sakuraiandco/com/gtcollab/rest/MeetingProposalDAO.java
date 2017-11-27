package sakuraiandco.com.gtcollab.rest;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.domain.MeetingProposal;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

import static sakuraiandco.com.gtcollab.utils.NetworkUtils.postRequest;

/**
 * Created by kaliq on 10/15/2017.
 */

public class MeetingProposalDAO extends BaseDAO<MeetingProposal> {

    private UserDAO userDAO;

    public MeetingProposalDAO(Listener<MeetingProposal> callback) {
        super(MeetingProposal.BASE_URL, callback);
        this.userDAO = new UserDAO(null);
    }

    public void approve(int id) {
        postRequest(getBaseURL() + id + "/approve/", null, this);
    }

    public void reject(int id) {
        postRequest(getBaseURL() + id + "/reject/", null, this);
    }

    @Override
    public JSONObject toJSON(MeetingProposal mn) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("meeting", mn.getMeetingId());
        o.put("location", mn.getLocation());
        o.put("start_date", mn.getStartDate().toString());
        o.put("start_time", mn.getStartTime().toString("HH:mm"));
        return o;
    }

    @Override
    public MeetingProposal toDomain(JSONObject o) throws JSONException {
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
        JSONArray responsesReceivedJSON = o.getJSONArray("responses_received");
        List<Integer> responsesReceived = new ArrayList<>();
        for (int i = 0; i < responsesReceivedJSON.length(); i++) {
            responsesReceived.add(responsesReceivedJSON.getInt(i));
        }
        return MeetingProposal.builder()
                .id(o.getInt("id"))
                .meetingId(o.getInt("meeting"))
                .title(o.getString("title"))
                .message(o.getString("message"))
                .messageExpanded(o.getString("message_expanded"))
                .creator(new UserDAO(null).toDomain(o.getJSONObject("creator")))
                .recipients(recipients)
                .recipientsReadBy(recipientsReadBy)
                .timestamp(DateTime.parse(o.getString("timestamp"))) // TODO: format?
                .location(o.getString("location"))
                .startDate(DateTime.parse(o.getString("start_date"), ISODateTimeFormat.yearMonthDay()).toLocalDate())
                .startTime(DateTime.parse(o.getString("start_time"), ISODateTimeFormat.hourMinuteSecond()).toLocalTime())
                .responsesReceived(responsesReceived)
                .expirationMinutes(o.getInt("expiration_minutes"))
                .applied(o.getBoolean("applied"))
                .closed(o.getBoolean("closed"))
                .build();
    }
}
