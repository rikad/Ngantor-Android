package id.ngulik.rikadapps;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class Walkthrought3Fragment extends Fragment {


    public Walkthrought3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_walkthrought3, container, false);

        Button startHome = view.findViewById(R.id.btnWalkthroughStart);
        startHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MenuActivity.class));
                getActivity().finish();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
