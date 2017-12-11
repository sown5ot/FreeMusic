package sonhoang.vn.freemusic.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import sonhoang.vn.freemusic.R;
import sonhoang.vn.freemusic.databases.TopSongsModel;
import sonhoang.vn.freemusic.events.OnClickTopSongsEvent;
import sonhoang.vn.freemusic.utils.MusicHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainPlayerFragment extends Fragment {
    private static final String TAG = MainPlayerFragment.class.toString();
    private TopSongsModel topSongsModel;

    @BindView(R.id.tb_playing_song)
    android.support.v7.widget.Toolbar tbPlayingSong;
    @BindView(R.id.iv_playing_song_image)
    ImageView ivPlayingSongImage;
    @BindView(R.id.iv_playing_song_download)
    ImageView ivPlayingSongDownload;
    @BindView(R.id.tv_playing_song_name)
    TextView tvPlayingSongName;
    @BindView(R.id.tv_playing_song_artist)
    TextView tvPlayingSongArtist;
    @BindView(R.id.iv_playing_song_next)
    ImageView ivPlayingSongNext;
    @BindView(R.id.iv_playing_song_previous)
    ImageView ivPlayingSongPrevious;
    @BindView(R.id.fab_playing_song_playpause)
    FloatingActionButton fabPlayingSongPlayPause;
    @BindView(R.id.tv_playing_song_current_time)
    TextView tvPlayingSongCurrentTime;
    @BindView(R.id.tv_playing_song_duration)
    TextView tvPlayingSongDuration;
    @BindView(R.id.sb_playing_song)
    SeekBar sbPlayingSong;


    public MainPlayerFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song_playing, container, false);
        setupUI(view);
        EventBus.getDefault().register(this);
        addListeners();
        return view;
    }

    private void addListeners() {
        ivPlayingSongDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDownloaded(topSongsModel)) {
                    downloadSong();
                } else {
                    Toast.makeText(getContext(), "Already downloaded", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: Already downloaded");
                }
            }
        });
    }

    private void downloadSong() {
        Uri downloadUri = Uri.parse(topSongsModel.songUrl);
        Uri destinationUri = Uri.parse(getContext().getExternalFilesDir("FreeMusic").toString() + "/FreeMusic" + topSongsModel.songName + "-" + topSongsModel.songArtist + ".mp3");
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri)
                .setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        Toast.makeText(getContext(), "Download completed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onDownloadComplete: ");
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                        Toast.makeText(getContext(), "Cannot download", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onDownloadFailed: " + errorMessage);
                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downloadedBytes, int progress) {
                        Toast.makeText(getContext(), "Downloading " + progress + "%", Toast.LENGTH_SHORT).show();
                    }
                });

        ThinDownloadManager thinDownloadManager = new ThinDownloadManager();
        thinDownloadManager.add(downloadRequest);
    }


    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        tbPlayingSong.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        tbPlayingSong.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        fabPlayingSongPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicHandler.playPause();
            }
        });
    }

    @Subscribe(sticky = true)
    public void onMiniPlayerClicked(OnClickTopSongsEvent onClickTopSongsEvent){
        topSongsModel = onClickTopSongsEvent.topSongsModel;

        tvPlayingSongName.setText(topSongsModel.songName);
        tvPlayingSongArtist.setText(topSongsModel.songArtist);
        if (topSongsModel.songLargeImage != null) {
            Picasso.with(getContext())
                    .load(topSongsModel.songLargeImage)
                    .transform(new CropCircleTransformation())
                    .into(ivPlayingSongImage);
        }

        MusicHandler.updateUIRealTime(sbPlayingSong, fabPlayingSongPlayPause, ivPlayingSongImage, tvPlayingSongCurrentTime, tvPlayingSongDuration);
    }

    private boolean isDownloaded(TopSongsModel topSongsModel){
        List<String> downloadedSongList = new ArrayList<>();

        File songFolder = new File(getContext().getExternalFilesDir("FreeMusic").toString());
        File[] listFile = songFolder.listFiles();
        if (listFile != null){
            for (int i = 0; i < listFile.length; i++){
                String filePath = listFile[i].getName();
                Log.d(TAG, "isDownloaded: " + filePath);
                downloadedSongList.add(filePath);
            }
        }

        for (int i = 0; i < downloadedSongList.size(); i++){
            if (downloadedSongList.get(i).equals("FreeMusic" + topSongsModel.songName + "-" + topSongsModel.songArtist + ".mp3"))
                return true;
        }

        return false;
    }

}
