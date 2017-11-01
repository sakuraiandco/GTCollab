package sakuraiandco.com.gtcollab.utils;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;

import sakuraiandco.com.gtcollab.CourseListActivity;
import sakuraiandco.com.gtcollab.LoginActivity;
import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.domain.Meeting;
import sakuraiandco.com.gtcollab.rest.GroupDAO;
import sakuraiandco.com.gtcollab.rest.MeetingDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.rest.base.DAOListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_1;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_2;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_3;
import static sakuraiandco.com.gtcollab.constants.Arguments.ACTION_4;

/**
 * Created by kaliq on 10/31/2017.
 */

public class NotificationUtils {

    public static void showTestNotification(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationChannel mChannel = new NotificationChannel("default_channel", getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT);
//        mNotificationManager.createNotificationChannel(mChannel);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Expanded View");
        bigTextStyle.bigText("A huge block of text\nA huge block of text\nA huge block of text\nA huge block of text\nA huge block of text\n");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(new Intent(context, LoginActivity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent action1Intent = new Intent(context, NotificationActionService.class);
        action1Intent.setAction(ACTION_1);
        PendingIntent actionPendingIntent = PendingIntent.getService(context, 1, action1Intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification n = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_sentiment_neutral_black_24dp)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(bigTextStyle)
                .setContentIntent(resultPendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

                .addAction(R.drawable.ic_sentiment_neutral_black_24dp, "Action", actionPendingIntent)
                .addAction(R.drawable.ic_sentiment_neutral_black_24dp, "Action 2", actionPendingIntent)
                .build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        mNotificationManager.notify(123, n);
    }

    public static void notifyGroupInvitation(final Context context, final int groupId, final String courseShortName, final String creatorFirstName) {
        GroupDAO groupDAO = new GroupDAO(new DAOListener<Group>() { // TODO: make synchronous; use synchronous Volley request?
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Group> groups) {}
            @Override
            public void onObjectReady(Group group) {
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                String content = courseShortName + ": " + creatorFirstName + " has invited you to their group!";
                String bigContent = content
                        + "\n\n" + group.getName();

                // NOTE: loses 32 bits of data
                int notificationId =  (int) System.currentTimeMillis();

                // Default Action
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(LoginActivity.class); // TODO: set to activity where user can see pending invitations / proposals
                stackBuilder.addNextIntent(new Intent(context, LoginActivity.class));
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                // Join Meeting Action
                Intent joinGroupIntent = new Intent(context, NotificationActionService.class);
                joinGroupIntent.setAction(ACTION_1);
                joinGroupIntent.putExtra("groupId", groupId); // TODO: refactor key
                joinGroupIntent.putExtra("notificationId", notificationId);
                PendingIntent joinMeetingPendingIntent = PendingIntent.getService(context, 0, joinGroupIntent, PendingIntent.FLAG_UPDATE_CURRENT); // TODO: unique request codes?

                // Ignore Invite Action
                Intent ignoreInviteIntent = new Intent(context, NotificationActionService.class);
                ignoreInviteIntent.setAction(ACTION_1);
                ignoreInviteIntent.putExtra("notificationId", notificationId);
                PendingIntent ignoreInvitePendingIntent = PendingIntent.getService(context, 1, ignoreInviteIntent, PendingIntent.FLAG_UPDATE_CURRENT); // TODO: unique request codes?

                // Expanded Content
                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                bigTextStyle.bigText(bigContent);

                Notification n = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_sentiment_neutral_black_24dp)
                        .setContentTitle("Group Invitation")
                        .setContentText(content)
                        .setStyle(bigTextStyle)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setContentIntent(resultPendingIntent)
                        .addAction(R.drawable.ic_sentiment_neutral_black_24dp, "Join", joinMeetingPendingIntent)
                        .addAction(R.drawable.ic_sentiment_neutral_black_24dp, "Ignore", ignoreInvitePendingIntent)
                        .build();
                n.flags |= Notification.FLAG_AUTO_CANCEL;
                mNotificationManager.notify((int) System.currentTimeMillis(), n); // TODO: custom notification ID - unique for each invitation? if-so use unique request codes for pendingIntents too
            }
        });
        groupDAO.get(groupId);
    }

    public static void notifyMeetingInvitation(final Context context, final int meetingId, final String courseShortName, final String creatorFirstName) {
        MeetingDAO meetingDAO = new MeetingDAO(new DAOListener<Meeting>() { // TODO: make synchronous; use synchronous Volley request?
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Meeting> meetings) {}
            @Override
            public void onObjectReady(Meeting meeting) {
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                String content = String.format(
                        "%s: %s has invited you to their meeting!\n\n" +
                        "Name: %s\n" +
                                "Location: %s\n" +
                                "Start Date: %s\n" +
                                "Start Time: %s\n" +
                                "Duration: %d minutes\n" +
                                "Description: %s", courseShortName, creatorFirstName, meeting.getName(), meeting.getLocation(), meeting.getStartDate().toString("EEE MMM dd"), meeting.getStartTime().toString("h:mm a"), meeting.getDurationMinutes(), meeting.getDescription());

                // NOTE: loses 32 bits of data
                int notificationId =  (int) System.currentTimeMillis();

                // Default Action
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(LoginActivity.class); // TODO: set to activity where user can see pending invitations / proposals
                stackBuilder.addNextIntent(new Intent(context, LoginActivity.class));
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                // Join Meeting Action
                Intent joinMeetingIntent = new Intent(context, NotificationActionService.class);
                joinMeetingIntent.setAction(ACTION_2);
                joinMeetingIntent.putExtra("meetingId", meetingId); // TODO: refactor key
                joinMeetingIntent.putExtra("notificationId", notificationId);

                PendingIntent joinMeetingPendingIntent = PendingIntent.getService(context, 0, joinMeetingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                // Ignore Invite Action
                Intent ignoreInviteIntent = new Intent(context, NotificationActionService.class);
                ignoreInviteIntent.setAction(ACTION_2);
                ignoreInviteIntent.putExtra("notificationId", notificationId);
                PendingIntent ignoreInvitePendingIntent = PendingIntent.getService(context, 1, ignoreInviteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                // Expanded Content
                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                bigTextStyle.bigText(content);

                Notification n = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_sentiment_neutral_black_24dp)
                        .setContentTitle("Meeting Invitation")
                        .setContentText(content)
                        .setStyle(bigTextStyle)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setContentIntent(resultPendingIntent)
                        .addAction(R.drawable.ic_sentiment_neutral_black_24dp, "Join", joinMeetingPendingIntent)
                        .addAction(R.drawable.ic_sentiment_neutral_black_24dp, "Ignore", ignoreInvitePendingIntent)
                        .build();
                n.flags |= Notification.FLAG_AUTO_CANCEL;
                mNotificationManager.notify(notificationId, n); // TODO: custom notification ID - unique for each invitation?
            }
        });
        meetingDAO.get(meetingId);
    }

    public static void notifyAddedToGroup(final Context context, final int groupId, final String courseShortName, final String creatorFirstName) {
        GroupDAO groupDAO = new GroupDAO(new DAOListener<Group>() { // TODO: make synchronous; use synchronous Volley request?
            @Override
            public void onDAOError(BaseDAO.Error error) {}
            @Override
            public void onListReady(List<Group> groups) {}
            @Override
            public void onObjectReady(Group group) {
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                String content = courseShortName + ": " + creatorFirstName + " has added you to their group!";
                String bigContent = content
                        + "\n\n" + group.getName();

                // NOTE: loses 32 bits of data
                int notificationId =  (int) System.currentTimeMillis();

                // Default Action
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(LoginActivity.class); // TODO: set to activity where user can see pending invitations / proposals
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("notificationId", notificationId);
                stackBuilder.addNextIntent(intent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                // Expanded Content
                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                bigTextStyle.bigText(bigContent);

                Notification n = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_sentiment_neutral_black_24dp)
                        .setContentTitle("Group Invitation")
                        .setContentText(content)
                        .setStyle(bigTextStyle)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setContentIntent(resultPendingIntent)
                        .build();
                n.flags |= Notification.FLAG_AUTO_CANCEL;
                mNotificationManager.notify(notificationId, n); // TODO: custom notification ID - unique for each invitation? if-so use unique request codes for pendingIntents too
            }
        });
        groupDAO.get(groupId);
    }

    public static class NotificationActionService extends IntentService {

        GroupDAO groupDAO;
        MeetingDAO meetingDAO;

        public NotificationActionService() {
            super(NotificationActionService.class.getSimpleName());
            groupDAO = new GroupDAO(new DAOListener<Group>() { // TODO: open app to course page to display success / error dialog (?)
                @Override
                public void onDAOError(BaseDAO.Error error) {}
                @Override
                public void onListReady(List<Group> groups) {}
                @Override
                public void onObjectReady(Group group) {}
            });
            meetingDAO = new MeetingDAO(new DAOListener<Meeting>() { // TODO: open app to cousre page to display success / error dialog (?)
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
            });
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            Log.d("NOTIFICATION_ACTION", "Made it here");
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String action = intent.getAction();
            manager.cancel(intent.getIntExtra("notificationId", -1)); // TODO
            switch (action) {
                case ACTION_1: // Join Group
                    int groupId = intent.getIntExtra("groupId", -1);
                    if (groupId != -1) {
                        groupDAO.joinGroup(groupId);
                    }
                    break;
                case ACTION_2: // Join Meeting
                    int meetingId = intent.getIntExtra("meetingId", -1);
                    if (meetingId != -1) {
                        meetingDAO.joinMeeting(meetingId);
                    }
                    break;
                case ACTION_3:
                    break;
                case ACTION_4: // Meeting Proposal
                    break;
                default:
            }
            // manager.cancel(NOTIFICATION_ID);
            // If you want to cancel the notification: NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
        }
    }
}
