package com.emailxl.consked_check_in;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    private static final boolean LOG = false;

    EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.edit_email);
        etPassword = (EditText) findViewById(R.id.edit_password);

        findViewById(R.id.button_login).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String email = etEmail.getText().toString().trim();
                        String password = etPassword.getText().toString().trim();

                        Intent inLoading = new Intent(view.getContext(), Loading.class);
                        inLoading.putExtra("email", email);
                        inLoading.putExtra("password", password);
                        startActivity(inLoading);
                        finish();
                    }
                }
        );
    }
}
