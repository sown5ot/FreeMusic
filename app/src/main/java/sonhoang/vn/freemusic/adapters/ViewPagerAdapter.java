package sonhoang.vn.freemusic.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import sonhoang.vn.freemusic.fragments.DownloadFragment;
import sonhoang.vn.freemusic.fragments.FavoriteFragment;
import sonhoang.vn.freemusic.fragments.MusicTypeFragment;

/**
 * Created by Son Hoang on 11/18/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final int TAB_COUNT = 3;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new MusicTypeFragment();
            case 1: return new FavoriteFragment();
            case 2: return new DownloadFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
