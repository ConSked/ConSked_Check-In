package com.emailxl.consked_check_in.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private int mViewResourceId;
    private List<ShiftCheckin> mShiftCheckinList;

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
        ViewHolder holder;

        ShiftCheckin shiftCheckin = mShiftCheckinList.get(position);
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

            holder.rgShift = (RadioGroup) convertView.findViewById(R.id.rgroup_shift);
            holder.rbCheckIn = (RadioButton) convertView.findViewById(R.id.radio_checkin);
            holder.rbCheckOut = (RadioButton) convertView.findViewById(R.id.radio_checkout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.rgShift.setOnCheckedChangeListener(null);

        if ("CHECK_IN".equals(statusType)) {
            holder.rbCheckOut.setChecked(true);
        } else if ("CHECK_OUT".equals(statusType)) {
            holder.rbCheckIn.setChecked(true);
        }

        holder.rgShift.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                        String now = sdf.format(new Date());

                        String statusType = "";
                        switch(checkedId) {
                            case R.id.radio_checkin:
                                statusType = "CHECK_OUT";
                                break;
                            case R.id.radio_checkout:
                                statusType = "CHECK_IN";
                                break;
                            default:
                                break;
                        }

                        ShiftStatusInt shiftStatusInt = new ShiftStatusInt();
                        shiftStatusInt.setShiftstatusIdExt(0);
                        shiftStatusInt.setWorkerIdExt(workerIdExt);
                        shiftStatusInt.setStationIdExt(stationIdExt);
                        shiftStatusInt.setExpoIdExt(expoIdExt);
                        shiftStatusInt.setStatusType(statusType);
                        shiftStatusInt.setStatusTime(now);
                        dbs.addShiftStatus(shiftStatusInt, 1);
                    }
                }
        );


        return convertView;
    }

    private static class ViewHolder {
        TextView tvWorkerName;
        RadioGroup rgShift;
        RadioButton rbCheckIn;
        RadioButton rbCheckOut;
    }
}
