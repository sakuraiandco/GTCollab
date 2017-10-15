package sakuraiandco.com.gtcollab.temp.rest.base;

import org.json.JSONObject;

import sakuraiandco.com.gtcollab.temp.domain.Entity;

/**
 * Created by kaliq on 10/15/2017.
 */

public abstract class ReadOnlyDAO<T extends Entity> extends BaseDAO<T> {

    public ReadOnlyDAO(String baseURL, DAOListener<T> callback) {
        super(baseURL, callback);
    }

    @Override
    public void create(T t) {}

    @Override
    public void update(int id, T t) {}

    @Override
    public void delete(int id) {}

    @Override
    public JSONObject toJSON(T t) { return null; } // don't need this, since we aren't POST/PATCH/DELETE-ing objects

}
