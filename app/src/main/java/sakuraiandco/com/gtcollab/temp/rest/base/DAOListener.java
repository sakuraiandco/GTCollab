package sakuraiandco.com.gtcollab.temp.rest.base;

import sakuraiandco.com.gtcollab.temp.domain.Entity;

/**
 * Created by kaliq on 10/15/2017.
 */

public interface DAOListener<T extends Entity> extends BaseDAO.Listener<T>, BaseDAO.ErrorListener {
}
