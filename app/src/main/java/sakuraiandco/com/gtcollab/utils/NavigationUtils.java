package sakuraiandco.com.gtcollab.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.activities.CourseActivity;
import sakuraiandco.com.gtcollab.activities.CourseListActivity;
import sakuraiandco.com.gtcollab.activities.CourseSearchActivity;
import sakuraiandco.com.gtcollab.activities.CreateGroupActivity;
import sakuraiandco.com.gtcollab.activities.CreateMeetingActivity;
import sakuraiandco.com.gtcollab.activities.GroupChatActivity;
import sakuraiandco.com.gtcollab.activities.LoginActivity;
import sakuraiandco.com.gtcollab.activities.RegisterActivity;
import sakuraiandco.com.gtcollab.activities.SubjectSearchActivity;
import sakuraiandco.com.gtcollab.activities.TermListActivity;
import sakuraiandco.com.gtcollab.activities.UserListActivity;
import sakuraiandco.com.gtcollab.activities.UserSelectActivity;
import sakuraiandco.com.gtcollab.domain.Course;
import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.domain.Meeting;
import sakuraiandco.com.gtcollab.domain.Subject;
import sakuraiandco.com.gtcollab.domain.Term;
import sakuraiandco.com.gtcollab.domain.User;

import static sakuraiandco.com.gtcollab.constants.Arguments.AUTH_TOKEN;
import static sakuraiandco.com.gtcollab.constants.Arguments.CURRENT_USER;
import static sakuraiandco.com.gtcollab.constants.Arguments.DEFAULT_SHARED_PREFERENCES;
import static sakuraiandco.com.gtcollab.constants.Arguments.DEVICE_REGISTRATION_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_COURSE;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_COURSE_TAB;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_GROUP;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_MEETING;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_SEARCH_RESULTS;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_SELECTED_USERS;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_SUBJECT;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_TERM;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_USER;
import static sakuraiandco.com.gtcollab.constants.Constants.TAB_MEETINGS;

/**
 * Created by kaliq on 10/31/2017.
 */

public class NavigationUtils {

    // TODO: order of arguments?

    public static void login(Context context) {
        clearCookies(context);
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public static void logout(Context context) {
        clearCookies(context);
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public static void register(Context context) {
        clearCookies(context);
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    public static void startCourseListActivity(Context context, User user, Term term) {
        Intent intent = new Intent(context, CourseListActivity.class);
        startActivityWithUserAndTerm(context, intent, user, term);
    }

    public static void startCourseActvitiy(Context context, User user, Term term, Course course) {
        startCourseActvitiy(context, user, term, course, TAB_MEETINGS);
    }

    public static void startCourseActvitiy(Context context, User user, Term term, Course course, int courseTab) {
        Intent intent = new Intent(context, CourseActivity.class);
        intent.putExtra(EXTRA_COURSE_TAB, courseTab);
        startActivityWithUserAndTermAndCourse(context, intent, user, term, course);
    }

    public static void startTermActivity(Context context, User user) {
        Intent intent = new Intent(context, TermListActivity.class);
        startActivityWithUser(context, intent, user);
    }

    public static void startSubjectSearchActivity(Context context, User user, Term term) {
        Intent intent = new Intent(context, SubjectSearchActivity.class);
        startActivityWithUserAndTerm(context, intent, user, term);
    }

    public static void startCourseSearchActivity(Context context, User user, Term term, Subject subject, List<Course> searchResults) {
        Intent intent = new Intent(context, CourseSearchActivity.class);
        intent.putExtra(EXTRA_SUBJECT, subject);
        if (searchResults != null) {
            intent.putParcelableArrayListExtra(EXTRA_SEARCH_RESULTS, new ArrayList<>(searchResults));
        }
        startActivityWithUserAndTerm(context, intent, user, term);
    }

    public static void startUserListActivity(Context context, User user, Term term, Course course, Group group, Meeting meeting) {
        Intent intent = new Intent(context, UserListActivity.class);
        if (group != null) {
            intent.putExtra(EXTRA_GROUP, group);
        }
        if (meeting != null) {
            intent.putExtra(EXTRA_MEETING, meeting);
        }
        startActivityWithUserAndTermAndCourse(context, intent, user, term, course);
    }

    public static void startUserSelectActivityForResult(Context context, User user, Term term, Course course, List<User> selectedUsers, int requestCode) { // TODO: clean/refactor
        Intent intent = new Intent(context, UserSelectActivity.class);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_TERM, term);
        intent.putExtra(EXTRA_COURSE, course);
        intent.putParcelableArrayListExtra(EXTRA_SELECTED_USERS, new ArrayList<>(selectedUsers));
        ((AppCompatActivity) context).startActivityForResult(intent, requestCode);
    }

    public static void startCreateMeetingActivity(Context context, User user, Term term, Course course) {
        Intent intent = new Intent(context, CreateMeetingActivity.class);
        startActivityWithUserAndTermAndCourse(context, intent, user, term, course);
    }

    public static void startCreateGroupActivity(Context context, User user, Term term, Course course) {
        Intent intent = new Intent(context, CreateGroupActivity.class);
        startActivityWithUserAndTermAndCourse(context, intent, user, term, course);
    }

    public static void startGroupChatActivity(Context context, User user, Group group) { // TODO: review
        Intent intent = new Intent(context, GroupChatActivity.class);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra("group", group);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startNotificationsActivity(Context context, User user, Term term) {
//        Intent intent = new Intent(context, NotificationsActivity.class);
//        startActivityWithUserAndTerm(context, intent, user, term);
    }

    /**************** Helper Methods **************************************************************/

    private static void startActivityWithUserAndTermAndCourse(Context context, Intent intent, User user, Term term, Course course) {
        intent.putExtra(EXTRA_COURSE, course);
        startActivityWithUserAndTerm(context, intent, user, term);
    }

    private static void startActivityWithUserAndTerm(Context context, Intent intent, User user, Term term) {
        intent.putExtra(EXTRA_TERM, term);
        startActivityWithUser(context, intent, user);
    }

    private static void startActivityWithUser(Context context, Intent intent, User user) {
        intent.putExtra(EXTRA_USER, user);
        context.startActivity(intent);
    }

    private static void clearCookies(Context context) {
        context.getSharedPreferences(DEFAULT_SHARED_PREFERENCES, 0).edit().remove(AUTH_TOKEN).remove(CURRENT_USER).remove(DEVICE_REGISTRATION_ID).apply();
    }

}
