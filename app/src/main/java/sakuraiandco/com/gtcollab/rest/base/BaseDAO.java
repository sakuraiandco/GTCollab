package sakuraiandco.com.gtcollab.rest.base;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import sakuraiandco.com.gtcollab.domain.Entity;
import sakuraiandco.com.gtcollab.utils.NetworkUtils;

import static sakuraiandco.com.gtcollab.utils.NetworkUtils.deleteRequest;
import static sakuraiandco.com.gtcollab.utils.NetworkUtils.getRequest;
import static sakuraiandco.com.gtcollab.utils.NetworkUtils.patchRequest;
import static sakuraiandco.com.gtcollab.utils.NetworkUtils.postRequest;

/**
 * Created by kaliq on 10/15/2017.
 */

public abstract class BaseDAO<T extends Entity> implements NetworkUtils.VolleyResponseListener {

    /**************** DAO Listeners ***************************************************************/

    public interface Listener<T extends Entity> {
        void onDAOError(Error error);
        void onListReady(List<T> tList);
        void onObjectReady(T t);
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

    @Getter String baseURL;
    private Listener<T> callback;
    private Integer count;
    @Getter private String nextPageURL;
    @Getter private String prevPageURL;

    // status
    private int numRequestsWaitingForResponse;

    public BaseDAO(String baseURL, Listener<T> callback) {
        this.baseURL = baseURL;
        this.callback = callback;
        this.count = null;
        this.nextPageURL = null;
        this.prevPageURL = null;
        this.numRequestsWaitingForResponse = 0;
    }

    /**************** CRUD - Single Object ********************************************************/

    public void create(T t) {
        try {
            postRequest(baseURL, toJSON(t), this);
            numRequestsWaitingForResponse++;
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onDAOError(new Error(null, e));
        }
    }

    public void update(int id, T t) {
        try {
            patchRequest(baseURL + id, toJSON(t), this);
            numRequestsWaitingForResponse++;
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onDAOError(new Error(null, e));
        }
    }

    public void delete(int id) {
        deleteRequest(baseURL + id, this);
        numRequestsWaitingForResponse++;
    }

    public void get(int id) {
        getRequest(baseURL + id, this);
        numRequestsWaitingForResponse++;
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
        numRequestsWaitingForResponse++;
    }

    /**************** Status **********************************************************************/

    public boolean isLoading() {
        return numRequestsWaitingForResponse > 0;
    }

    /**************** Pagination ******************************************************************/

    public Integer getCount() {
        if (count == null) {
            throw new IllegalStateException("No request context");
        }
        return count;
    }

    public boolean hasNext() {
        if (nextPageURL == null) {
            throw new IllegalStateException("No request context");
        }
        return !nextPageURL.toString().equals("null");
    }

    public boolean hasPrev() {
        if (prevPageURL == null) {
            throw new IllegalStateException("No request context");
        }
        return !prevPageURL.toString().equals("null");
    }

    public void getNextPage() {
        if (nextPageURL == null) {
            throw new IllegalStateException("No request context");
        }
        if (!hasNext()) {
            throw new IllegalStateException("Page out of bounds");
        }
        getRequest(nextPageURL, this);
        numRequestsWaitingForResponse++;
    }

    public void getPrevPage() {
        if (prevPageURL == null) {
            throw new IllegalStateException("No request context");
        }
        if (!hasPrev()) {
            throw new IllegalStateException("Page out of bounds");
        }
        getRequest(prevPageURL, this);
        numRequestsWaitingForResponse++;
    }

    /**************** Factory Methods *************************************************************/

    public abstract JSONObject toJSON(T t) throws JSONException;

    public abstract T toDomain(JSONObject o) throws JSONException;

    /**************** VolleyResponseListener Implementation ***************************************/

    @Override
    public void onResponse(JSONObject response) { // TODO: deliver pagination and count info to callback?
        numRequestsWaitingForResponse--;
        if (callback != null) {
            JSONArray resultsJSON = response.optJSONArray("results");
            if (resultsJSON != null) { // list of objects
                List<T> results = new ArrayList<>();
                try {
                    // handle pagination
                    count = response.getInt("count");
                    nextPageURL = response.getString("next");
                    prevPageURL = response.getString("previous");
                    // process results
                    for (int i = 0; i < resultsJSON.length(); i++) {
                        results.add(toDomain(resultsJSON.getJSONObject(i)));
                    }
                    callback.onListReady(results);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onDAOError(new Error(null, e));
                }
            } else { // single object
                try {
                    callback.onObjectReady(toDomain(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onDAOError(new Error(null, e));
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        numRequestsWaitingForResponse--;
        if (callback != null) {
            callback.onDAOError(new Error(error, null));
        }
    }

}
