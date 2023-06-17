package com.bearya.robot.programme.station;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.programme.R;

import java.util.List;
import java.util.Locale;

public class LibFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lib, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        long libUuid = getArguments().getLong("lib");
        Lib lib = StationsActivity.stationLib.getByUUID(libUuid);
        int spanCount = lib.getCount() <= 4 ? 2 : 3;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        recyclerView.setAdapter(createAdapter(lib.items));
    }

    public static LibFragment newInstance(long libUuid) {
        Bundle args = new Bundle();
        args.putLong("lib", libUuid);
        LibFragment fragment = new LibFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        int index = (int) view.getTag();
        MusicUtil.playAssetsAudio(String.format(Locale.CHINA, "station/zh/station_action_%d.mp3", index));
        Intent intent = new Intent();
        requireActivity().setResult(Activity.RESULT_OK, intent);
        requireActivity().finish();
    }

    protected RecyclerView.Adapter<LibItemHolder> createAdapter(List<LibItem> data) {
        RecyclerViewAdapter<LibItemHolder, LibItem> adapter = new RecyclerViewAdapter<LibItemHolder, LibItem>(data) {
        };
        adapter.setOnItemClickedListener(new OnItemClickedListener<LibItem>() {
            @Override
            public void onItemClicked(LibItem data, int flag) {
                Intent intent = new Intent();
                intent.putExtra("data", data);
                requireActivity().setResult(Activity.RESULT_OK, intent);
                requireActivity().finish();
            }
        });
        return adapter;
    }

}