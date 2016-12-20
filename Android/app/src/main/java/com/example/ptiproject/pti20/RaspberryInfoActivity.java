package com.example.ptiproject.pti20;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

/**
 * Created by eric on 8/12/16.
 */

public class RaspberryInfoActivity extends AppCompatActivity {

    static private String resultTemp = "";
    static private String resultTempLocal = "";
    static private String resultRAM = "";
    static Handler h = new Handler();
    static TextView tempCPU;
    static TextView ram;
    static TextView tempLocal;
    static boolean connectedTemp = false;
    static boolean connectedTempLocal = false;
    static boolean connectedRam = false;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raspberry_info);

        tempCPU = (TextView) findViewById(R.id.temp_cpu);
        tempCPU.setText("CPU: ");

        ram = (TextView) findViewById(R.id.ram);
        ram.setText("RAM: ");

        tempLocal = (TextView) findViewById(R.id.temp_local);
        tempLocal.setText("LOCAL: ");

        checkRAM();
        checkTemp();
        checkTempLocal();

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
                checkRAM();
                checkTemp();
                checkTempLocal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkTemp() {
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RaspberryInfoActivity.this);
                    String username = prefs.getString(getString(R.string.pref_username_key), getString(R.string.pref_username_default));
                    String password = prefs.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default));
                    String domainName = prefs.getString("domain name", "");
                    int port = Integer.parseInt(prefs.getString("port", ""));
                    String command = "echo " + password + " | " + "sudo -S /opt/vc/bin/vcgencmd measure_temp";
                    resultTemp = executeRemoteCommand(username, password,domainName, port, command);
                    connectedTemp = true;
                } catch (Exception e) {
                    connectedTemp = false;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected  void onPostExecute(Void v) {
                if (!resultTemp.isEmpty()) {
                    String temp = resultTemp.substring(5,9);
                    tempCPU.setText("CPU: " + temp + "Cº");
                    connectedTemp = false;
                }
            }
        }.execute(1);
    }

    private void checkTempLocal() {
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RaspberryInfoActivity.this);
                    String username = prefs.getString(getString(R.string.pref_username_key), getString(R.string.pref_username_default));
                    String password = prefs.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default));
                    String domainName = prefs.getString("domain name", "");
                    int port = Integer.parseInt(prefs.getString("port", ""));
                    String command = "awk 'END{print}' temperatura.txt";
                    resultTempLocal = executeRemoteCommand(username, password,domainName, port, command);
                    connectedTempLocal = true;
                } catch (Exception e) {
                    connectedTempLocal = false;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected  void onPostExecute(Void v) {
                if (!resultTempLocal.isEmpty()) {
                    String temp = resultTempLocal.substring(0,2);
                    tempLocal.setText("LOCAL: " + temp + "Cº");
                    connectedTempLocal = false;
                }
            }
        }.execute(1);
    }

    private void checkRAM() {
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RaspberryInfoActivity.this);
                    String username = prefs.getString(getString(R.string.pref_username_key), getString(R.string.pref_username_default));
                    String password = prefs.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default));
                    String domainName = prefs.getString("domain name", "");
                    int port = Integer.parseInt(prefs.getString("port", ""));
                    String command = "free | grep Mem | awk '{print $3/$2 * 100.0}'";
                    resultRAM = executeRemoteCommand(username, password,domainName, port, command);
                    connectedRam = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    connectedRam = false;
                }
                return null;
            }

            @Override
            protected  void onPostExecute(Void v) {
                if (!resultRAM.isEmpty()) {
                    String ramString = resultRAM.substring(0,2);
                    ram.setText("RAM: " + ramString + "%");
                    connectedRam = false;
                }
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

        }
        channelssh.disconnect();

        return baos.toString();
    }

}