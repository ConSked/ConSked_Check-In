package com.emailxl.consked_check_in.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.emailxl.consked_check_in.R;
import com.emailxl.consked_check_in.internal_db.ShiftAssignmentInt;
import com.emailxl.consked_check_in.internal_db.ShiftStatusHandler;
import com.emailxl.consked_check_in.internal_db.ShiftStatusInt;
import com.emailxl.consked_check_in.internal_db.WorkerHandler;
import com.emailxl.consked_check_in.internal_db.WorkerInt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author ECG
 */

public class ShiftAssignmentAdapter extends ArrayAdapter<ShiftAssignmentInt> {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<ShiftAssignmentInt> mShiftAssignmentInts;
    private int mViewResourceId;
    int expoIdExt, stationIdExt;
    private ShiftStatusHandler dbs;
    List<ShiftStatusInt> shiftStatusInts;

    public ShiftAssignmentAdapter(Context context, int viewResourceId, List<ShiftAssignmentInt> shiftAssignmentInts) {
        super(context, viewResourceId, shiftAssignmentInts);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mViewResourceId = viewResourceId;
        mShiftAssignmentInts = shiftAssignmentInts;
    }

    @Override
    public int getCount() {
        return mShiftAssignmentInts.size();
    }

    @Override
    public ShiftAssignmentInt getItem(int position) {
        return mShiftAssignmentInts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mShiftAssignmentInts.get(position).getIdInt();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        ShiftAssignmentInt shiftAssignmentInt = mShiftAssignmentInts.get(position);

        // get expo and station
        expoIdExt = shiftAssignmentInt.getExpoIdExt();
        stationIdExt = shiftAssignmentInt.getStationIdExt();

        // get worker name
        int workerIdExt = shiftAssignmentInt.getWorkerIdExt();

        WorkerHandler dbw = new WorkerHandler(mContext);
        WorkerInt workerInt = dbw.getWorkerIdExt(workerIdExt);

        String name = workerInt.getFirstName() + " " + workerInt.getLastName();

        // get shiftstatus
        dbs = new ShiftStatusHandler(mContext);
        shiftStatusInts = dbs.getShiftStatusIdExt(expoIdExt, stationIdExt, workerIdExt);

        if (convertView == null) {
            convertView = mInflater.inflate(mViewResourceId, null);

            holder = new ViewHolder();
            holder.tvWorkerName = (TextView) convertView.findViewById(R.id.workerName);
            holder.tvWorkerName.setText(name);

            holder.buCheckIn = (Button) convertView.findViewById(R.id.button_checkin);
            holder.buCheckIn.setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            int position = (Integer) view.getTag();

                            ShiftAssignmentInt shiftAssignmentInt = mShiftAssignmentInts.get(position);
                            int workerIdExt = shiftAssignmentInt.getWorkerIdExt();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                            String now = sdf.format(new Date());

                            ShiftStatusInt shiftstatusInt = new ShiftStatusInt();
                            shiftstatusInt.setShiftstatusIdExt(0);
                            shiftstatusInt.setWorkerIdExt(workerIdExt);
                            shiftstatusInt.setStationIdExt(stationIdExt);
                            shiftstatusInt.setExpoIdExt(expoIdExt);
                            shiftstatusInt.setStatusType("CHECK_IN");
                            shiftstatusInt.setStatusTime(now);
                            dbs.addShiftStatus(shiftstatusInt, 1);

                            holder.buCheckIn.setEnabled(false);
                            holder.buCheckOut.setEnabled(true);
                        }
                    }
            );

            holder.buCheckOut = (Button) convertView.findViewById(R.id.button_checkout);
            holder.buCheckOut.setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            int position = (Integer) view.getTag();

                            ShiftAssignmentInt shiftAssignmentInt = mShiftAssignmentInts.get(position);
                            int workerIdExt = shiftAssignmentInt.getWorkerIdExt();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                            String now = sdf.format(new Date());

                            ShiftStatusInt shiftstatusInt = new ShiftStatusInt();
                            shiftstatusInt.setShiftstatusIdExt(0);
                            shiftstatusInt.setWorkerIdExt(workerIdExt);
                            shiftstatusInt.setStationIdExt(stationIdExt);
                            shiftstatusInt.setExpoIdExt(expoIdExt);
                            shiftstatusInt.setStatusType("CHECK_OUT");
                            shiftstatusInt.setStatusTime(now);
                            dbs.addShiftStatus(shiftstatusInt, 1);

                            holder.buCheckIn.setEnabled(true);
                            holder.buCheckOut.setEnabled(false);
                        }
                    }
            );

            if (shiftStatusInts.size() == 0) {
                holder.buCheckIn.setEnabled(true);
                holder.buCheckOut.setEnabled(false);
            } else {
                if ("CHECK_IN".equals(shiftStatusInts.get(0).getStatusType())) {
                    holder.buCheckIn.setEnabled(false);
                    holder.buCheckOut.setEnabled(true);
                } else if ("CHECK_OUT".equals(shiftStatusInts.get(0).getStatusType())) {
                    holder.buCheckIn.setEnabled(true);
                    holder.buCheckOut.setEnabled(false);
                }
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.buCheckIn.setTag(position);
        holder.buCheckOut.setTag(position);

        return convertView;
    }

    private static class ViewHolder {
        public TextView tvWorkerName;
        public Button buCheckIn;
        public Button buCheckOut;
    }
}
