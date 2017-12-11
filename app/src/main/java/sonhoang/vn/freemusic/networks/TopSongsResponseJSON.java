package sonhoang.vn.freemusic.networks;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Son Hoang on 11/25/2017.
 */

public class TopSongsResponseJSON {
    public FeedJSON feed;

    public class FeedJSON {
        public List<EntryJSON> entry;

        public class EntryJSON {
            @SerializedName("im:name")
            public NameJSON name;

            @SerializedName("im:artist")
            public ArtistJSON artist;

            @SerializedName("im:image")
            public List<ImageJSON> image;

            public class ImageJSON {
                public String label;
            }

            public class ArtistJSON {
                public String label;
            }

            public class NameJSON {
                public String label;
            }
        }
    }

}
