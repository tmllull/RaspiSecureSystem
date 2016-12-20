package com.example.ptiproject.pti20;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

import static com.example.ptiproject.pti20.R.id.timePicker;

/**
 * Created by ericgarciaribera on 11/12/16.
 */

public class ScheduleActivity extends AppCompatActivity{

    int hoursBeginning;
    int minutesBeginning;
    int hoursEnd;
    int minutesEnd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        final TimePicker schedule = (TimePicker) findViewById(timePicker);
        schedule.setIs24HourView(DateFormat.is24HourFormat(this));
        schedule.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            }
        });

        Button beginButton = (Button) findViewById(R.id.beginButton);
        beginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                hoursBeginning = schedule.getHour();
                minutesBeginning = schedule.getMinute();
                setRunCommand(hoursBeginning,minutesBeginning);
                showSnackBar();
            }
        });

        Button endButton = (Button) findViewById(R.id.endButton);
        endButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                hoursEnd = schedule.getHour();
                minutesEnd = schedule.getMinute();
                setKillCommand(hoursEnd,minutesEnd);
                showSnackBar();
            }
        });

        Button activateButton = (Button) findViewById(R.id.activateButton);
        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runCommand();
                showSnackBar();
            }
        });

        Button deactivateButton = (Button) findViewById(R.id.deactivateButton);
        deactivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                killCommand();
                showSnackBar();
            }
        });

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

    private void setRunCommand(final int hours, final int minutes) {
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ScheduleActivity.this);
                    String username = prefs.getString(getString(R.string.pref_username_key), getString(R.string.pref_username_default));
                    String password = prefs.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default));
                    String domainName = prefs.getString("domain name", "");
                    int port = Integer.parseInt(prefs.getString("port", ""));
                    String command = "/home/demo/runProcessCron.sh " + minutes + " " + hours;
                    executeRemoteCommand(username, password,domainName, port, command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected  void onPostExecute(Void v) {

            }
        }.execute(1);
    }

    private void setKillCommand(final int hours, final int minutes) {
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ScheduleActivity.this);
                    String username = prefs.getString(getString(R.string.pref_username_key), getString(R.string.pref_username_default));
                    String password = prefs.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default));
                    String domainName = prefs.getString("domain name", "");
                    int port = Integer.parseInt(prefs.getString("port", ""));
                    String command = "/home/demo/killProcessCron.sh " + minutes + " " + hours;
                    executeRemoteCommand(username, password,domainName, port, command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected  void onPostExecute(Void v) {

            }
        }.execute(1);
    }

    private void runCommand() {
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ScheduleActivity.this);
                    String username = prefs.getString(getString(R.string.pref_username_key), getString(R.string.pref_username_default));
                    String password = prefs.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default));
                    String domainName = prefs.getString("domain name", "");
                    int port = Integer.parseInt(prefs.getString("port", ""));
                    String command = "/home/demo/runProcess.sh";
                    executeRemoteCommand(username, password,domainName, port, command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected  void onPostExecute(Void v) {

            }
        }.execute(1);
    }

    private void killCommand() {
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ScheduleActivity.this);
                    String username = prefs.getString(getString(R.string.pref_username_key), getString(R.string.pref_username_default));
                    String password = prefs.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default));
                    String domainName = prefs.getString("domain name", "");
                    int port = Integer.parseInt(prefs.getString("port", ""));
                    String command = "/home/demo/killProcess.sh";
                    executeRemoteCommand(username, password,domainName, port, command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected  void onPostExecute(Void v) {

            }
        }.execute(1);
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
}
