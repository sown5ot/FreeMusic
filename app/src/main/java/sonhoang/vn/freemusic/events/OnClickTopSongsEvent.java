package sonhoang.vn.freemusic.events;

import sonhoang.vn.freemusic.databases.TopSongsModel;

/**
 * Created by Son Hoang on 11/29/2017.
 */

public class OnClickTopSongsEvent {
    public TopSongsModel topSongsModel;

    public OnClickTopSongsEvent(TopSongsModel topSongsModel){
        this.topSongsModel = topSongsModel;
    }
}
