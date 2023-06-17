package com.bearya.robot.programme.station;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bearya.robot.base.BaseApplication;


public abstract class RecyclerViewHolder<D> extends RecyclerView.ViewHolder{
    protected   OnItemClickedListener<D> onItemClickedListener;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
    }
    public RecyclerViewHolder(ViewGroup parent, int layout) {
        this(LayoutInflater.from(BaseApplication.getInstance()).inflate(layout,parent,false));
    }

    public void setData(D d,int position){
        setData(d);
    }
    public abstract void setData(D d);

    public void setOnItemClickedListener(OnItemClickedListener<D> onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    protected boolean needOnItemClickedListener(){
        return false;
    }

}
