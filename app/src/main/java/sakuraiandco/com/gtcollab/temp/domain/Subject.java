package sakuraiandco.com.gtcollab.temp.domain;

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
public class Subject extends Entity {

    public static final String BASE_URL = Constants.BASE_URL + "/subjects/";

    int id;
    String name;
    String code;
    String term_name; // TODO: needed?
    boolean coursesLoaded;

}
