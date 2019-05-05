package id.ngulik.rikadapps;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WalkthroughActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);


        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        WalkthroughPagerAdapter myPagerAdapter = new WalkthroughPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);

//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
//        tabLayout.setupWithViewPager(viewPager);

    }


    public void startHome(View view) {
        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
    }
}
