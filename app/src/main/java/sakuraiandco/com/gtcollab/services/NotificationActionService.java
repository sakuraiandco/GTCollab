package sakuraiandco.com.gtcollab.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.List;

import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.domain.Meeting;
import sakuraiandco.com.gtcollab.domain.MeetingProposal;
import sakuraiandco.com.gtcollab.rest.GroupDAO;
import sakuraiandco.com.gtcollab.rest.MeetingDAO;
import sakuraiandco.com.gtcollab.rest.MeetingProposalDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_GROUP_INVITATION_ACCEPT;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_GROUP_INVITATION_IGNORE;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_MEETING_INVITATION_ACCEPT;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_MEETING_INVITATION_IGNORE;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_MEETING_PROPOSAL_ACCEPT;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_MEETING_PROPOSAL_REJECT;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_GROUP_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_MEETING_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_MEETING_PROPOSAL_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_NOTIFICATION_ID;

/**
 * Created by kaliq on 11/14/2017.
 */

public class NotificationActionService extends IntentService {

    public static final String TAG = "NOTIFICATION_SERVICE";

    GroupDAO groupDAO;
    MeetingDAO meetingDAO;
    MeetingProposalDAO meetingProposalDAO;

    public NotificationActionService() {
        super(NotificationActionService.class.getSimpleName());
        groupDAO = new GroupDAO(new BaseDAO.Listener<Group>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Group> groups) {}
            @Override
            public void onObjectReady(Group group) {
                // TODO: display success notification
            }
            @Override
            public void onObjectDeleted() {}
        });
        meetingDAO = new MeetingDAO(new BaseDAO.Listener<Meeting>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Meeting> meetings) {}
            @Override
            public void onObjectReady(Meeting meeting) {
                // TODO: display success notification
            }
            @Override
            public void onObjectDeleted() {}
        });
        meetingProposalDAO = new MeetingProposalDAO(new BaseDAO.Listener<MeetingProposal>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<MeetingProposal> meetingProposals) {}
            @Override
            public void onObjectReady(MeetingProposal meetingProposal) {
                // TODO: display success notification, along with current statistics (e.g. 5/7 have accepted the proposal)
            }
            @Override
            public void onObjectDeleted() {}
        });
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManagerCompat.from(this).cancel(intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1));
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case ACTION_GROUP_INVITATION_ACCEPT:
                    groupDAO.joinGroup(intent.getIntExtra(EXTRA_GROUP_ID, -1));
                    break;
                case ACTION_GROUP_INVITATION_IGNORE:
                    Log.d(TAG, "Group invitation ignored");
                    break;
                case ACTION_MEETING_INVITATION_ACCEPT:
                    meetingDAO.joinMeeting(intent.getIntExtra(EXTRA_MEETING_ID, -1));
                    break;
                case ACTION_MEETING_INVITATION_IGNORE:
                    Log.d(TAG, "Meeting invitation ignored");
                    break;
                case ACTION_MEETING_PROPOSAL_ACCEPT:
                    meetingProposalDAO.approve(intent.getIntExtra(EXTRA_MEETING_PROPOSAL_ID, -1));
                    break;
                case ACTION_MEETING_PROPOSAL_REJECT:
                    meetingProposalDAO.reject(intent.getIntExtra(EXTRA_MEETING_PROPOSAL_ID, -1));
                    break;
                default:
                    Log.d(TAG, "Invalid intent action"); // TODO: error handling
            }
        } else {
            Log.d(TAG, "Missing intent action"); // TODO: error handling
        }
    }

}

