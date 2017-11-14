package sakuraiandco.com.gtcollab.constants;

/**
 * Created by kaliq on 10/17/2017.
 */

public class Arguments {

    // Filter query parameters (strict)

    public static final String FILTER_USERNAME = "username";
    public static final String FILTER_TERM = "term";
    public static final String FILTER_SUBJECT = "subject";
    public static final String FILTER_COURSE = "course";
    public static final String FILTER_GROUP = "group";
    public static final String FILTER_MEETING = "meeting";
    public static final String FILTER_MEMBERS = "members";
    public static final String FILTER_COURSES_AS_MEMBER = "courses_as_member";
    public static final String FILTER_GROUPS_AS_MEMBER = "groups_as_member";
    public static final String FILTER_MEETINGS_AS_MEMBER = "meetings_as_member";

    // Intent extras (arbitrary)

    public static final String EXTRA_USER = "user";
    public static final String EXTRA_TERM = "term";
    public static final String EXTRA_SUBJECT = "subject";
    public static final String EXTRA_COURSE = "course";
    public static final String EXTRA_GROUP = "group";
    public static final String EXTRA_MEETING = "meeting";
    public static final String EXTRA_GROUP_INVITATION = "groupInvitation";
    public static final String EXTRA_MEETING_INVITATION = "meetingInvitation";
    public static final String EXTRA_COURSE_MESSAGE = "courseMessage";
    public static final String EXTRA_GROUP_MESSAGE = "groupMessage";
    public static final String EXTRA_SEARCH_RESULTS = "searchResults";
    public static final String EXTRA_SELECTED_USERS = "selectedUsers";
    public static final String EXTRA_COURSE_TAB = "courseTab";

    // Shared Preferences arguments (arbitrary)

    public static final String DEFAULT_SHARED_PREFERENCES = "default_shared_preference";
    public static final String AUTH_TOKEN = "auth_token";
    public static final String CURRENT_USER = "current_user";
    public static final String DEVICE_REGISTRATION_ID = "device_registration_id";

    // Intent actions for notifications (arbitrary)

    public static final String ACTION_1  = "action_1"; // TODO
    public static final String ACTION_2  = "action_2"; // TODO
    public static final String ACTION_3  = "action_3"; // TODO
    public static final String ACTION_4  = "action_4"; // TODO

    // startActivityForResult()
    public static final int DEFAULT_REQUEST_CODE = 0;
    public static final int DEFAULT_RESULT_CODE = 0;
}
