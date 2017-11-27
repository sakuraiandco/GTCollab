package sakuraiandco.com.gtcollab.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.List;
import java.util.Map;

import sakuraiandco.com.gtcollab.activities.NotificationsActivity;
import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.domain.Meeting;
import sakuraiandco.com.gtcollab.rest.GroupDAO;
import sakuraiandco.com.gtcollab.rest.MeetingDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.services.NotificationActionService;

import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_GROUP_INVITATION_ACCEPT;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_GROUP_INVITATION_IGNORE;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_MEETING_INVITATION_ACCEPT;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_MEETING_INVITATION_IGNORE;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_MEETING_PROPOSAL_ACCEPT;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_MEETING_PROPOSAL_REJECT;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_GROUP_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_GROUP_INVITATION_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_GROUP_NOTIFICATION_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_MEETING_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_MEETING_INVITATION_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_MEETING_NOTIFICATION_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_MEETING_PROPOSAL_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_MEETING_PROPOSAL_RESULT_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_NOTIFICATION_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.TAG_NOTIFICATION_UTILS;
import static sakuraiandco.com.gtcollab.constants.Constants.GROUP_INVITATION;
import static sakuraiandco.com.gtcollab.constants.Constants.GROUP_NOTIFICATION;
import static sakuraiandco.com.gtcollab.constants.Constants.MEETING_INVITATION;
import static sakuraiandco.com.gtcollab.constants.Constants.MEETING_NOTIFICATION;
import static sakuraiandco.com.gtcollab.constants.Constants.MEETING_PROPOSAL;
import static sakuraiandco.com.gtcollab.constants.Constants.MEETING_PROPOSAL_RESULT;
import static sakuraiandco.com.gtcollab.constants.Constants.NOTIFICATION_ICON;
import static sakuraiandco.com.gtcollab.constants.Constants.STANDARD_NOTIFICATION;

/**
 * Created by kaliq on 10/31/2017.
 */

public class NotificationUtils {

    public static void handleNotification(Context context, Map<String, String> data) {

        if (data.size() > 0) {
            // Notification
            int id = Integer.valueOf(data.get("id"));
            int type = Integer.valueOf(data.get("type"));
            String title = data.get("title");
            String message = data.get("message");
            String messageExpanded = data.get("message_expanded");
            String creatorFirstName = data.get("creator_first_name");

            int groupId = -1;
            int meetingId = -1;
            String courseShortName = null;

            String location = null;
            LocalDate startDate = null; // TODO: cleanup
            LocalTime startTime = null; // TODO: cleanup

            int meetingProposalId = -1;

            Log.d(TAG_NOTIFICATION_UTILS, "Message type: " + type); // TODO: remove
            if (type == STANDARD_NOTIFICATION) {
                Log.d(TAG_NOTIFICATION_UTILS, "Standard Notification"); // TODO: remove
                dispatchStandardNotification(context, id, title, message, messageExpanded, creatorFirstName);
            } else {
                courseShortName = data.get("course_short_name");
                if (type == GROUP_NOTIFICATION || type == GROUP_INVITATION) {
                    groupId = Integer.valueOf(data.get("group_id"));
                    if (type == GROUP_NOTIFICATION) {
                        Log.d(TAG_NOTIFICATION_UTILS, "Group Notification"); // TODO: remove
                        dispatchGroupNotification(context, id, title, message, messageExpanded, creatorFirstName, courseShortName, groupId);
                    } else {
                        Log.d(TAG_NOTIFICATION_UTILS, "Group Invitation"); // TODO: remove
                        dispatchGroupInvitation(context, id, title, message, messageExpanded, creatorFirstName, courseShortName, groupId);
                    }
                } else if (type == MEETING_NOTIFICATION || type == MEETING_INVITATION) {
                    meetingId = Integer.valueOf(data.get("meeting_id"));
                    if (type == MEETING_NOTIFICATION) {
                        Log.d(TAG_NOTIFICATION_UTILS, "Meeting Notification"); // TODO: remove
                        dispatchMeetingNotification(context, id, title, message, messageExpanded, creatorFirstName, courseShortName, meetingId);
                    } else {
                        Log.d(TAG_NOTIFICATION_UTILS, "Meeting Invitation"); // TODO: remove
                        dispatchMeetingInvitation(context, id, title, message, messageExpanded, creatorFirstName, courseShortName, meetingId);
                    }
                } else if (type == MEETING_PROPOSAL || type == MEETING_PROPOSAL_RESULT) {
                    location = data.get("location");
                    startDate = DateTime.parse(data.get("start_date"), ISODateTimeFormat.yearMonthDay()).toLocalDate();
                    startTime = DateTime.parse(data.get("start_time"), ISODateTimeFormat.hourMinuteSecond()).toLocalTime();
                    if (type == MEETING_PROPOSAL) {
                        Log.d(TAG_NOTIFICATION_UTILS, "Meeting Proposal"); // TODO: remove
                        courseShortName = data.get("course_short_name");
                        dispatchMeetingProposal(context, id, title, message, messageExpanded, creatorFirstName, courseShortName, meetingId, location, startDate, startTime);
                    } else {
                        Log.d(TAG_NOTIFICATION_UTILS, "Meeting Proposal Result"); // TODO: remove
                        meetingProposalId = Integer.valueOf(data.get("meeting_proposal_id"));
                        dispatchMeetingProposalResult(context, id, title, message, messageExpanded, creatorFirstName, courseShortName, meetingId, location, startDate, startTime);
                    }
                } else {

                }
            }

        } else {
            // TODO: something went wrong; all messages should have data
        }

    }

