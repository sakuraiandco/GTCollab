package sakuraiandco.com.gtcollab;

/**
 * Created by Alex on 10/14/17.
 */

final class Singleton {
    private static final Singleton instance = new Singleton();

    // TODO: replace hardcoding once login implemented
    private static TempUser tempUser = new TempUser(1, "admin", "Token ae58c6766f9152f8ffe0a143382f4121fbf6e3cb");
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
