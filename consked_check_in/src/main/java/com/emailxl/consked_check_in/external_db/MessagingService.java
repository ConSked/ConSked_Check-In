package com.emailxl.consked_check_in.external_db;

import android.util.Log;

import com.emailxl.consked_check_in.internal_db.ChangeLogHandler;
import com.emailxl.consked_check_in.internal_db.ChangeLogInt;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = "MessagingService";
    private static final Boolean LOG = true;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (LOG) Log.i(TAG, "Starting onMessageReceived");

        Map<String, String> data = remoteMessage.getData();

        ChangeLogHandler dbc = new ChangeLogHandler(this);
        ChangeLogInt changeLog = new ChangeLogInt();

        changeLog.setTimestamp(System.currentTimeMillis());
        changeLog.setSource("remote");
        changeLog.setOperation(data.get("operation"));
        changeLog.setTableName(data.get("tableName"));
        changeLog.setJson(data.get("json"));
        changeLog.setIdInt(0);
        changeLog.setIdExt(Integer.parseInt(data.get("idExt")));
        changeLog.setDone(0);

        dbc.addChangeLog(changeLog);

        if (LOG) Log.i(TAG, "Ending onMessageReceived");
    }
}
