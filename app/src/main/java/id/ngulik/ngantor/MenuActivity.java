package id.ngulik.ngantor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import id.ngulik.ngantor.api.ApiEndPoint;
import id.ngulik.ngantor.api.ApiService;
import id.ngulik.ngantor.model.ResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()){
                case R.id.navigation_home:
                    fragment = new MenuHomeFragment();
                    break;
                case R.id.navigation_profile:
                    fragment = new MenuProfileFragment();
                    break;
                case R.id.navigation_stat:
                    fragment = new MenuStatFragment();
                    break;
            }

            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        loadFragment(new MenuHomeFragment());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }

        return false;
    }

    private void doAbsen(int user_id){
        //Declare Retrofit
        ApiService api = ApiEndPoint.getClient().create(ApiService.class);

        Call<ResponseModel> addSuperheroResponseModelCall = api.postAbsen(user_id);
        addSuperheroResponseModelCall.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                String statusCode = response.body().getStatusCode();
                String message = response.body().getMessage();

                if (statusCode.equals("200")) {
                    if (message.equals("true")) {
                        new LoginActivity().replaceLoginFragment();
                        Toast.makeText(MenuActivity.this, "Absen Berhasil", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(MenuActivity.this, "Gagal Silahkan Coba Lagi", Toast.LENGTH_SHORT)
                                .show();
                    }
                }  else  {
                    Toast.makeText(MenuActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MenuActivity.this, "Oops, your connection is WONGKY! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void launchClick(View view) {
        switch(view.getId()) {
            case R.id.btnAbsen:
                SharedPreferences sharedPref = getSharedPreferences(
                        "id.ngulik.ngantor.saved", view.getContext().MODE_PRIVATE);
                int user_id = sharedPref.getInt(getString(R.string.saved_user_id), 0);

                doAbsen(user_id);

                Log.d("btn", "launchClick: clicked absen id "+user_id);
                break;
        }
    }
}