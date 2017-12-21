package sonhoang.vn.freemusic.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import sonhoang.vn.freemusic.R;
import sonhoang.vn.freemusic.adapters.MusicTypeAdapter;
import sonhoang.vn.freemusic.events.OnUpdateFavorite;
import sonhoang.vn.freemusic.utils.DatabaseHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    @BindView(R.id.rv_fav)
    RecyclerView rvFav;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);
        rvFav.setAdapter(new MusicTypeAdapter(
                DatabaseHandler.getFavoriteMusicType(),
                getContext()
        ));
        rvFav.setLayoutManager(new GridLayoutManager(getContext(), 2));
        EventBus.getDefault().register(this);
        return view;
    }

    @Subscribe(sticky = true)
    public void update(OnUpdateFavorite onUpdateFavorite){
        rvFav.setAdapter(new MusicTypeAdapter(
                DatabaseHandler.getFavoriteMusicType(),
                getContext()
        ));
    }

}
