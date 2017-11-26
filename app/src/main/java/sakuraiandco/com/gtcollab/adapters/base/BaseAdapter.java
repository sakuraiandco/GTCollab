package sakuraiandco.com.gtcollab.adapters.base;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import lombok.Getter;
import sakuraiandco.com.gtcollab.adapters.AdapterListener;
import sakuraiandco.com.gtcollab.domain.Entity;

/**
 * Created by kaliq on 11/13/2017.
 */

public abstract class BaseAdapter<T extends Entity, V extends BaseViewHolder<T>> extends RecyclerView.Adapter<V> {

    @Getter public List<T> data;
    public AdapterListener<T> callback;

    public BaseAdapter(List<T> data, AdapterListener<T> callback) {
        this.data = data;
        this.callback = callback;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        holder.bind(data.get(position));
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

}