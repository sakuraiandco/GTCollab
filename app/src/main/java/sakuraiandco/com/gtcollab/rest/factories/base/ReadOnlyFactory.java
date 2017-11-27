package sakuraiandco.com.gtcollab.rest.factories.base;

import org.json.JSONObject;

import sakuraiandco.com.gtcollab.domain.Entity;

/**
 * Created by kaliq on 11/26/2017.
 */

public abstract class ReadOnlyFactory<T extends Entity> extends BaseFactory<T> {

    @Override
    public JSONObject toJSON(T t) { return null; } // don't need this, since we aren't POST/PATCH/DELETE-ing objects

}