    private static void showNotification(Context context, Notification n, int notificationID) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(notificationID, n);
        } else {
            Log.d(TAG_NOTIFICATION_UTILS, "NotificationManager not found");
        }
    }

    private static void dispatchStandardNotification(Context context, int id, String title, String message, String messageExpanded, String creatorFirstName) {
        int notificationID = (int) System.currentTimeMillis();
        Notification n = buildStandardNotification(context, notificationID, id, title, message, messageExpanded, creatorFirstName);
        showNotification(context, n, notificationID);
    }

    private static void dispatchGroupNotification(final Context context, final int id, final String title, final String message, final String messageExpanded, final String creatorFirstName, final String courseShortName, final int groupId) {
        GroupDAO groupDAO = new GroupDAO(new BaseDAO.Listener<Group>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Group> groups) {}
            @Override
            public void onObjectReady(Group group) {
                int notificationID = (int) System.currentTimeMillis();
                Notification n = buildGroupNotification(context, notificationID, id, title, message, messageExpanded, creatorFirstName, courseShortName, group);
                showNotification(context, n, notificationID);
            }
            @Override
            public void onObjectDeleted() {}
        });
        groupDAO.get(groupId);
    }

    private static void dispatchMeetingNotification(final Context context, final int id, final String title, final String message, final String messageExpanded, final String creatorFirstName, final String courseShortName, final int meetingId) {
        MeetingDAO meetingDAO = new MeetingDAO(new BaseDAO.Listener<Meeting>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Meeting> meetings) {}
            @Override
            public void onObjectReady(Meeting meeting) {
                int notificationID = (int) System.currentTimeMillis();
                Notification n = buildMeetingNotification(context, notificationID, id, title, message, messageExpanded, creatorFirstName, courseShortName, meeting);
                showNotification(context, n, notificationID);
            }
            @Override
            public void onObjectDeleted() {}
        });
        meetingDAO.get(meetingId);
    }

    private static void dispatchGroupInvitation(final Context context, final int id, final String title, final String message, final String messageExpanded, final String creatorFirstName, final String courseShortName, final int groupId) {
        GroupDAO groupDAO = new GroupDAO(new BaseDAO.Listener<Group>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Group> groups) {}
            @Override
            public void onObjectReady(Group group) {
                int notificationID = (int) System.currentTimeMillis();
                Notification n = buildGroupInvitation(context, notificationID, id, title, message, messageExpanded, creatorFirstName, courseShortName, group);
                showNotification(context, n, notificationID);
            }
            @Override
            public void onObjectDeleted() {}
        });
        groupDAO.get(groupId);
    }

    private static void dispatchMeetingInvitation(final Context context, final int id, final String title, final String message, final String messageExpanded, final String creatorFirstName, final String courseShortName, final int meetingId) {
        MeetingDAO meetingDAO = new MeetingDAO(new BaseDAO.Listener<Meeting>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Meeting> meetings) {}
            @Override
            public void onObjectReady(Meeting meeting) {
                int notificationID = (int) System.currentTimeMillis();
                Notification n = buildMeetingInvitation(context, notificationID, id, title, message, messageExpanded, creatorFirstName, courseShortName, meeting);
                showNotification(context, n, notificationID);
            }
            @Override
            public void onObjectDeleted() {}
        });
        meetingDAO.get(meetingId);
    }

    private static void dispatchMeetingProposal(final Context context, final int id, final String title, final String message, final String messageExpanded, final String creatorFirstName, final String courseShortName, final int meetingId, final String location, final LocalDate startDate, final LocalTime startTime) {
        MeetingDAO meetingDAO = new MeetingDAO(new BaseDAO.Listener<Meeting>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Meeting> meetings) {}
            @Override
            public void onObjectReady(Meeting meeting) {
                int notificationID = (int) System.currentTimeMillis();
                Notification n = buildMeetingProposal(context, notificationID, id, title, message, messageExpanded, creatorFirstName, courseShortName, meeting, location, startDate, startTime);
                showNotification(context, n, notificationID);
            }
            @Override
            public void onObjectDeleted() {}
        });
        meetingDAO.get(meetingId);
    }

    private static void dispatchMeetingProposalResult(final Context context, final int id, final String title, final String message, final String messageExpanded, final String creatorFirstName, final String courseShortName, final int meetingId, final String location, final LocalDate startDate, final LocalTime startTime) {
        MeetingDAO meetingDAO = new MeetingDAO(new BaseDAO.Listener<Meeting>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Meeting> meetings) {}
            @Override
            public void onObjectReady(Meeting meeting) {
                int notificationID = (int) System.currentTimeMillis();
                Notification n = buildMeetingProposalResult(context, notificationID, id, title, message, messageExpanded, creatorFirstName, courseShortName, meeting, location, startDate, startTime);
                showNotification(context, n, notificationID);
            }
            @Override
            public void onObjectDeleted() {}
        });
        meetingDAO.get(meetingId);
    }

    private static Notification buildStandardNotification(Context context, int notificationID, int id, String title, String message, String messageExpanded, String creatorFirstName) {
        return buildNotification(context, title, message, messageExpanded, null);
    }

    private static Notification buildGroupNotification(Context context, int notificationID, int id, String title, String message, String messageExpanded, String creatorFirstName, String courseShortName, Group group) {
        return buildNotification(context, title, message, messageExpanded, buildContentPendingIntent(context, EXTRA_GROUP_NOTIFICATION_ID, id));
    }

    private static Notification buildMeetingNotification(Context context, int notificationID, int id, String title, String message, String messageExpanded, String creatorFirstName, String courseShortName, Meeting meeting) {
        return buildNotification(context, title, message, messageExpanded, buildContentPendingIntent(context, EXTRA_MEETING_NOTIFICATION_ID, id));
    }

    private static Notification buildGroupInvitation(Context context, int notificationID, int id, String title, String message, String messageExpanded, String creatorFirstName, String courseShortName, Group group) {
        Bundle extras = new Bundle();
        extras.putInt(EXTRA_GROUP_ID, group.getId());
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText(messageExpanded);
        Notification n = getNotificationBuilder(context, NOTIFICATION_ICON, title, message, bigTextStyle, buildContentPendingIntent(context, EXTRA_GROUP_INVITATION_ID, id))
                .addAction(NOTIFICATION_ICON, "Join", buildActionPendingIntent(context, ACTION_GROUP_INVITATION_ACCEPT, notificationID, extras))
                .addAction(NOTIFICATION_ICON, "Ignore", buildActionPendingIntent(context, ACTION_GROUP_INVITATION_IGNORE, notificationID, extras))
                .build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        return n;
    }

    private static Notification buildMeetingInvitation(Context context, int notificationID, int id, String title, String message, String messageExpanded, String creatorFirstName, String courseShortName, Meeting meeting) {
        Bundle extras = new Bundle();
        extras.putInt(EXTRA_MEETING_ID, meeting.getId());
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText(messageExpanded);
        Notification n = getNotificationBuilder(context, NOTIFICATION_ICON, title, message, bigTextStyle, buildContentPendingIntent(context, EXTRA_MEETING_INVITATION_ID, id))
                .addAction(NOTIFICATION_ICON, "Join", buildActionPendingIntent(context, ACTION_MEETING_INVITATION_ACCEPT, notificationID, extras))
                .addAction(NOTIFICATION_ICON, "Ignore", buildActionPendingIntent(context, ACTION_MEETING_INVITATION_IGNORE, notificationID, extras))
                .build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        return n;
    }

    private static Notification buildMeetingProposal(Context context, int notificationID, int id, String title, String message, String messageExpanded, String creatorFirstName, String courseShortName, Meeting meeting, String location, LocalDate startDate, LocalTime startTime) {
        Bundle extras = new Bundle();
        extras.putInt(EXTRA_MEETING_PROPOSAL_ID, id);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText(messageExpanded);
        Notification n = getNotificationBuilder(context, NOTIFICATION_ICON, title, message, bigTextStyle, buildContentPendingIntent(context, EXTRA_MEETING_PROPOSAL_ID, id))
                .addAction(NOTIFICATION_ICON, "Accept", buildActionPendingIntent(context, ACTION_MEETING_PROPOSAL_ACCEPT, notificationID, extras))
                .addAction(NOTIFICATION_ICON, "Reject", buildActionPendingIntent(context, ACTION_MEETING_PROPOSAL_REJECT, notificationID, extras))
                .build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        return n;
    }

    private static Notification buildMeetingProposalResult(Context context, int notificationID, int id, String title, String message, String messageExpanded, String creatorFirstName, String courseShortName, Meeting meeting, String location, LocalDate startDate, LocalTime startTime) {
        return buildNotification(context, title, message, messageExpanded, buildContentPendingIntent(context, EXTRA_MEETING_PROPOSAL_RESULT_ID, id));
    }

    private static PendingIntent buildActionPendingIntent(Context context, String action, int notificationID, Bundle extras) {
        Intent intent = new Intent(context, NotificationActionService.class);
        intent.setAction(action);
        intent.putExtra(EXTRA_NOTIFICATION_ID, notificationID);
        intent.putExtras(extras);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent buildContentPendingIntent(Context context, String objectIdKey, int objectIdValue) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationsActivity.class);
        Intent intent = new Intent(context, NotificationsActivity.class);
        intent.putExtra(objectIdKey, objectIdValue);
        stackBuilder.addNextIntent(intent);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Notification buildNotification(Context context, String title, String message, String messageExpanded, PendingIntent contentPendengIntent) {
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText(messageExpanded);
        Notification n = getNotificationBuilder(context, NOTIFICATION_ICON, title, message, bigTextStyle, contentPendengIntent).build();
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
