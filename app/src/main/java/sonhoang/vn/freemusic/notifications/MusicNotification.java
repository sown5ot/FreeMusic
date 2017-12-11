package sonhoang.vn.freemusic.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import sonhoang.vn.freemusic.R;
import sonhoang.vn.freemusic.activities.MainActivity;
import sonhoang.vn.freemusic.databases.TopSongsModel;
import sonhoang.vn.freemusic.utils.MusicHandler;

/**
 * Created by Son Hoang on 12/9/2017.
 */

public class MusicNotification {
    private static final int NOTIFICATION_ID = 1;
    private static RemoteViews remoteViews;
    public static NotificationCompat.Builder builder;
    public static NotificationManager notificationManager;

    public static void setupNotification(Context context, TopSongsModel topSongsModel) {
        remoteViews = new RemoteViews(
                context.getPackageName(),
                R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.tv_noti_name, topSongsModel.songName);
        remoteViews.setTextViewText(R.id.tv_noti_artist, topSongsModel.songArtist);
        remoteViews.setImageViewResource(R.id.iv_noti_player_pause, R.drawable.ic_pause_really_black_24dp);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContent(remoteViews)
                .setContentIntent(pendingIntent)
                .setOngoing(true);

        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Picasso.with(context)
                .load(topSongsModel.songImage)
                .transform(new CropCircleTransformation())
                .into(remoteViews, R.id.iv_noti_image, NOTIFICATION_ID, builder.build());
        setOnClickPlayPause(context);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void updateNotification() {
        if (MusicHandler.hybridMediaPlayer.isPlaying()){
            remoteViews.setImageViewResource(R.id.iv_noti_player_pause, R.drawable.ic_pause_really_black_24dp);
            builder.setOngoing(true);
        } else {
            remoteViews.setImageViewResource(R.id.iv_noti_player_pause, R.drawable.ic_play_arrow_really_black_24dp);
            builder.setOngoing(false);
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private static void setOnClickPlayPause(Context context){
        Intent intent = new Intent(context, MusicService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_noti_player_pause, pendingIntent);
    }
}
