package sakuraiandco.com.gtcollab.rest;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.domain.Term;
import sakuraiandco.com.gtcollab.rest.base.ReadOnlyDAO;

import static sakuraiandco.com.gtcollab.utils.NetworkUtils.getRequest;

/**
 * Created by kaliq on 10/15/2017.
 */

public class TermDAO extends ReadOnlyDAO<Term> {

    public TermDAO(Listener<Term> callback) {
        super(Term.BASE_URL, callback);
    }

    public void getCurrent() {
        getRequest(getBaseURL() + "current", this);
    }

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
