package sakuraiandco.com.gtcollab.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import sakuraiandco.com.gtcollab.domain.Entity;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

/**
 * Created by kaliq on 10/19/2017.
 */

public class PaginationScrollListener<T extends Entity> extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;
    BaseDAO<T> baseDAO;

    public PaginationScrollListener(LinearLayoutManager layoutManager, BaseDAO<T> baseDAO) {
        this.layoutManager = layoutManager;
        this.baseDAO = baseDAO;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        boolean reachedBottomOfList = layoutManager.findFirstVisibleItemPosition() + layoutManager.getChildCount() >= layoutManager.getItemCount();
        if (reachedBottomOfList && !baseDAO.isLoading() && (baseDAO.getNextPageURL() != null) && baseDAO.hasNext()) {
            baseDAO.getNextPage();
        }
    }
}
