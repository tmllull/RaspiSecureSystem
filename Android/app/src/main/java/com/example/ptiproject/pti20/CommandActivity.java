package com.example.ptiproject.pti20;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Properties;

/**
 * Created by eric on 25/11/16.
 */

public class CommandActivity extends AppCompatActivity{

    static private String command;
    static private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);

        final Button sendButton = (Button) findViewById(R.id.button_to_send_command);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                EditText commandToSend = (EditText) findViewById(R.id.command_to_send);
                command = commandToSend.getText().toString();
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CommandActivity.this);
                            String username = prefs.getString(getString(R.string.pref_username_key), getString(R.string.pref_username_default));
                            String password = prefs.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default));
                            String domainName = prefs.getString("domain name", "");
                            int port = Integer.parseInt(prefs.getString("port", ""));
                            result = executeRemoteCommand(username, password,domainName, port);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected  void onPostExecute(Void v) {
                        if (!result.isEmpty()) {
                            TextView textResult = (TextView) findViewById(R.id.textView_output);
                            textResult.setMovementMethod(new ScrollingMovementMethod());
                            textResult.setText(result);
                        }
                    }
                }.execute(1);
            }
        });
    }

    public static String executeRemoteCommand(String username, String password, String hostname, int port) throws Exception {

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
        //channelssh.disconnect();

        return baos.toString();
    }
}
