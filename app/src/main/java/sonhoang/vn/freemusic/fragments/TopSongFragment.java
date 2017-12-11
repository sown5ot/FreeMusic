package sonhoang.vn.freemusic.fragments;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sonhoang.vn.freemusic.R;
import sonhoang.vn.freemusic.adapters.TopSongsAdapter;
import sonhoang.vn.freemusic.databases.MusicTypeModel;
import sonhoang.vn.freemusic.databases.TopSongsModel;
import sonhoang.vn.freemusic.events.OnClickMusicTypeEvent;
import sonhoang.vn.freemusic.networks.MusicInterface;
import sonhoang.vn.freemusic.networks.RetrofitInstance;
import sonhoang.vn.freemusic.networks.TopSongsResponseJSON;


/**
 * A simple {@link Fragment} subclass.
 */
public class TopSongFragment extends Fragment {
    private static final String TAG = TopSongFragment.class.toString();
    private TopSongsAdapter topSongsAdapter;
    private List<TopSongsModel> topSongsModels = new ArrayList<>();
    public MusicTypeModel musicTypeModel;

    TextView tvMusicType;
    @BindView(R.id.iv_frag_favorite)
    ImageView ivFragFavorite;
    @BindView(R.id.iv_frag_music_type)
    ImageView ivFragMusicType;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.rv_frag_content)
    RecyclerView rvMusicContent;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.av_loading)
    AVLoadingIndicatorView avLoadingIndicatorView;



    public TopSongFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_song, container, false);
        EventBus.getDefault().register(this);
        setupUI(view);
        loadData();
        return view;
    }

    private void loadData() {
        MusicInterface musicInterface = RetrofitInstance.getInstance().create(MusicInterface.class);
        musicInterface.getTopSongs(musicTypeModel.id).enqueue(new Callback<TopSongsResponseJSON>() {
            @Override
            public void onResponse(Call<TopSongsResponseJSON> call, Response<TopSongsResponseJSON> response) {
                List<TopSongsResponseJSON.FeedJSON.EntryJSON> entryJSONList = response.body().feed.entry;
                for (int i = 0; i < entryJSONList.size(); i++){
                    TopSongsModel topSongsModel = new TopSongsModel();
                    topSongsModel.songArtist = entryJSONList.get(i).artist.label;
                    topSongsModel.songName = entryJSONList.get(i).name.label;
                    topSongsModel.songImage = entryJSONList.get(i).image.get(2).label;

                    topSongsModels.add(topSongsModel);
                    topSongsAdapter.notifyItemChanged(i);
                    avLoadingIndicatorView.hide();
                }
//                topSongsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TopSongsResponseJSON> call, Throwable t) {
                Toast.makeText(getContext(), "No connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe(sticky = true)
    public void onReceivedOnMusicTypeClick(OnClickMusicTypeEvent onClickMusicTypeEvent){
        musicTypeModel = onClickMusicTypeEvent.musicTypeModel;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        tvMusicType = view.findViewById(R.id.tv_frag_music_type_name);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        Picasso.with(getContext()).load(musicTypeModel.imageID).into(ivFragMusicType);
        tvMusicType.setText(musicTypeModel.key);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(TAG, "onOffsetChanged: " + verticalOffset);
                if (verticalOffset != 0){
                    toolbar.setBackground(getResources().getDrawable(R.drawable.custom_gradient_text_bot_to_top));
                } else {
                    toolbar.setBackground(null);
                }
            }
        });

        topSongsAdapter = new TopSongsAdapter(getContext(), topSongsModels);
        rvMusicContent.setAdapter(topSongsAdapter);
        rvMusicContent.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMusicContent.setItemAnimator(new SlideInLeftAnimator());
        rvMusicContent.getItemAnimator().setAddDuration(300);
        avLoadingIndicatorView.show();
    }

}
