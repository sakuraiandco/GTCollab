package sakuraiandco.com.gtcollab.adapters;

import sakuraiandco.com.gtcollab.domain.Entity;

/**
 * Created by kaliq on 10/19/2017.
 */

public interface AdapterListener<T extends Entity> {

    void onClick(T t);

}
