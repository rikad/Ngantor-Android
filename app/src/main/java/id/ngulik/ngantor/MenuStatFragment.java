package id.ngulik.ngantor;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuStatFragment extends Fragment {

    private WebView wv;

    public MenuStatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_stat, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                "id.ngulik.ngantor.saved", getContext().MODE_PRIVATE);
        String user_id = sharedPref.getString(getString(R.string.saved_user_id), "1");

        wv = (WebView) view.findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new MyBrowser());
        wv.loadUrl(getString(R.string.endpoint)+"api/profile.php?id="+user_id);

        // Inflate the layout for this fragment
        return view;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
