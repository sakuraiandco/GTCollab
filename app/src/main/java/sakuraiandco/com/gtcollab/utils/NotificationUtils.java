package sakuraiandco.com.gtcollab.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;
import java.util.Locale;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.activities.LoginActivity;
import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.domain.Meeting;
import sakuraiandco.com.gtcollab.rest.GroupDAO;
import sakuraiandco.com.gtcollab.rest.MeetingDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.services.NotificationActionService;

import static sakuraiandco.com.gtcollab.constants.Arguments.ACCEPTED;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_1;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_2;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_4;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_GROUP;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_MEETING;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_MEETING_PROPOSAL_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_MEETING_PROPOSAL_RESPONSE;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_NOTIFICATION_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.REJECTED;

/**
 * Created by kaliq on 10/31/2017.
 */

public class NotificationUtils {

    final static int NOTIFICATION_ICON = R.drawable.ic_sentiment_neutral_black_24dp;

    final static String TAG = "NOTIFICATIONS";

    // TODO: custom notification ID - unique for each invitation? if-so use unique request codes for pendingIntents too

    public static void notifyGroupInvitation(final Context context, final int groupId, final String courseShortName, final String creatorFirstName) {
        GroupDAO groupDAO = new GroupDAO(new BaseDAO.Listener<Group>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Group> groups) {}
            @Override
            public void onObjectReady(Group group) {
                int notificationID = (int) System.currentTimeMillis();
                Notification n = buildNotificationGroupInvitation(context, notificationID, group, courseShortName, creatorFirstName);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify(notificationID, n);
                } else {
                    Log.d(TAG, "NotificationManager not found");
                }
            }
            @Override
            public void onObjectDeleted() {}
        });
        groupDAO.get(groupId);
    }

    public static void notifyMeetingInvitation(final Context context, final int meetingId, final String courseShortName, final String creatorFirstName) {
        MeetingDAO meetingDAO = new MeetingDAO(new BaseDAO.Listener<Meeting>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Meeting> meetings) {}
            @Override
            public void onObjectReady(Meeting meeting) {
                int notificationID = (int) System.currentTimeMillis();
                Notification n = buildNotificationMeetingInvitation(context, notificationID, meeting, courseShortName, creatorFirstName);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify(notificationID, n);
                } else {
                    Log.d(TAG, "NotificationManager not found");
                }
            }
            @Override
            public void onObjectDeleted() {}
        });
        meetingDAO.get(meetingId);
    }

    public static void notifyAddedToGroup(final Context context, final int groupId, final String courseShortName, final String creatorFirstName) {
        GroupDAO groupDAO = new GroupDAO(new BaseDAO.Listener<Group>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Group> groups) {}
            @Override
            public void onObjectReady(Group group) {
                int notificationID = (int) System.currentTimeMillis();
                Notification n = buildNotificationAddedToGroup(context, notificationID, group, courseShortName, creatorFirstName);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify(notificationID, n);
                } else {
                    Log.d(TAG, "NotificationManager not found");
                }
            }
            @Override
            public void onObjectDeleted() {}
        });
        groupDAO.get(groupId);
    }

    public static void notifyMeetingProposal(final Context context, final int meetingProposalId, final int meetingId, final String location, final LocalDate startDate, final LocalTime startTime, final String courseShortName, final String creatorFirstName) {
        MeetingDAO meetingDAO = new MeetingDAO(new BaseDAO.Listener<Meeting>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Meeting> meetings) {}
            @Override
            public void onObjectReady(Meeting meeting) {
                int notificationID = (int) System.currentTimeMillis();
                Notification n = buildNotificationMeetingProposal(context, notificationID, meetingProposalId, meeting, location, startDate, startTime, courseShortName, creatorFirstName);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify(notificationID, n);
                } else {
                    Log.d(TAG, "NotificationManager not found");
                }
            }
            @Override
            public void onObjectDeleted() {}
        });
        meetingDAO.get(meetingId);
    }

    public static void notifyMeetingProposalResult(final Context context, final int meetingProposalId, final int meetingId, final String location, final LocalDate startDate, final LocalTime startTime, final boolean applied, final String courseShortName, final String creatorFirstName) {
        MeetingDAO meetingDAO = new MeetingDAO(new BaseDAO.Listener<Meeting>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Meeting> meetings) {}
            @Override
            public void onObjectReady(Meeting meeting) {
                int notificationID = (int) System.currentTimeMillis();
                Notification n = buildNotificationMeetingProposalResult(context, notificationID, meetingProposalId, meeting, location, startDate, startTime, applied, courseShortName, creatorFirstName);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify(notificationID, n);
                } else {
                    Log.d(TAG, "NotificationManager not found");
                }
            }
            @Override
            public void onObjectDeleted() {}
        });
        meetingDAO.get(meetingId);
    }

    private static Notification buildNotificationGroupInvitation(Context context, int notificationID, Group group, String courseShortName, String creatorFirstName) {
        String content = courseShortName + ": " + creatorFirstName + " has invited you to their group!";
        String bigContent = content
                + "\n\n" + group.getName();

        // Expanded Content
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(bigContent);

        // Default Action
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(LoginActivity.class); // TODO: set to activity where user can see pending invitations / proposals
        stackBuilder.addNextIntent(new Intent(context, LoginActivity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Join Meeting Action
        Intent joinGroupIntent = new Intent(context, NotificationActionService.class);
        joinGroupIntent.setAction(ACTION_1);
        joinGroupIntent.putExtra(EXTRA_GROUP, group); // TODO: refactor key
        joinGroupIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationID);
        PendingIntent joingGroupPendingIntent = PendingIntent.getService(context, 0, joinGroupIntent, PendingIntent.FLAG_UPDATE_CURRENT); // TODO: unique request codes?

        // Ignore Invite Action
        Intent ignoreInviteIntent = new Intent(context, NotificationActionService.class);
        ignoreInviteIntent.setAction(ACTION_1);
        ignoreInviteIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationID);
        PendingIntent ignoreInvitePendingIntent = PendingIntent.getService(context, 1, ignoreInviteIntent, PendingIntent.FLAG_UPDATE_CURRENT); // TODO: unique request codes?

        String title = "Group Invitation";
        Notification n = getNotificationBuilder(context, NOTIFICATION_ICON, title, content, bigTextStyle, resultPendingIntent)
                .addAction(NOTIFICATION_ICON, "Join", joingGroupPendingIntent)
                .addAction(NOTIFICATION_ICON, "Ignore", ignoreInvitePendingIntent)
                .build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        return n;
    }

    private static Notification buildNotificationMeetingInvitation(Context context, int notificationID, Meeting meeting, String courseShortName, String creatorFirstName) {
        String formatString = "%s: %s has invited you to their meeting!\n\nName: %s\nLocation: %s\nStart Date: %s\nStart Time: %s\nDuration: %d minutes\nDescription: %s";
        String content = String.format(Locale.getDefault(), formatString, courseShortName, creatorFirstName, meeting.getName(), meeting.getLocation(), meeting.getStartDate().toString("EEE MMM dd"), meeting.getStartTime().toString("h:mm a"), meeting.getDurationMinutes(), meeting.getDescription());

        // Expanded Content
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText(content);
        bigTextStyle.bigText(content);

        // Default Action
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(LoginActivity.class); // TODO: set to activity where user can see pending invitations / proposals
        stackBuilder.addNextIntent(new Intent(context, LoginActivity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Join Meeting Action TODO: consolidate into one intent
        Intent joinMeetingIntent = new Intent(context, NotificationActionService.class);
        joinMeetingIntent.setAction(ACTION_2);
        joinMeetingIntent.putExtra(EXTRA_MEETING, meeting); // TODO: refactor key
        joinMeetingIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationID);
        PendingIntent joinMeetingPendingIntent = PendingIntent.getService(context, 0, joinMeetingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Ignore Invite Action TODO: consolidate into one intent
        Intent ignoreInviteIntent = new Intent(context, NotificationActionService.class);
        ignoreInviteIntent.setAction(ACTION_2);
        ignoreInviteIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationID);
        PendingIntent ignoreInvitePendingIntent = PendingIntent.getService(context, 1, ignoreInviteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String title = "Meeting Invitation";
        Notification n = getNotificationBuilder(context, NOTIFICATION_ICON, title, content, bigTextStyle, resultPendingIntent)
                .addAction(NOTIFICATION_ICON, "Join", joinMeetingPendingIntent)
                .addAction(NOTIFICATION_ICON, "Ignore", ignoreInvitePendingIntent)
                .build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        return n;
    }

    private static Notification buildNotificationAddedToGroup(Context context, int notificationID, Group group, String courseShortName, String creatorFirstName) {
        String content = courseShortName + ": " + creatorFirstName + " has added you to their group!";
        String bigContent = content
                + "\n\n" + group.getName();

        // Expanded Content
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(bigContent);

        // Default Action
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(LoginActivity.class); // TODO: set to activity where user can see pending invitations / proposals
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(EXTRA_NOTIFICATION_ID, notificationID);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        String title = "Group Invitation";
        Notification n = getNotificationBuilder(context, NOTIFICATION_ICON, title, content, bigTextStyle, resultPendingIntent).build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        return n;
    }

    private static Notification buildNotificationMeetingProposal(Context context, int notificationID, int meetingProposalId, Meeting meeting, String location, LocalDate startDate, LocalTime startTime, String courseShortName, String creatorFirstName) {
        String formatString = "%s has proposed a time/location change:\n\nFrom:\n%s\n%s\n\nTo:\n%s\n%s"; // TODO: check if same (no change)
        String currLocation = meeting.getLocation();
        String dateTimeString = startDate.toString("EEE MMM dd") + " " + startTime.toString("h:mm a");
        String currDateTimeString = meeting.getStartDate().toString("EEE MMM dd") + " " + meeting.getStartTime().toString("h:mm a");
        String content = String.format(Locale.getDefault(), formatString, creatorFirstName, currLocation, currDateTimeString, location, dateTimeString);

        // Expanded Content
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText(content);
        bigTextStyle.bigText(content);

        // Default Action
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(LoginActivity.class); // TODO: set to activity where user can see pending invitations / proposals
        stackBuilder.addNextIntent(new Intent(context, LoginActivity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Accept Meeting Proposal Action
        Intent acceptMeetingProposalIntent = new Intent(context, NotificationActionService.class);
        acceptMeetingProposalIntent.setAction(ACTION_4);
        acceptMeetingProposalIntent.putExtra(EXTRA_MEETING_PROPOSAL_ID, meetingProposalId);
        acceptMeetingProposalIntent.putExtra(EXTRA_MEETING_PROPOSAL_RESPONSE, ACCEPTED);
        acceptMeetingProposalIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationID);
        PendingIntent acceptMeetingProposalPendingIntent = PendingIntent.getService(context, 0, acceptMeetingProposalIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Reject Meeting Proposal Action
        Intent rejectMeetingProposalIntent = new Intent(context, NotificationActionService.class);
        rejectMeetingProposalIntent.setAction(ACTION_4);
        rejectMeetingProposalIntent.putExtra(EXTRA_MEETING_PROPOSAL_ID, meetingProposalId);
        rejectMeetingProposalIntent.putExtra(EXTRA_MEETING_PROPOSAL_RESPONSE, REJECTED);
        rejectMeetingProposalIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationID);
        PendingIntent rejectMeetingProposalPendingIntent = PendingIntent.getService(context, 1, rejectMeetingProposalIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String title = courseShortName + " - " + meeting.getName();
        Notification n = getNotificationBuilder(context, NOTIFICATION_ICON, title, content, bigTextStyle, resultPendingIntent)
                .addAction(NOTIFICATION_ICON, "Accept", acceptMeetingProposalPendingIntent)
                .addAction(NOTIFICATION_ICON, "Reject", rejectMeetingProposalPendingIntent)
                .build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        return n;
    }

    private static Notification buildNotificationMeetingProposalResult(Context context, int notificationID, int meetingProposalId, Meeting meeting, String location, LocalDate startDate, LocalTime startTime, boolean applied, String courseShortName, String creatorFirstName) {
        String content = "";
        if (applied) {
            String dateTimeString = startDate.toString("EEE MMM dd") + " " + startTime.toString("h:mm a");
            String formatString = "Meeting time/location has been changed:\n\n%s\n%s"; // TODO: check if same (no change)
            content = String.format(Locale.getDefault(), formatString, location, dateTimeString);
        } else {
            content = "Proposal for meeting time/location change has been rejected.";
        }

        // Expanded Content
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText(content);
        bigTextStyle.bigText(content);

        // Default Action
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(LoginActivity.class); // TODO: set to activity where user can see pending invitations / proposals
        stackBuilder.addNextIntent(new Intent(context, LoginActivity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        String title = courseShortName + " - " + meeting.getName();
        Notification n = getNotificationBuilder(context, NOTIFICATION_ICON, title, content, bigTextStyle, resultPendingIntent).build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        return n;
    }

    private static NotificationCompat.Builder getNotificationBuilder(Context context, int iconResourceID, String title, String content, NotificationCompat.Style style, PendingIntent pendingIntent) {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(iconResourceID)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(style)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
    }

}
