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
    public JSONObject toJSON(MeetingProposal m) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("id", m.getId());
        o.put("meeting", m.getMeetingId());
        o.put("location", m.getLocation());
        o.put("start_date", m.getStartDate().toString());
        o.put("start_time", m.getStartTime().toString("HH:mm"));
        return o;
    }

    @Override
    public MeetingProposal toDomain(JSONObject o) throws JSONException {
        JSONArray responsesReceivedJSON = o.getJSONArray("responses_received");
        List<Integer> responsesReceived = new ArrayList<>();
        for (int i = 0; i < responsesReceivedJSON.length(); i++) {
            responsesReceived.add(responsesReceivedJSON.getInt(i));
        }
        return MeetingProposal.builder()
                .id(o.getInt("id"))
                .meetingId(o.getInt("meeting"))
                .location(o.getString("location"))
                .startDate(DateTime.parse(o.getString("start_date"), ISODateTimeFormat.yearMonthDay()).toLocalDate())
                .startTime(DateTime.parse(o.getString("start_time"), ISODateTimeFormat.hourMinuteSecond()).toLocalTime())
                .creator(userDAO.toDomain(o.getJSONObject("creator")))
                .timestamp(DateTime.parse(o.getString("timestamp"))) // TODO: format?
                .responsesReceived(responsesReceived)
                .expirationMinutes(o.getInt("expiration_minutes"))
                .applied(o.getBoolean("applied"))
                .closed(o.getBoolean("closed"))
                .build();
    }
}
