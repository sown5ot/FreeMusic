package sonhoang.vn.freemusic.events;

import java.util.List;

import sonhoang.vn.freemusic.databases.TopSongsModel;

/**
 * Created by Son Hoang on 11/29/2017.
 */

public class OnClickTopSongsEvent {
    public TopSongsModel topSongsModel;
    public List<TopSongsModel> topSongsModels;

    public OnClickTopSongsEvent(TopSongsModel topSongsModel, List<TopSongsModel> topSongsModels){
        this.topSongsModel = topSongsModel;
        this.topSongsModels = topSongsModels;
    }
}
