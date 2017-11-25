package sakuraiandco.com.gtcollab.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import lombok.Getter;
import sakuraiandco.com.gtcollab.domain.Entity;

/**
 * Created by kaliq on 11/13/2017.
 */

abstract class BaseAdapter<T extends Entity, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    @Getter List<T> data;
    AdapterListener<T> callback;

    BaseAdapter(List<T> data, AdapterListener<T> callback) {
        this.data = data;
        this.callback = callback;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        int startPosition = this.data.size() - 1;
        this.data.addAll(data);
        notifyItemRangeInserted(startPosition, data.size());
    }

    public void addItem(T item) {
        this.data.add(item);
        notifyItemInserted(this.data.size() - 1);
    }

}