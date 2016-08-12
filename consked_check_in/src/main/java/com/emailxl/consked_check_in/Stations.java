package com.emailxl.consked_check_in;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.emailxl.consked_check_in.internal_db.ExpoHandler;
import com.emailxl.consked_check_in.internal_db.ExpoInt;
import com.emailxl.consked_check_in.internal_db.WorkerHandler;
import com.emailxl.consked_check_in.internal_db.WorkerInt;

import java.util.List;

public class Stations extends AppCompatActivity {
    private static final String TAG = "Stations";
    private static boolean LOG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);
        if (LOG) Log.i(TAG, "Create page");

        ExpoHandler dbe = new ExpoHandler(this);
        List<ExpoInt> expoInts = dbe.getAllExpos();

        String output = "";
        if (expoInts != null && expoInts.size() != 0) {
            for (ExpoInt expoInt: expoInts) {
                output += expoInt.toString() + "\n\n";
            }
        }

        TextView tvTest = (TextView) findViewById(R.id.test);
        tvTest.setText(output.toString());
    }
}
