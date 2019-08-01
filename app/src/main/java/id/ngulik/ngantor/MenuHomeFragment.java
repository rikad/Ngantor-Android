package id.ngulik.ngantor;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.ngulik.ngantor.api.ApiEndPoint;
import id.ngulik.ngantor.api.ApiService;
import id.ngulik.ngantor.model.ResponseModel;
import id.ngulik.ngantor.model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuHomeFragment extends Fragment implements View.OnClickListener {

    TextView mClock, mType, mDistance;
    ImageButton btnAbsen;
    String stlocation, name, user_id, position;
    SharedPreferences sharedPref;

    public MenuHomeFragment() {
        // Required empty public constructor
    }

    private void setListeners() {
        btnAbsen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAbsen:
                doAbsen();
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_home, container, false);

        mClock = view.findViewById(R.id.textTime);
        mDistance = view.findViewById(R.id.textDistance);
        mType = view.findViewById(R.id.textType);
        btnAbsen = view.findViewById(R.id.btnAbsen);

        sharedPref = getActivity().getSharedPreferences(
                "id.ngulik.ngantor.saved", getContext().MODE_PRIVATE);
        name = sharedPref.getString(getString(R.string.saved_name), "Name");
        user_id = sharedPref.getString(getString(R.string.saved_user_id), "1");
        position = sharedPref.getString(getString(R.string.saved_position), "Position");
        stlocation = sharedPref.getString(getString(R.string.saved_location), "-6.886963,107.615392");

        TextView tvName = view.findViewById(R.id.home_name);
        TextView tvPosition = view.findViewById(R.id.home_position);
        tvName.setText(name);
        tvPosition.setText(position);

        setListeners();

        return view;
    }

    private void doAbsen(){

        //Declare Retrofit
        ApiService api = ApiEndPoint.getClient().create(ApiService.class);

        Call<ResponseModel> addSuperheroResponseModelCall = api.postAbsen(Integer.parseInt(user_id));
        addSuperheroResponseModelCall.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                String statusCode = response.body().getStatusCode();
                String message = response.body().getMessage();

                if (statusCode.equals("200")) {
                    if (message.equals("true")) {
                        Toast.makeText(getContext(), "Absen Berhasil", Toast.LENGTH_SHORT)
                                .show();
                        btnAbsen.setEnabled(false);
                    } else {
                        Toast.makeText(getContext(), "Gagal Silahkan Coba Lagi", Toast.LENGTH_SHORT)
                                .show();
                    }
                }  else  {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getContext(), "Oops, your connection is WONGKY! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Handler mHandler = new Handler();
    private Runnable timerTask = new Runnable() {
        @Override
        public void run() {
            getLastInfo(user_id);
            FusedLocationProviderClient fusedLocationClient;
            SimpleDateFormat ft = new SimpleDateFormat("kk:mm");
            Date now = new Date();
            Date z = null;
            try {
                z = ft.parse("12:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String currentDate = String.format(ft.format(now));

            mClock.setText(currentDate);

            if (now.before(z)) {
                mType.setText("Masuk");
            } else {
                mType.setText("Pulang");
            }

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.d("GPS", "onSuccess: " + location.getLatitude() + "," + location.getLongitude());
                                Location kantor = new Location("point kantor");

                                String[] split = stlocation.split(",");
                                kantor.setLatitude(Double.parseDouble(split[0]));
                                kantor.setLongitude(Double.parseDouble(split[1]));

                                float distance = location.distanceTo(kantor);
                                if (distance < 60) {
                                    btnAbsen.setEnabled(true);
                                } else {
                                    btnAbsen.setEnabled(false);
                                }

                                mDistance.setText(Math.round(distance) + "m");
                            }
                        }
                    });

            mHandler.postDelayed(timerTask,6000);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mHandler.post(timerTask);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(timerTask);
    }

    private void getLastInfo(String id) {
        //Declare Retrofit
        ApiService api = ApiEndPoint.getClient().create(ApiService.class);

        Call<ResponseModel> addSuperheroResponseModelCall = api.getUser(id);
        addSuperheroResponseModelCall.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                String statusCode = response.body().getStatusCode();
                String message = response.body().getMessage();
                Log.d("NETWORK", "onResponse: "+message);

                if (statusCode.equals("ok")) {
                    if (message.equals("true")) {

                        UserModel user = response.body().getData();

                        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                                "id.ngulik.ngantor.saved", getContext().MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.saved_email), user.getEmail());
                        editor.putString(getString(R.string.saved_name), user.getName());
                        editor.putString(getString(R.string.saved_position), user.getPosition());
                        editor.putString(getString(R.string.saved_office_id), user.getOfficeId());
                        editor.putString(getString(R.string.saved_user_id), user.getId());
                        editor.putString(getString(R.string.saved_location), user.getLocation());
                        editor.commit();

                    }
                } else  {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                //
            }
        });
    }

}