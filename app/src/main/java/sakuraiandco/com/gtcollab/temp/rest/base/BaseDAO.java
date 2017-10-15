package sakuraiandco.com.gtcollab.temp.rest.base;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sakuraiandco.com.gtcollab.temp.domain.Entity;
import sakuraiandco.com.gtcollab.temp.utils.VolleyResponseListener;

import static sakuraiandco.com.gtcollab.temp.utils.NetworkUtils.deleteRequest;
import static sakuraiandco.com.gtcollab.temp.utils.NetworkUtils.getRequest;
import static sakuraiandco.com.gtcollab.temp.utils.NetworkUtils.patchRequest;
import static sakuraiandco.com.gtcollab.temp.utils.NetworkUtils.postRequest;

/**
 * Created by kaliq on 10/15/2017.
 */

public abstract class BaseDAO<T extends Entity> implements VolleyResponseListener {

    /**************** DAO Listeners ***************************************************************/

    public static interface Listener<T extends Entity> {
        void onListReady(List<T> tList);
        void onObjectReady(T t);
    }

    public static interface ErrorListener {
        void onDAOError(Error error);
    }

    public static class Error {
        public VolleyError volleyError;
        public Exception exception;

        public Error(VolleyError volleyError, Exception exception) {
            this.volleyError = volleyError;
            this.exception = exception;
        }
    }

    /**************** Instance ********************************************************************/

    private DAOListener<T> callback;
    public final String baseURL;

    public BaseDAO(String baseURL, DAOListener<T> callback) {
        this.callback = callback;
        this.baseURL = baseURL;
    }

    /**************** CRUD - Single Object ********************************************************/

    public void create(T t) {
        postRequest(baseURL, toJSON(t), this);
    }

    public void update(int id, T t) {
        patchRequest(baseURL + id, toJSON(t), this);
    }

    public void delete(int id) {
        deleteRequest(baseURL + id, this);
    }

    public void get(int id) {
        getRequest(baseURL + id, this);
    }

    /**************** CRUD - List of Objects ******************************************************/

    public void getAll() {
        getRequest(baseURL, this);
    }

    public void getBySearch(String searchQuery) {
        getBySearchAndFilters(searchQuery, null);
    }

    public void getByFilters(Map<String, String> filters) {
        getBySearchAndFilters(null, filters);
    }

    public void getBySearchAndFilters(String searchQuery, Map<String, String> filters) {
        Map<String, String> queryParams = new HashMap<>();
        if (searchQuery != null) {
            queryParams.put("search", searchQuery);
        }
        if (filters != null) {
            queryParams.putAll(filters);
        }
        getRequest(baseURL, queryParams, this);
    }

    /**************** Factory Methods *************************************************************/

    public abstract JSONObject toJSON(T t);

    public abstract T toDomain(JSONObject o);

    /**************** VolleyResponseListener Implementation ***************************************/

    @Override
    public void onResponse(JSONObject response) { // TODO: deliver pagination and count info to callback?
        if (callback != null) {
            JSONArray resultsJSON = response.optJSONArray("results");
            if (resultsJSON != null) { // list of objects
                List<T> results = new ArrayList<>();
                try {
                    for (int i = 0; i < resultsJSON.length(); i++) {
                        results.add(toDomain(resultsJSON.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onDAOError(new Error(null, e));
                }
                callback.onListReady(results);
            } else { // single object
                callback.onObjectReady(toDomain(response));
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (callback != null) {
            callback.onDAOError(new Error(error, null));
        }
    }

}
