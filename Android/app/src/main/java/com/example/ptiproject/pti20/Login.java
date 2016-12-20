package com.example.ptiproject.pti20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity{

    private Button btLog;
    private EditText etUser, etPass;
    private TextInputLayout inputLayoutName, inputLayoutPassword;
    public SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = PreferenceManager.getDefaultSharedPreferences(Login.this);

        btLog = (Button) findViewById(R.id.bt_login);
        etUser = (EditText) findViewById(R.id.et_user);
        etPass = (EditText) findViewById(R.id.et_pass);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        btLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUser.getText().toString().isEmpty()) {
                    inputLayoutName.setErrorEnabled(true);
                    inputLayoutName.setError("Has d'introduïr un nom");
                }
                else if (etPass.getText().toString().isEmpty()) {
                    inputLayoutName.setErrorEnabled(false);
                    inputLayoutPassword.setErrorEnabled(true);
                    inputLayoutPassword.setError("Has d'introduir una contrassenya");
                }
                else {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("first", "nope");
                    editor.commit();
                    editor.putString("username", etUser.getText().toString());
                    editor.commit();
                    editor.putString("password", etPass.getText().toString());
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
