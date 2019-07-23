package id.ngulik.ngantor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPref = getSharedPreferences(
                "id.ngulik.ngantor.saved", getApplicationContext().MODE_PRIVATE);
        final int user_id = sharedPref.getInt(getString(R.string.saved_user_id), 0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user_id != 0) {
                    startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                finish();
            }
        }, 2000L); //3000 L = 3 detik

    }
}
