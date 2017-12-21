package sonhoang.vn.freemusic.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import sonhoang.vn.freemusic.adapters.ViewPagerAdapter;
import sonhoang.vn.freemusic.R;
import sonhoang.vn.freemusic.databases.TopSongsModel;
import sonhoang.vn.freemusic.events.OnClickTopSongsEvent;
import sonhoang.vn.freemusic.fragments.MainPlayerFragment;
import sonhoang.vn.freemusic.utils.MusicHandler;
import sonhoang.vn.freemusic.utils.Utils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.toString();
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.rl_mini_player)
    RelativeLayout rlMiniPlayer;
    @BindView(R.id.sb_mini_player)
    SeekBar sbMiniPlayer;
    @BindView(R.id.tv_mini_player_name)
    TextView tvMiniPlayerName;
    @BindView(R.id.tv_mini_player_artist)
    TextView tvMiniPlayerArtist;
    @BindView(R.id.iv_mini_player_image)
    ImageView ivMiniPlayerImage;
    @BindView(R.id.fab_player_pause)
    FloatingActionButton fabPlayPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);
        setupUI();
    }

    @Subscribe(sticky = true)
    public void onReceivedTopSong(OnClickTopSongsEvent onClickTopSongsEvent){
        TopSongsModel topSongsModel = onClickTopSongsEvent.topSongsModel;

        rlMiniPlayer.setVisibility(View.VISIBLE);
        Picasso.with(this).load(topSongsModel.songImage).transform(new CropCircleTransformation()).into(ivMiniPlayerImage);
        tvMiniPlayerArtist.setText(topSongsModel.songArtist);
        tvMiniPlayerName.setText(topSongsModel.songName);

        MusicHandler.getSearchSong(topSongsModel, this);
        MusicHandler.updateUIRealTime(sbMiniPlayer, fabPlayPause, ivMiniPlayerImage, null, null);


        Log.d(TAG, "onReceivedTopSong: " + topSongsModel.songName);
    }

    private void setupUI() {
        ButterKnife.bind(this);
        setupTabLayout();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        sbMiniPlayer.setPadding(0, 0, 0, 0);
        fabPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicHandler.playPause();
            }
        });

        rlMiniPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openFragment(getSupportFragmentManager(),
                        R.id.rl_main,
                        new MainPlayerFragment());
            }
        });
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_dashboard_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_favorite_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_file_download_black_24dp));

        tabLayout.getTabAt(0).getIcon().setAlpha(255);
        tabLayout.getTabAt(1).getIcon().setAlpha(100);
        tabLayout.getTabAt(2).getIcon().setAlpha(100);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setAlpha(255);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setAlpha(100);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            super.onBackPressed();
        } else {
            moveTaskToBack(true);
        }
    }
}
