package sakuraiandco.com.gtcollab.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Map;

import static sakuraiandco.com.gtcollab.constants.Constants.GROUP_INVITATION;
import static sakuraiandco.com.gtcollab.constants.Constants.GROUP_NOTIFICATION;
import static sakuraiandco.com.gtcollab.constants.Constants.MEETING_INVITATION;
import static sakuraiandco.com.gtcollab.constants.Constants.MEETING_PROPOSAL;
import static sakuraiandco.com.gtcollab.constants.Constants.MEETING_PROPOSAL_RESULT;
import static sakuraiandco.com.gtcollab.utils.NotificationUtils.notifyAddedToGroup;
import static sakuraiandco.com.gtcollab.utils.NotificationUtils.notifyGroupInvitation;
import static sakuraiandco.com.gtcollab.utils.NotificationUtils.notifyMeetingInvitation;
import static sakuraiandco.com.gtcollab.utils.NotificationUtils.notifyMeetingProposal;
import static sakuraiandco.com.gtcollab.utils.NotificationUtils.notifyMeetingProposalResult;

/**
 * Created by kaliq on 10/31/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "FCM MESSAGE";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Map<String,String> data = remoteMessage.getData();
            Log.d(TAG, "Message data payload: " + data);
            int type = Integer.valueOf(data.get("type"));
            Log.d(TAG, "Message type: " + type);
            LocalDate startDate = null; // TODO: cleanup
            LocalTime startTime = null; // TODO: cleanup
            switch (type) {
                case GROUP_INVITATION:
                    Log.d(TAG, "Group Invitation");
                    notifyGroupInvitation(this, Integer.valueOf(data.get("group_id")), data.get("course_short_name"), data.get("creator_first_name"));
                    break;
                case MEETING_INVITATION:
                    Log.d(TAG, "Meeting Invitation");
                    notifyMeetingInvitation(this, Integer.valueOf(data.get("meeting_id")), data.get("course_short_name"), data.get("creator_first_name"));
                    break;
                case GROUP_NOTIFICATION:
                    Log.d(TAG, "Group Notification");
                    notifyAddedToGroup(this, Integer.valueOf(data.get("group_id")), data.get("course_short_name"), data.get("creator_first_name"));
                    break;
                case MEETING_PROPOSAL:
                    Log.d(TAG, "Meeting Proposal");
                    startDate = DateTime.parse(data.get("start_date"), ISODateTimeFormat.yearMonthDay()).toLocalDate();
                    startTime = DateTime.parse(data.get("start_time"), ISODateTimeFormat.hourMinuteSecond()).toLocalTime();
                    notifyMeetingProposal(this, Integer.valueOf(data.get("id")), Integer.valueOf(data.get("meeting_id")), data.get("location"), startDate, startTime, data.get("course_short_name"), data.get("creator_first_name"));
                    break;
                case MEETING_PROPOSAL_RESULT:
                    Log.d(TAG, "Meeting Proposal Result");
                    startDate = DateTime.parse(data.get("start_date"), ISODateTimeFormat.yearMonthDay()).toLocalDate();
                    startTime = DateTime.parse(data.get("start_time"), ISODateTimeFormat.hourMinuteSecond()).toLocalTime();
                    notifyMeetingProposalResult(this, Integer.valueOf(data.get("id")), Integer.valueOf(data.get("meeting_id")), data.get("location"), startDate, startTime, Boolean.valueOf(data.get("applied")), data.get("course_short_name"), data.get("creator_first_name"));
                    break;
                default:
                    // TODO
            }
        } else {
            // TODO: something went wrong; all messages should have data
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // TODO: check if user is logged in - otherwise no notification sent?

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onDeletedMessages() {
        // TODO
    }

}
