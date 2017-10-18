package sakuraiandco.com.gtcollab.domain;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import sakuraiandco.com.gtcollab.constants.Constants;

/**
 * Created by kaliq on 10/14/2017.
 */

@Data
@Builder
public class Group extends Entity {

    public static final String BASE_URL = Constants.BASE_URL + "/groups/";

    int id;
    String name;
    int courseId;
    User creator;
    List<Integer> members;

}
