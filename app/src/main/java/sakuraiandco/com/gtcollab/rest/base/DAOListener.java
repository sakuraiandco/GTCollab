package sakuraiandco.com.gtcollab.rest.base;

import sakuraiandco.com.gtcollab.domain.Entity;

/**
 * Created by kaliq on 10/15/2017.
 */

public interface DAOListener<T extends Entity> extends BaseDAO.Listener<T>, BaseDAO.ErrorListener {
}
