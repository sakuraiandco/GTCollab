package sakuraiandco.com.gtcollab.temp.rest;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.temp.domain.Term;
import sakuraiandco.com.gtcollab.temp.rest.base.DAOListener;
import sakuraiandco.com.gtcollab.temp.rest.base.ReadOnlyDAO;

import static sakuraiandco.com.gtcollab.temp.utils.NetworkUtils.getRequest;

/**
 * Created by kaliq on 10/15/2017.
 */

public class TermDAO extends ReadOnlyDAO<Term> {

    public TermDAO(DAOListener<Term> callback) {
        super(Term.BASE_URL, callback);
    }

    public void getCurrent() {
        getRequest(baseURL + "current", this);
    }

    @Override
    public Term toDomain(JSONObject o) {
        try {
            return Term.builder()
                    .id(o.getInt("id"))
                    .name(o.getString("name"))
                    .code(o.getString("code"))
                    .startDate(DateTime.parse(o.getString("startDate"), ISODateTimeFormat.yearMonthDay()).toLocalDate())
                    .endDate(DateTime.parse(o.getString("startTime"), ISODateTimeFormat.yearMonthDay()).toLocalDate())
                    .subjectsLoaded(o.getBoolean("subjects_loaded"))
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // TODO
    }
}
