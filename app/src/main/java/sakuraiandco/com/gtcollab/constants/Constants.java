package sakuraiandco.com.gtcollab.constants;

/**
 * Created by kaliq on 10/15/2017.
 */

public abstract class Constants {

    // REST API endpoints
    public static final String HOST_NAME = "https://secure-headland-60131.herokuapp.com";
    public static final String BASE_URL = HOST_NAME + "/api";

    public static final String AUTH_HEADER = "Authorization";

    public static final String CURR_TERM = "1"; // TODO: obtain programatically via api/terms/current/ endpoint

    public static final int TAB_MEETINGS = 0;
    public static final int TAB_GROUPS = 1;

}
