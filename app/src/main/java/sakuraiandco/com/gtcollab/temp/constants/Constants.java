package sakuraiandco.com.gtcollab.temp.constants;

/**
 * Created by kaliq on 10/15/2017.
 */

public abstract class Constants {

    // REST API endpoints
    public static final String HOST_NAME = "https://secure-headland-60131.herokuapp.com";
    public static final String BASE_URL = HOST_NAME + "/api";
    public static final String AUTH_URL = BASE_URL + "/api-token-auth/";
    public static final String SERVER_STATUS_URL = BASE_URL + "/server-status/";

    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_TOKEN_FILE = "api_auth_token";
    public static final String AUTH_TOKEN_KEY = "authToken";
    public static final String CURRENT_USER_KEY = "currUser";

}
