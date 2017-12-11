package sonhoang.vn.freemusic.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import sonhoang.vn.freemusic.R;
import sonhoang.vn.freemusic.adapters.DownloadAdapter;
import sonhoang.vn.freemusic.databases.TopSongsModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {
    private static final String TAG = DownloadFragment.class.toString();
    private DownloadAdapter downloadAdapter;
    private List<TopSongsModel> topSongsModels = new ArrayList<>();

    @BindView(R.id.rv_downloaded_songs)
    RecyclerView rvDownloadSong;

    public DownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        setupUI(view);
        loadData();
        return view;
    }

    private void loadData() {
        File songFolder = new File(getContext().getExternalFilesDir("FreeMusic").toString());
        Log.d(TAG, "loadData: " + songFolder);
        File[] listFile = songFolder.listFiles();
        if (listFile != null){
            for (int i = 0; i < listFile.length; i++){
                String filePath = listFile[i].getName();
                Log.d(TAG, "loadData: " + filePath);
                TopSongsModel topSongsModel = new TopSongsModel();
                topSongsModel.songName = filePath.substring(9, filePath.indexOf("-"));
                topSongsModel.songArtist = filePath.substring(filePath.lastIndexOf("-") + 1, filePath.indexOf("."));
                topSongsModel.songUrl = listFile[i].getAbsolutePath();
                topSongsModel.songImage = "https://demo.tutorialzine.com/2015/03/html5-music-player/assets/img/default.png";
                topSongsModels.add(topSongsModel);
                Log.d(TAG, "loadData: " + listFile[i].getAbsolutePath());
                downloadAdapter.notifyItemChanged(i);
            }
        }
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);

        downloadAdapter = new DownloadAdapter(getContext(), topSongsModels );
        rvDownloadSong.setAdapter(downloadAdapter);
        rvDownloadSong.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDownloadSong.setItemAnimator(new SlideInLeftAnimator());
        rvDownloadSong.getItemAnimator().setAddDuration(300);
    }

}
