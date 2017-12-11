package sonhoang.vn.freemusic.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sonhoang.vn.freemusic.R;
import sonhoang.vn.freemusic.events.OnClickMusicTypeEvent;
import sonhoang.vn.freemusic.fragments.TopSongFragment;
import sonhoang.vn.freemusic.activities.MainActivity;
import sonhoang.vn.freemusic.databases.MusicTypeModel;
import sonhoang.vn.freemusic.utils.Utils;

/**
 * Created by Son Hoang on 11/18/2017.
 */

public class MusicTypeAdapter extends RecyclerView.Adapter<MusicTypeAdapter.MusicTypeViewHolder> {
    List<MusicTypeModel> musicTypeModelList;
    Context context;

    public MusicTypeAdapter(List<MusicTypeModel> musicTypeModelList, Context context) {
        this.musicTypeModelList = musicTypeModelList;
        this.context = context;
    }

    @Override
    public MusicTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_music_type, parent, false);

        return new MusicTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicTypeViewHolder holder, int position) {
        holder.setData(musicTypeModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return musicTypeModelList.size();
    }

    public class MusicTypeViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_music_type)
        ImageView ivMusicType;
        @BindView(R.id.tv_music_type_name)
        TextView tvMusicTypeName;

        View view;

        public MusicTypeViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void setData(final MusicTypeModel musicTypeModel){
            Picasso.with(context).load(musicTypeModel.imageID).into(ivMusicType);
            tvMusicTypeName.setText(musicTypeModel.key);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.openFragment(
                            ((MainActivity) context).getSupportFragmentManager(),
                            R.id.rl_main,
                            new TopSongFragment()
                    );

                    EventBus.getDefault().postSticky(new OnClickMusicTypeEvent(musicTypeModel));
                }
            });
        }
    }


}
