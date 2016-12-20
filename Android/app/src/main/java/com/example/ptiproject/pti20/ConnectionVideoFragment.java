package com.example.ptiproject.pti20;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectionVideoFragment extends Fragment {

    public static WebView webView;
    private String webcamURL;
    String usernameCamera;
    String passwordCamera;
    String domainNameCamera;
    int portCamera;
    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        usernameCamera = pref.getString("userCamera", getString(R.string.pref_username_default));
        passwordCamera = pref.getString("passwordCamera", getString(R.string.pref_password_default));
        domainNameCamera = pref.getString("domainNameCamera", "");
        portCamera = Integer.parseInt(pref.getString("portCamera", "0"));
        webcamURL = "http://" + domainNameCamera + ":" + portCamera + "/video.cgi";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_video_webcam, container, false);

        webView = (WebView) rootView.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient());
        //webView.setHttpAuthUsernamePassword("ptiproject.ddns.net:8081", "realm",username,password);
        webView.enableSlowWholeDocumentDraw();
        webView.loadUrl(webcamURL);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedHttpAuthRequest(WebView view,
                                                  HttpAuthHandler handler, String host, String realm) {
                handler.proceed(usernameCamera, passwordCamera);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //prDialog.setMessage("Please wait ...");
                //prDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                    /*if (prDialog != null) {
                        prDialog.dismiss();
                    }*/
            }
        });

        button = (Button) rootView.findViewById(R.id.captureButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    screenshot(webView);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        MainActivity.fab.hide();
        return rootView;
    }

    public Bitmap screenshot(WebView webView) throws IOException {
        webView.measure(View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        webView.layout(0, 0, webView.getMeasuredWidth(), webView.getMeasuredHeight());
        webView.setDrawingCacheEnabled(true);
        webView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(webView.getMeasuredWidth(),
                webView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        int iHeight = bitmap.getHeight();
        canvas.drawBitmap(bitmap, 0, iHeight, paint);
        webView.draw(canvas);
        final File undo = savebitmap(bitmap);

        Snackbar snackbar = Snackbar
                .make(getView(), "Image saved successfully!", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar snackbar1 = Snackbar.make(getView(), "Image deleted!", Snackbar.LENGTH_SHORT);
                        // Changing message text color
                        snackbar1.setActionTextColor(Color.BLACK);

                        // Changing action button text color
                        View sbView = snackbar1.getView();
                        sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(20);
                        undo.delete();
                        snackbar1.show();
                    }
                });
        // Changing message text color
        snackbar.setActionTextColor(Color.BLACK);

        // Changing action button text color
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        snackbar.show();
        return bitmap;
    }

    public static File savebitmap(Bitmap bmp) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String date = sdf.format(new Date());
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "Capture_" + date + ".jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }
}
