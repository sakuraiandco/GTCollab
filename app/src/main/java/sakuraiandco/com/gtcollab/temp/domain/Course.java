package sakuraiandco.com.gtcollab.temp.domain;

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
public class Course extends Entity {

    public static final String BASE_URL = Constants.BASE_URL + "/courses/";

    int id;
    String name;
    String subject_code;
    String course_number;
    List<String> sections;
    List<MeetingTime> meetingTimes;
    int numMembers;
    boolean isCancelled;

    @Data
    @Builder
    public static class MeetingTime {
        String meetDays;
        LocalTime startTime;
        LocalTime endTime;
    }

}
