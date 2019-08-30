package wholesalefactory.co.drawer;

import androidx.fragment.app.Fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;


public class Policies extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    WebView wvPage1;
    String url;
    ProgressBar proWeb;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Policies() {
    }

    public static Policies newInstance(String param1, String param2) {
        Policies fragment = new Policies();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_policies, container, false);

        proWeb=(ProgressBar)view.findViewById(R.id.proWeb);
        proWeb.setVisibility(View.VISIBLE);
        url= URLs.URL_privacypolicy;
        wvPage1 = (WebView)view.findViewById(R.id.wvPage1);
        wvPage1.loadUrl(url);
        WebSettings settings = wvPage1.getSettings();
        settings.setJavaScriptEnabled(true);

        wvPage1.setWebViewClient(new MyWebViewClient());
        wvPage1.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                proWeb.setProgress(newProgress);
                if (newProgress == 100) {
                    proWeb.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
}