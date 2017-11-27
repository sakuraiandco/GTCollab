package sakuraiandco.com.gtcollab.constants;

import sakuraiandco.com.gtcollab.R;

/**
 * Created by kaliq on 10/15/2017.
 */

public abstract class Constants {

    // REST API
    public static final String HOST_NAME = "https://gtcollab.herokuapp.com";
    public static final String BASE_URL = HOST_NAME + "/api";
    public static final String AUTH_HEADER = "Authorization";

    public static final String CURR_TERM = "1"; // TODO: obtain programatically via api/terms/current/ endpoint

    // Course activity tabs
    public static final int TAB_MEETINGS = 0;
    public static final int TAB_GROUPS = 1;

    // Notification types
    public static final int STANDARD_NOTIFICATION = 1;
    public static final int GROUP_NOTIFICATION = 2;
    public static final int MEETING_NOTIFICATION = 3;
    public static final int GROUP_INVITATION = 4;
    public static final int MEETING_INVITATION = 5;
    public static final int MEETING_PROPOSAL = 6;
    public static final int MEETING_PROPOSAL_RESULT = 7;

    // Icons
    public final static int NOTIFICATION_ICON = R.drawable.ic_sentiment_neutral_black_24dp;

}
