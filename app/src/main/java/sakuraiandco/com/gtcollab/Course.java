package sakuraiandco.com.gtcollab;

import java.util.ArrayList;

/**
 * Created by Alex on 10/14/17.
 */

public class Course {
    private int id;
    private String name;

    Course (int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
