package sakuraiandco.com.gtcollab.domain;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import sakuraiandco.com.gtcollab.constants.Constants;

/**
 * Created by kaliq on 10/14/2017.
 */

@Data
@Builder
public class Meeting extends Entity {

    public static final String BASE_URL = Constants.BASE_URL + "/meetings/";

    int id;
    String name;
    String location;
    String description;
    LocalDate startDate;
    LocalTime startTime;
    int durationMinutes;
    int courseId;
    User creator;
    List<Integer> members;

}
