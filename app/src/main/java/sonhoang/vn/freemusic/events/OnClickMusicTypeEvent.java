package sonhoang.vn.freemusic.events;

import sonhoang.vn.freemusic.databases.MusicTypeModel;

/**
 * Created by Son Hoang on 11/22/2017.
 */

public class OnClickMusicTypeEvent {
    public MusicTypeModel musicTypeModel;

    public OnClickMusicTypeEvent(MusicTypeModel musicTypeModel) {
        this.musicTypeModel = musicTypeModel;
    }
}
