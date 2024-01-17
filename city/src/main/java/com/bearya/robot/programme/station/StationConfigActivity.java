package com.bearya.robot.programme.station;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bearya.actionlib.utils.SharedPreferencesUtil;
import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.programme.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.File;
import java.util.Locale;

public class StationConfigActivity extends BaseActivity implements View.OnClickListener {

    private static final String STATION_KEY = "station_loc_";
    private TabLayout tabLayout;
    private Tab[] tabs;

    private int currentSelectIndex = 0;
    private final BaseFragment[] fragments = new BaseFragment[3];
    private PlayData station;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        index = getIntent().getIntExtra("index", 1);
        setContentView(R.layout.activity_station_config);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        fragments[0] = StationImageFragment.newInstance();
        fragments[1] = StationActionFragment.newInstance();
        fragments[2] = StationSoundFragment.newInstance();

        ViewPager viewPager = findViewById(R.id.viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BaseFragment preFragment = fragments[currentSelectIndex];
                currentSelectIndex = position;
                BaseFragment currentFragment = fragments[currentSelectIndex];
                if (preFragment != null && preFragment != currentFragment) {
                    preFragment.onUnselected();
                }
                MusicUtil.playAssetsAudio(String.format(Locale.CHINA, "station/zh/station_play_%d.mp3", position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabs = new Tab[]{new Tab(getString(R.string.image), R.drawable.station_tab_image_selector),
                new Tab(getString(R.string.action), R.drawable.station_tab_action_selector),
                new Tab(getString(R.string.voice), R.drawable.station_tab_sound_selector)};
        setupTabIcons();

        station = getLastConfigStation(getApplicationContext(), index);

        MusicUtil.playAssetsAudios(String.format(Locale.CHINA, "station/zh/station%d.mp3", index),
                "station/zh/station_play_1.mp3");

    }

    private void setupTabIcons() {
        for (int i = 0; i < tabs.length; i++) {
            tabLayout.getTabAt(i).setCustomView(inflateTabView(i));
        }
    }

    public View inflateTabView(int position) {
        Tab tab = tabs[position];
        View view = getLayoutInflater().inflate(R.layout.item_tab, null);
        ((TextView) view.findViewById(R.id.txt_title)).setText(tab.name);
        ((ImageView) view.findViewById(R.id.img_title)).setImageResource(tab.icon);
        return view;
    }

    public static PlayData getLastConfigStation(Context context, int index) {
        String lastConfig = SharedPreferencesUtil.getInstance(context).getString(getStationKey(index));
        if (TextUtils.isEmpty(lastConfig)) {
            return new PlayData();
        }

        lastConfig = BaseApplication.isEnglish ?
                lastConfig.replace("/zh/", "/en/") :
                lastConfig.replace("/en/", "/zh/");

        PlayData station = new Gson().fromJson(lastConfig, PlayData.class);
        if (station == null) {
            return new PlayData();
        }
        return station;
    }

    public void saveLastStationConfig() {
        String lastConfig = new Gson().toJson(station);
        SharedPreferencesUtil.getInstance(getApplicationContext()).put(getStationKey(index), lastConfig);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivBack) {
            saveLastStationConfig();
            finish();
        }
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (BaseFragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public PlayData getStation() {
        return station;
    }

    public static String getStationKey(int index) {
        return STATION_KEY + index;
    }

    public static void clearStationCache(Context context) {
        for (int i = 1; i <= 6; i++) {
            SharedPreferencesUtil.getInstance(context.getApplicationContext()).remove(getStationKey(i));
        }
        File file = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        if (file != null && file.exists())
            file.delete();
    }

    private static class Tab {
        String name;
        int icon;

        Tab(String name, int icon) {
            this.name = name;
            this.icon = icon;
        }
    }

}
