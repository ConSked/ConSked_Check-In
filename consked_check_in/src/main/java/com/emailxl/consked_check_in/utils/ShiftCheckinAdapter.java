package com.emailxl.consked_check_in.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.emailxl.consked_check_in.R;
import com.emailxl.consked_check_in.internal_db.ShiftAssignmentInt;
import com.emailxl.consked_check_in.internal_db.ShiftCheckin;
import com.emailxl.consked_check_in.internal_db.ShiftStatusHandler;
import com.emailxl.consked_check_in.internal_db.ShiftStatusInt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author ECG
 */

public class ShiftCheckinAdapter extends ArrayAdapter<ShiftCheckin> {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<ShiftCheckin> mShiftCheckinList;
    private int mViewResourceId;

    public ShiftCheckinAdapter(Context context, int viewResourceId, List<ShiftCheckin> shiftCheckinList) {
        super(context, viewResourceId, shiftCheckinList);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mViewResourceId = viewResourceId;
        mShiftCheckinList = shiftCheckinList;
    }

    @Override
    public int getCount() {
        return mShiftCheckinList.size();
    }

    @Override
    public ShiftCheckin getItem(int position) {
        return mShiftCheckinList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mShiftCheckinList.get(position).getShiftAssignment().getIdInt();
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;

        final ShiftCheckin shiftCheckin = mShiftCheckinList.get(position);
        final ShiftAssignmentInt shiftAssignment = shiftCheckin.getShiftAssignment();
        final int workerIdExt = shiftAssignment.getWorkerIdExt();
        final int stationIdExt = shiftAssignment.getStationIdExt();
        final int expoIdExt = shiftAssignment.getExpoIdExt();
        String name = shiftCheckin.getName();
        String statusType = shiftCheckin.getStatusType();

        final ShiftStatusHandler dbs = new ShiftStatusHandler(mContext);

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
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                            String now = sdf.format(new Date());

                            ShiftStatusInt shiftstatusInt = new ShiftStatusInt();
                            shiftstatusInt.setShiftstatusIdExt(0);
                            shiftstatusInt.setWorkerIdExt(shiftAssignment.getWorkerIdExt());
                            shiftstatusInt.setStationIdExt(shiftAssignment.getStationIdExt());
                            shiftstatusInt.setExpoIdExt(shiftAssignment.getExpoIdExt());
                            shiftstatusInt.setStatusType("CHECK_OUT");
                            shiftstatusInt.setStatusTime(now);
                            dbs.addShiftStatus(shiftstatusInt, 1);

                            holder.buCheckIn.setEnabled(true);
                            holder.buCheckOut.setEnabled(false);
                        }
                    }
            );

            if ("CHECK_IN".equals(statusType)) {
                holder.buCheckIn.setEnabled(false);
                holder.buCheckOut.setEnabled(true);
            } else if ("CHECK_OUT".equals(statusType)) {
                holder.buCheckIn.setEnabled(true);
                holder.buCheckOut.setEnabled(false);
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
        TextView tvWorkerName;
        Button buCheckIn;
        Button buCheckOut;
    }
}
