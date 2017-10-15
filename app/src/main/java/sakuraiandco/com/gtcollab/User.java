package sakuraiandco.com.gtcollab;

/**
 * Created by Alex on 10/14/17.
 */

public class User {
    // TODO: consider replacing with something more secure

    private int id;
    private String username;
    private String authorization;

    public User(int id, String username, String authorization) {
        this.id = id;
        this.username = username;
        this.authorization = authorization;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthorization() {
        return authorization;
    }

}
