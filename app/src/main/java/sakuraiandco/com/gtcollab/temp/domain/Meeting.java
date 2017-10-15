package sakuraiandco.com.gtcollab.temp.domain;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

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
public class Meeting extends Entity {

    public static final String BASE_URL = Constants.BASE_URL + "/meetings/";

    int id;
    String name;
    String description;
    LocalDate startDate;
    LocalTime startTime;
    int durationMinutes;
    User creator;
    List<User> members;

}
