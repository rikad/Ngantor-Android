package id.ngulik.ngantor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.ngulik.ngantor.api.ApiEndPoint;
import id.ngulik.ngantor.api.ApiService;
import id.ngulik.ngantor.model.ResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuProfileFragment extends Fragment implements View.OnClickListener {
    private static View view;
    private static EditText fullName, emailId, position,
            password, confirmPassword, officeId;
    private TextView login;
    private Button signUpButton;
    String user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu_profile, container, false);

        initViews();
        setListeners();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user:

                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
        }

    }

    // Initialize all views
    private void initViews() {
        fullName = (EditText) view.findViewById(R.id.name);
        emailId = (EditText) view.findViewById(R.id.userEmailId);
        position = (EditText) view.findViewById(R.id.position);
        officeId = (EditText) view.findViewById(R.id.officeId);
        password = (EditText) view.findViewById(R.id.password);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        signUpButton = (Button) view.findViewById(R.id.signUpBtn);
        login = (TextView) view.findViewById(R.id.already_user);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                "id.ngulik.ngantor.saved", getContext().MODE_PRIVATE);
        fullName.setText(sharedPref.getString(getString(R.string.saved_name), "Name"));
        emailId.setText(sharedPref.getString(getString(R.string.saved_email), "1"));
        position.setText(sharedPref.getString(getString(R.string.saved_position), "Position"));
        officeId.setText(sharedPref.getString(getString(R.string.saved_office_id), "Position"));

        user_id = sharedPref.getString(getString(R.string.saved_user_id), "0");

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getPosition = position.getText().toString();
        String getOfficeId = officeId.getText().toString();
        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getPosition.equals("") || getPosition.length() == 0)

            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required.");

            // Check if email id valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");

            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword))
            new CustomToast().Show_Toast(getActivity(), view,
                    "Both password doesn't match.");

            // Make sure user should check Terms and Conditions checkbox
        else
            doEdit(user_id,getFullName,getEmailId,getPosition,getPassword,getOfficeId);

    }

    private void doEdit(String id,String name, String email,String position,String password,String officeId) {
        //Declare Retrofit
        ApiService api = ApiEndPoint.getClient().create(ApiService.class);

        Call<ResponseModel> addSuperheroResponseModelCall = api.postEditUser(id,name, email, position, password, officeId);
        addSuperheroResponseModelCall.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                String statusCode = response.body().getStatusCode();
                String message = response.body().getMessage();

                if (statusCode.equals("ok")) {
                    if (message.equals("true")) {
                        Toast.makeText(getActivity(), "Berhasil di rubah", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(getActivity(), "Gagal Silahkan Coba Lagi", Toast.LENGTH_SHORT)
                                .show();
                    }
                }  else  {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Oops, your connection is WONGKY! ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
