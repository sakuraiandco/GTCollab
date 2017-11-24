package sakuraiandco.com.gtcollab.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.domain.Meeting;
import sakuraiandco.com.gtcollab.domain.MeetingProposal;
import sakuraiandco.com.gtcollab.rest.GroupDAO;
import sakuraiandco.com.gtcollab.rest.MeetingDAO;
import sakuraiandco.com.gtcollab.rest.MeetingProposalDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

import static sakuraiandco.com.gtcollab.constants.Arguments.ACCEPTED;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_1;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_2;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_3;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_4;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_5;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_GROUP;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_MEETING;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_MEETING_PROPOSAL_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_MEETING_PROPOSAL_RESPONSE;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_NOTIFICATION_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.REJECTED;

/**
 * Created by kaliq on 11/14/2017.
 */

public class NotificationActionService extends IntentService {

    public static final String TAG = "NOTIFICATION_ACTION";

    GroupDAO groupDAO;
    MeetingDAO meetingDAO;
    MeetingProposalDAO meetingProposalDAO;

    public NotificationActionService() {
        super(NotificationActionService.class.getSimpleName());
        groupDAO = new GroupDAO(new BaseDAO.Listener<Group>() { // TODO: open app to course page to display success / error dialog (?)
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Group> groups) {}
            @Override
            public void onObjectReady(Group group) {}
            @Override
            public void onObjectDeleted() {}
        });
        meetingDAO = new MeetingDAO(new BaseDAO.Listener<Meeting>() { // TODO: open app to course page to display success / error dialog (?)
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Meeting> meetings) {}
            @Override
            public void onObjectReady(Meeting meeting) {
//                    Intent intent = new Intent(NotificationActionService.this, CourseListActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                    startActivity(intent);
            }
            @Override
            public void onObjectDeleted() {}
        });
        meetingProposalDAO = new MeetingProposalDAO(new BaseDAO.Listener<MeetingProposal>() { // TODO: open app to course page to display success / error dialog (?)
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<MeetingProposal> meetingProposals) {}
            @Override
            public void onObjectReady(MeetingProposal meetingProposal) {
                // TODO
            }
            @Override
            public void onObjectDeleted() {}
        });
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String action = intent.getAction();
        if (manager != null) { // TODO
            manager.cancel(intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)); // TODO
        } else {
            Log.d(TAG, "NotificationManager not found");
        }
        Meeting meeting = null; // TODO: reorganize
        switch (action) {
            case ACTION_1: // Join Group
                Group group = intent.getParcelableExtra(EXTRA_GROUP);
                if (group != null) {
                    groupDAO.joinGroup(group.getId());
                } else {
                    Log.d(TAG, "No group - ignore was pressed");
                }
                break;
            case ACTION_2: // Join Meeting
                meeting = intent.getParcelableExtra(EXTRA_MEETING);
                if (meeting != null) {
                    meetingDAO.joinMeeting(meeting.getId());
                } else {
                    Log.d(TAG, "No meeting - ignore was pressed");
                }
                break;
            case ACTION_3:
                break;
            case ACTION_4: // Meeting Proposal
                int meetingProposalId = intent.getIntExtra(EXTRA_MEETING_PROPOSAL_ID, -1);
                int meetingProposalResponse = intent.getIntExtra(EXTRA_MEETING_PROPOSAL_RESPONSE, -1);
                switch (meetingProposalResponse) {
                    case ACCEPTED:
                        meetingProposalDAO.approve(meetingProposalId);
                        break;
                    case REJECTED:
                        meetingProposalDAO.reject(meetingProposalId);
                        break;
                    default:
                        // TODO: error handling
                }
                break;
            case ACTION_5:
                break;
            default:
                // TODO: error handling
        }
        // If you want to cancel the notification: NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
    }
}

