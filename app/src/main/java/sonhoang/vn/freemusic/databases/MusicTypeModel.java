package sonhoang.vn.freemusic.databases;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Son Hoang on 11/18/2017.
 */

public class MusicTypeModel extends RealmObject{
    @PrimaryKey
    public String key;
    public String id;
    public int imageID;
    public boolean isFavorite;
}
