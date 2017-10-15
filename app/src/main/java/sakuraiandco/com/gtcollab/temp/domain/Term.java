package sakuraiandco.com.gtcollab.temp.domain;

import org.joda.time.LocalDate;

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
public class Term extends Entity {

    public static final String BASE_URL = Constants.BASE_URL + "/terms/";

    int id;
    String name;
    String code;
    LocalDate startDate;
    LocalDate endDate;
    boolean subjectsLoaded; // TODO

}