package com.example.ptiproject.pti20;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

/**
 * Created by eric on 25/11/16.
 */

public class LightsActivity extends AppCompatActivity{

    private static String result = "";
    private static boolean isON = false;
    private ImageButton topLeftButton;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights);

        topLeftButton = (ImageButton) findViewById(R.id.imageButton5);
        topLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isON) {
                    Log.e("LightsActivity","close");
                    closeLight();
                    showSnackBar();
                } else {
                    openLight();
                    showSnackBar();
                }
            }
        });
        ImageButton topRightButton = (ImageButton) findViewById(R.id.imageButton);
        ImageButton middleRightButton = (ImageButton) findViewById(R.id.imageButton6);
        ImageButton middleLeftButton = (ImageButton) findViewById(R.id.imageButton3);
        ImageButton bottomRightButton = (ImageButton) findViewById(R.id.imageButton4);
        ImageButton bottomLeftButton = (ImageButton) findViewById(R.id.imageButton2);

        topLeftButton.setBackgroundResource(android.R.drawable.btn_default);
        topRightButton.setBackgroundResource(android.R.drawable.btn_default);
        middleLeftButton.setBackgroundResource(android.R.drawable.btn_default);
        middleRightButton.setBackgroundResource(android.R.drawable.btn_default);
        bottomLeftButton.setBackgroundResource(android.R.drawable.btn_default);
        bottomRightButton.setBackgroundResource(android.R.drawable.btn_default);

        check();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lights, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                check();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSnackBar() {
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Applying...", Snackbar.LENGTH_LONG);

        // Changing action button text color
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        snackbar.show();
    }

    private static String executeRemoteCommand(String username, String password, String hostname, int port, String command) throws Exception {

        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, port);
        session.setPassword(password);

        // Avoid asking for key confirmation
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        session.connect();

        // SSH Channel
        ChannelExec channelssh = (ChannelExec)
                session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        // Execute command
        channelssh.setCommand(command);
        channelssh.connect();
        while(!channelssh.isClosed()){
            SystemClock.sleep(500);
            channelssh.disconnect();
        }

        return baos.toString();
    }

    private void openLight() {
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LightsActivity.this);
                    String username = prefs.getString(getString(R.string.pref_username_key), getString(R.string.pref_username_default));
                    String password = prefs.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default));
                    String domainName = prefs.getString("domain name", "");
                    int port = Integer.parseInt(prefs.getString("port", "0"));
                    String command = "/home/demo/lights.sh";
                    executeRemoteCommand(username, password,domainName, port, command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected  void onPostExecute(Void v) {
                topLeftButton.setBackgroundColor(getResources().getColor(R.color.greenDark));
                isON = true;
            }
        }.execute(1);
    }

    private void closeLight() {
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LightsActivity.this);
                    String username = prefs.getString(getString(R.string.pref_username_key), getString(R.string.pref_username_default));
                    String password = prefs.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default));
                    String domainName = prefs.getString("domain name", "");
                    int port = Integer.parseInt(prefs.getString("port", ""));
                    String command = "/home/demo/lights.sh";
                    executeRemoteCommand(username, password,domainName, port, command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected  void onPostExecute(Void v) {
                topLeftButton.setBackgroundResource(android.R.drawable.btn_default);
                isON = false;
            }
        }.execute(1);
    }

    private void check() {
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LightsActivity.this);
                    String username = prefs.getString(getString(R.string.pref_username_key), getString(R.string.pref_username_default));
                    String password = prefs.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default));
                    String domainName = prefs.getString("domain name", "");
                    int port = Integer.parseInt(prefs.getString("port", ""));
                    String command = "cat /home/demo/lights.state";
                    result = executeRemoteCommand(username, password,domainName, port, command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected  void onPostExecute(Void v) {
                if (!result.isEmpty()) {
                    if (result.equals("ON\n")) {
                        topLeftButton.setBackgroundColor(getResources().getColor(R.color.greenDark));
                        isON = true;
                    }
                    else if (result.equals("OFF\n")) {
                        topLeftButton.setBackgroundResource(android.R.drawable.btn_default);
                        isON = false;
                    }
                }
            }
        }.execute(1);
    }
}
