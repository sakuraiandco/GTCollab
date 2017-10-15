package sakuraiandco.com.gtcollab;

/**
 * Created by Alex on 10/14/17.
 */

final class Singleton {
    private static final Singleton instance = new Singleton();

    // TODO: replace hardcoding once login implemented
    private static TempUser tempUser = new TempUser(2, "user1", "Token 3193ce9eae1d77c51d133f75edfb4c3df8e2f96a");
    // TODO: consider moving variables elsewhere for organization, and changing authorization to be tempUser specific
    private static String baseURL = "https://secure-headland-60131.herokuapp.com/api/";
    private static final RequestHandler requestHandler = new RequestHandler(baseURL, tempUser.getAuthorization());

    private Singleton() {
    }

    public static TempUser getTempUser() {
        return tempUser;
    }

    static RequestHandler getRequestHandler() {
        return requestHandler;
    }

    static Singleton getInstance() {
        return instance;
    }
}
