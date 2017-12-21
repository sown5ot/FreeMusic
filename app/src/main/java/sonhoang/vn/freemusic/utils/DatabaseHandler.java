package sonhoang.vn.freemusic.utils;

import java.util.List;

import io.realm.Realm;
import sonhoang.vn.freemusic.databases.MusicTypeModel;

/**
 * Created by Son Hoang on 12/16/2017.
 */

public class DatabaseHandler {
    private static Realm realm = Realm.getDefaultInstance();

    public static void addMusicType(MusicTypeModel musicTypeModel){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(musicTypeModel);
        realm.commitTransaction();
    }

    public static List<MusicTypeModel> getMusicType(){
        return realm.where(MusicTypeModel.class).findAll();
    }

    public static void setFavorite(MusicTypeModel musicTypeModel){
        realm.beginTransaction();
        musicTypeModel.isFavorite = !musicTypeModel.isFavorite;
        realm.commitTransaction();
    }

    public static List<MusicTypeModel> getFavoriteMusicType(){
        return realm.where(MusicTypeModel.class).equalTo("isFavorite", true).findAll();
    }
}
