package sonhoang.vn.freemusic.networks;

import java.util.List;

/**
 * Created by Son Hoang on 11/15/2017.
 */

public class MusicTypesResponseJSON {
    public List<SubObjectJSON> subgenres;

    public class SubObjectJSON {
        public String id;
        public String translation_key;
    }
}
