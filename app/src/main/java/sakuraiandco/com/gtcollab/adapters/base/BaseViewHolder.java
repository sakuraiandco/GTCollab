package sakuraiandco.com.gtcollab.adapters.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import sakuraiandco.com.gtcollab.domain.Entity;

/**
 * Created by kaliq on 11/25/2017.
 */

public abstract class BaseViewHolder<T extends Entity> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View view) {
        super(view);
    }

    public abstract void bind(T t);

}
