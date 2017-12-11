package sonhoang.vn.freemusic.utils;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import hybridmediaplayer.HybridMediaPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sonhoang.vn.freemusic.R;
import sonhoang.vn.freemusic.databases.TopSongsModel;
import sonhoang.vn.freemusic.networks.MusicInterface;
import sonhoang.vn.freemusic.networks.RetrofitInstance;
import sonhoang.vn.freemusic.networks.SearchSongResponseJSON;
import sonhoang.vn.freemusic.notifications.MusicNotification;

/**
 * Created by Son Hoang on 11/29/2017.
 */

public class MusicHandler {
    public static HybridMediaPlayer hybridMediaPlayer;
    static boolean keepUpdating = true;

    public static void getSearchSong(final TopSongsModel topSongsModel, final Context context) {
        MusicInterface musicInterface = RetrofitInstance.getInstance().create(MusicInterface.class);
        musicInterface.getSearchSong(topSongsModel.songName
                + " "
                + topSongsModel.songArtist).enqueue(new Callback<SearchSongResponseJSON>() {
            @Override
            public void onResponse(Call<SearchSongResponseJSON> call, Response<SearchSongResponseJSON> response) {
                if (response.isSuccessful()) {
                    topSongsModel.songUrl = response.body().data.url;
                    topSongsModel.songLargeImage = response.body().data.thumbnail;

                    playMusic(context, topSongsModel);
                    MusicNotification.setupNotification(context, topSongsModel);
                } else if (!response.isSuccessful()){
                    Toast.makeText(context, "Not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchSongResponseJSON> call, Throwable t) {
                Toast.makeText(context,"No connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void playMusic(Context context, TopSongsModel topSongsModel) {
        if (hybridMediaPlayer != null){
            hybridMediaPlayer.pause();
            hybridMediaPlayer.release();
        }

        hybridMediaPlayer = HybridMediaPlayer.getInstance(context);
        hybridMediaPlayer.setDataSource(topSongsModel.songUrl);
        hybridMediaPlayer.prepare();
        hybridMediaPlayer.setOnPreparedListener(new HybridMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(HybridMediaPlayer hybridMediaPlayer) {
                hybridMediaPlayer.play();
            }
        });
    }

    public static void playPause(){
        if (hybridMediaPlayer.isPlaying()){
            hybridMediaPlayer.pause();
        } else {
            hybridMediaPlayer.play();
        }
        MusicNotification.updateNotification();
    }

    public static void updateUIRealTime(final SeekBar seekBar,
                                        final FloatingActionButton floatingActionButton,
                                        final ImageView imageView,
                                        final TextView tvCurrentTime,
                                        final TextView tvDuration){
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //update UI
                if (keepUpdating && hybridMediaPlayer != null){
                    //update seekbar real time
                    seekBar.setMax(hybridMediaPlayer.getDuration());
                    seekBar.setProgress(hybridMediaPlayer.getCurrentPosition());

                    //update play pause button
                    if (hybridMediaPlayer.isPlaying()){
                        floatingActionButton.setImageResource(R.drawable.ic_pause_black_24dp);
                    } else {
                        floatingActionButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    }
                    if (imageView != null) Utils.rotateImage(imageView, hybridMediaPlayer.isPlaying());

                    if (tvCurrentTime != null){
                        tvCurrentTime.setText(Utils.convertTime(hybridMediaPlayer.getCurrentPosition()));
                        tvDuration.setText(Utils.convertTime(hybridMediaPlayer.getDuration()));
                    }
                }
                //run every 100ms
                handler.postDelayed(this, 100);
            }
        };
        runnable.run();

        //set seek bar on progress
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                keepUpdating = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                hybridMediaPlayer.seekTo(seekBar.getProgress());
                keepUpdating = true;
            }
        });
    }
}
