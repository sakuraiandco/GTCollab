package sakuraiandco.com.gtcollab.domain;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.Data;
import sakuraiandco.com.gtcollab.constants.Constants;

/**
 * Created by kaliq on 10/14/2017.
 */

@Data
@Builder
public class GroupMessage extends Entity {

    public static final String BASE_URL = Constants.BASE_URL + "/group-messages/";

    int id;
    String content;
    int groupId;
    User creator;
    DateTime timestamp;

}
