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
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import sonhoang.vn.freemusic.R;
import sonhoang.vn.freemusic.databases.TopSongsModel;
import sonhoang.vn.freemusic.events.OnClickTopSongsEvent;
import sonhoang.vn.freemusic.notifications.MusicNotification;

/**
 * Created by Son Hoang on 11/25/2017.
 */

public class TopSongsAdapter extends RecyclerView.Adapter<TopSongsAdapter.TopSongsViewHolder> {
    private Context context;
    List<TopSongsModel> topSongsModels;

    public TopSongsAdapter(Context context, List<TopSongsModel> topSongsModels) {
        this.context = context;
        this.topSongsModels = topSongsModels;
    }

    @Override
    public TopSongsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_list_top_song, null);
        return new TopSongsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopSongsViewHolder holder, int position) {
        holder.setData(topSongsModels.get(position));
    }

    @Override
    public int getItemCount() {
        return topSongsModels.size();
    }

    public class TopSongsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_song_image)
        ImageView ivSongImage;
        @BindView(R.id.tv_song_name)
        TextView tvSongName;
        @BindView(R.id.tv_song_artist)
        TextView tvSongArtist;
        View view;

        public TopSongsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void setData(final TopSongsModel topSongsModel){
            Picasso.with(context).load(topSongsModel.songImage).transform(new CropCircleTransformation()).into(ivSongImage);
            tvSongName.setText(topSongsModel.songName);
            tvSongArtist.setText(topSongsModel.songArtist);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().postSticky(new OnClickTopSongsEvent(topSongsModel));

                }
            });
        }
    }
}
