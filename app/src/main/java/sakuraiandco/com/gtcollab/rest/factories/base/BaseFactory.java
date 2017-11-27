package sakuraiandco.com.gtcollab.rest.factories.base;

import org.json.JSONException;
import org.json.JSONObject;

import sakuraiandco.com.gtcollab.domain.Entity;

/**
 * Created by kaliq on 11/26/2017.
 */

public abstract class BaseFactory<T extends Entity> {

    public abstract JSONObject toJSON(T t) throws JSONException;

    public abstract T toDomain(JSONObject o) throws JSONException;

}
