package sonhoang.vn.freemusic.networks;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Son Hoang on 11/15/2017.
 */

public interface MusicInterface {
    @GET("api")
    Call<MusicTypesResponseJSON> getMusicTypes();

    @GET("https://itunes.apple.com/us/rss/topsongs/limit=50/genre={id}/explicit=true/json")
    Call<TopSongsResponseJSON> getTopSongs(@Path("id") String id);

    @GET("https://tk-gx.herokuapp.com/api/audio")
    Call<SearchSongResponseJSON> getSearchSong(@Query("search_terms") String search);
}
