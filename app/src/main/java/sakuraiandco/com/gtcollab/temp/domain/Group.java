package sakuraiandco.com.gtcollab.temp.domain;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import sakuraiandco.com.gtcollab.temp.constants.Constants;

/**
 * Created by kaliq on 10/14/2017.
 */

@Data
@Builder
@EqualsAndHashCode(callSuper=true)
public class Group extends Entity {

    public static final String BASE_URL = Constants.BASE_URL + "/groups/";

    int id;
    String name;
    User creator;
    List<Integer> members;

}
