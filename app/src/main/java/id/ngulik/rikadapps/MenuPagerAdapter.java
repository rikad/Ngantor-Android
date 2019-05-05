package id.ngulik.rikadapps;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MenuPagerAdapter extends FragmentStatePagerAdapter {

    public MenuPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override public Fragment getItem(int position) {
        switch (position){
            case 0: return new MenuHomeFragment();
            case 1: return new MenuDailyFragment();
            case 2: return new MenuGalleryFragment();
            case 3: return new MenuMusicFragment();
            case 4: return new MenuProfileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Home";
            case 1: return "Daily";
            case 2: return "Gallery";
            case 3: return "Music";
            case 4: return "Profile";
            default: return null;
        }
    }
}
