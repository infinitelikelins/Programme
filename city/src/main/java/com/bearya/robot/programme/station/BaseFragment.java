package com.bearya.robot.programme.station;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bearya.robot.base.play.PlayData;

public abstract class BaseFragment extends Fragment {
    private View rootView;

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(getLayoutId(), null);
            initView(rootView);
            loadLastStationConfig();
        }
        return rootView;
    }

    protected PlayData getStation() {
        return getActivity() instanceof StationConfigActivity ?
                ((StationConfigActivity) getActivity()).getStation() : null;
    }

    protected abstract void loadLastStationConfig();

    protected void saveStation() {
        if (getActivity() instanceof StationConfigActivity)
            ((StationConfigActivity) getActivity()).saveLastStationConfig();
    }

    public void onUnselected() {

    }
}