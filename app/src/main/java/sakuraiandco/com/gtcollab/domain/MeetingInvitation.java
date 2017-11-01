package sakuraiandco.com.gtcollab.domain;

import org.joda.time.DateTime;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import sakuraiandco.com.gtcollab.constants.Constants;

/**
 * Created by kaliq on 11/1/2017.
 */

@Data
@Builder
public class MeetingInvitation extends Entity {

    public static final String BASE_URL = Constants.BASE_URL + "/meeting-invitations/";

    int id;
    int meetingId;
    User creator;
    List<Integer> recipients;
    DateTime timestamp;
    
}
