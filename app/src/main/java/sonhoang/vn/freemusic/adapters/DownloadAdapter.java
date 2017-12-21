package sonhoang.vn.freemusic.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sonhoang.vn.freemusic.R;
import sonhoang.vn.freemusic.databases.TopSongsModel;
import sonhoang.vn.freemusic.events.OnClickTopSongsEvent;

/**
 * Created by Son Hoang on 12/11/2017.
 */

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {
    private Context context;
    private List<TopSongsModel> topSongsModels;


    public DownloadAdapter(Context context, List<TopSongsModel> topSongsModels) {
        this.context = context;
        this.topSongsModels = topSongsModels;
    }

    @Override
    public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.item_list_downloaded_song, null);
        return new DownloadViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DownloadViewHolder holder, int position) {
        holder.setData(topSongsModels.get(position));
    }

    @Override
    public int getItemCount() {
        return topSongsModels.size();
    }

    public class DownloadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_downloaded_song_name)
        TextView tvDownloadSongName;
        @BindView(R.id.tv_downloaded_song_artist)
        TextView tvDownloadSongArtist;
        @BindView(R.id.iv_downloaded_song_image)
        ImageView ivDownloadSongImage;
        private View view;

        public DownloadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
        }

        public void setData(final TopSongsModel topSongsModel) {
            tvDownloadSongArtist.setText(topSongsModel.songName);
            tvDownloadSongName.setText(topSongsModel.songArtist);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().postSticky(new OnClickTopSongsEvent(topSongsModel, topSongsModels));
                }
            });
        }
    }
}
