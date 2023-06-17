package com.bearya.robot.programme.station;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bearya.robot.programme.R;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class RecyclerViewAdapter<T extends RecyclerViewHolder<D>, D> extends RecyclerView.Adapter<T> {
    private List<D> datas;
    private OnItemClickedListener<D> onItemClickedListener;

    public RecyclerViewAdapter(List<D> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            return getViewClass().getConstructor(ViewGroup.class).newInstance(parent);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(T holder, final int position) {
        if (onItemClickedListener != null) {
            if (holder.needOnItemClickedListener()) {
                holder.setOnItemClickedListener(onItemClickedListener);
            }
            holder.itemView.setTag(datas.get(position));
            holder.itemView.setTag(R.id.item_click_flag, position);
            holder.itemView.setOnClickListener(onItemClickedListener);
        }
        holder.setData(datas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public Class<T> getViewClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void setOnItemClickedListener(OnItemClickedListener<D> onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}
