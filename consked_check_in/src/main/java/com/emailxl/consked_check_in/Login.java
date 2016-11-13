package com.emailxl.consked_check_in;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.emailxl.consked_check_in.external_db.WorkerLoginExt;
import com.emailxl.consked_check_in.utils.AppConstants;
import com.emailxl.consked_check_in.utils.WorkerLoginAsync;

import java.util.concurrent.ExecutionException;

import static com.emailxl.consked_check_in.utils.Utils.toastError;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    private static final boolean LOG = AppConstants.LOG_MAIN;

    private EditText etEmail, etPassword;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            etEmail = (EditText) findViewById(R.id.edit_email);
            etPassword = (EditText) findViewById(R.id.edit_password);
            tvError = (TextView) findViewById(R.id.error_login);

            findViewById(R.id.button_login).setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            String email = etEmail.getText().toString().trim();
                            String password = etPassword.getText().toString().trim();

                            WorkerLoginExt params = new WorkerLoginExt();
                            params.setEmail(email);
                            params.setPassword(password);

                            String result = "";
                            try {
                                result = new WorkerLoginAsync(Login.this, R.string.loading).execute(params).get();
                            } catch (InterruptedException | ExecutionException e) {
                                if (LOG) Log.e(TAG, e.getMessage());
                            }

                            if ("Success".equals(result)) {
                                Intent inLoading = new Intent(view.getContext(), Loading.class);
                                inLoading.putExtra("email", email);
                                inLoading.putExtra("password", password);
                                startActivity(inLoading);
                                finish();
                            } else {
                                tvError.setVisibility(View.VISIBLE);
                                tvError.setText(result);
                            }
                        }
                    }
            );
        } else {
            toastError(Login.this, R.string.error_network);
        }
    }
}
