package sonhoang.vn.freemusic.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sonhoang.vn.freemusic.R;
import sonhoang.vn.freemusic.adapters.MusicTypeAdapter;
import sonhoang.vn.freemusic.databases.MusicTypeModel;
import sonhoang.vn.freemusic.networks.MusicInterface;
import sonhoang.vn.freemusic.networks.MusicTypesResponseJSON;
import sonhoang.vn.freemusic.networks.RetrofitInstance;

public class MusicTypeFragment extends Fragment {
    private List<MusicTypeModel> musicTypeModelList = new ArrayList<>();
    private Context context;
    private MusicTypeAdapter musicTypeAdapter;

    @BindView(R.id.rv_music_types)
    RecyclerView rvMusicTypes;


    public MusicTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_type, container, false);
        ButterKnife.bind(this, view);
        setupUI();
        loadData();

        return view;
    }

    private void setupUI() {
        context = getContext();
        musicTypeAdapter = new MusicTypeAdapter(musicTypeModelList, getContext());
        rvMusicTypes.setAdapter(musicTypeAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 3 == 0 ? 2 : 1);
            }
        });
        rvMusicTypes.setLayoutManager(gridLayoutManager);
    }

    private void loadData() {
        MusicInterface musicInterface =
                RetrofitInstance.getInstance().create(MusicInterface.class);
        musicInterface.getMusicTypes().enqueue(new Callback<MusicTypesResponseJSON>() {
            @Override
            public void onResponse(Call<MusicTypesResponseJSON> call, Response<MusicTypesResponseJSON> response) {
                List<MusicTypesResponseJSON.SubObjectJSON> list = response.body().subgenres;
                for (MusicTypesResponseJSON.SubObjectJSON subObjectJSON : list) {
                    MusicTypeModel musicTypeModel = new MusicTypeModel();
                    musicTypeModel.id = subObjectJSON.id;
                    musicTypeModel.key = subObjectJSON.translation_key;
                    musicTypeModel.imageID = context.getResources().getIdentifier(
                            "genre_x2_" + musicTypeModel.id,
                            "raw",
                            context.getPackageName()
                    );
                    musicTypeModelList.add(musicTypeModel);
                }
                musicTypeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MusicTypesResponseJSON> call, Throwable t) {
                Toast.makeText(context, "No connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
