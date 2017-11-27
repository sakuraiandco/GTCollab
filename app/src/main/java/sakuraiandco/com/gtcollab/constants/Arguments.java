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
    public static final String EXTRA_GROUP_INVITATION = "group_invitation";
    public static final String EXTRA_MEETING_INVITATION = "meeting_invitation";
    public static final String EXTRA_COURSE_MESSAGE = "course_message";
    public static final String EXTRA_GROUP_MESSAGE = "group_message";
    public static final String EXTRA_SEARCH_RESULTS = "search_results";
    public static final String EXTRA_SELECTED_USERS = "selected_users";
    public static final String EXTRA_COURSE_TAB = "course_tab";

    public static final String EXTRA_GROUP_NOTIFICATION_ID = "group_notification_id";
    public static final String EXTRA_MEETING_NOTIFICATION_ID = "meeting_notification_id";
    public static final String EXTRA_GROUP_INVITATION_ID = "group_invitation_id";
    public static final String EXTRA_MEETING_INVITATION_ID = "meeting_invitation_id";
    public static final String EXTRA_MEETING_PROPOSAL_ID = "meeting_proposal_id";
    public static final String EXTRA_MEETING_PROPOSAL_RESULT_ID = "meeting_proposal_result_id";

    public static final String EXTRA_GROUP_ID = "group_id";
    public static final String EXTRA_MEETING_ID = "meeting_id";

    public static final String EXTRA_MEETING_PROPOSAL_RESPONSE = "meeting_proposal_response";
    public static final int ACCEPTED = 1;
    public static final int REJECTED = 2;

    public static final String EXTRA_NOTIFICATION_ID = "notification_id";

    public static final String EXTRA_DISPLAY_BACK_BUTTON = "display_back_button";

        // search
    public static final String EXTRA_CALLING_ACTIVITY = "calling_activity";
    public static final int SUBJECT_SEARCH_ACTIVITY = 1;
    public static final int COURSE_SEARCH_ACTIVITY = 2;
    public static final int COURSE_ACTIVITY = 3;

    // Shared Preferences arguments (arbitrary)

    public static final String DEFAULT_SHARED_PREFERENCES = "default_shared_preference";
    public static final String AUTH_TOKEN = "auth_token";
    public static final String CURRENT_USER = "current_user";
    public static final String DEVICE_REGISTRATION_ID = "device_registration_id";
    public static final String LAST_OPENED_COURSE = "last_opened_course";

    // Intent actions for notifications (arbitrary)

    public static final String ACTION_GROUP_INVITATION_ACCEPT  = "action_group_invitation_accept";
    public static final String ACTION_GROUP_INVITATION_IGNORE  = "action_group_invitation_ignore";
    public static final String ACTION_MEETING_INVITATION_ACCEPT  = "action_meeting_invitation_accept";
    public static final String ACTION_MEETING_INVITATION_IGNORE  = "action_meeting_invitation_ignore";
    public static final String ACTION_MEETING_PROPOSAL_ACCEPT  = "action_meeting_proposal_accept";
    public static final String ACTION_MEETING_PROPOSAL_REJECT  = "action_meeting_proposal_reject";

    // startActivityForResult()
    public static final int DEFAULT_REQUEST_CODE = 0;
    public static final int DEFAULT_RESULT_CODE = 0;

    // Tags TODO
    public static final String TAG_MEETING_PROPOSAL_DIALOG = "meeting_proposal_dialog";
    public final static String TAG_NOTIFICATION_UTILS = "NOTIFICATIONS";

}
