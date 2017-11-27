package sakuraiandco.com.gtcollab.rest.factories;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.domain.Term;
import sakuraiandco.com.gtcollab.rest.factories.base.ReadOnlyFactory;

/**
 * Created by kaliq on 11/26/2017.
 */

public class TermFactory extends ReadOnlyFactory<Term> {

    @Override
    public Term toDomain(JSONObject o)  throws JSONException {
        return Term.builder()
                .id(o.getInt("id"))
                .name(o.getString("name"))
                .code(o.getString("code"))
                .startDate(DateTime.parse(o.getString("start_date"), ISODateTimeFormat.yearMonthDay()).toLocalDate())
                .endDate(DateTime.parse(o.getString("end_date"), ISODateTimeFormat.yearMonthDay()).toLocalDate())
                .subjectsLoaded(o.getBoolean("subjects_loaded"))
                .build();
    }
}
