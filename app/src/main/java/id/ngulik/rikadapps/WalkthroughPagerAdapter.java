package id.ngulik.rikadapps;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class WalkthroughPagerAdapter extends FragmentStatePagerAdapter {

    public WalkthroughPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override    public Fragment getItem(int position) {
        switch (position){
            case 0: return new Walkthrought1Fragment();
            case 1: return new Walkthrought2Fragment();
            case 2: return new Walkthrought3Fragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Tab 1";
            case 1: return "Tab 2";
            default: return null;
        }
    }
}
