package com.emailxl.consked_check_in.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.emailxl.consked_check_in.R;
import com.emailxl.consked_check_in.internal_db.StationJobInt;

import java.util.List;

/**
 * @author ECG
 */

public class StationJobAdapter extends ArrayAdapter<StationJobInt> {
    private LayoutInflater mInflater;
    private List<StationJobInt> mStationJobInts;
    private int mViewResourceId;
    private Context mContext;

    public StationJobAdapter(Context context, int viewResourceId, List<StationJobInt> stationJobInts) {
        super(context, viewResourceId, stationJobInts);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mViewResourceId = viewResourceId;
        mStationJobInts = stationJobInts;
    }

    @Override
    public int getCount() {
        return mStationJobInts.size();
    }

    @Override
    public StationJobInt getItem(int position) {
        return mStationJobInts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mStationJobInts.get(position).getIdInt();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(mViewResourceId, null);

            holder = new ViewHolder();
            holder.tvStationJobShift = (TextView) convertView.findViewById(R.id.stationJobShift);
            holder.tvStationJobLocation = (TextView) convertView.findViewById(R.id.stationJobLocation);
            holder.tvStationJobDate = (TextView) convertView.findViewById(R.id.stationJobDate);
            holder.tvStationJobTime = (TextView) convertView.findViewById(R.id.stationJobTime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final StationJobInt stationJobInt = mStationJobInts.get(position);
        holder.tvStationJobShift.setText(stationJobInt.getStationTitle());
        holder.tvStationJobLocation.setText(stationJobInt.getLocation());

        String startTime = stationJobInt.getStartTime();
        String stopTime = stationJobInt.getStopTime();

        holder.tvStationJobDate.setText(getDate(startTime));
        holder.tvStationJobTime.setText(getTime(startTime, stopTime));

        return convertView;
    }

    private static class ViewHolder {
        public TextView tvStationJobShift;
        public TextView tvStationJobLocation;
        public TextView tvStationJobDate;
        public TextView tvStationJobTime;
    }

    private static String getDate(String startTime) {
        String[] split1 = startTime.split(" ");
        String[] split2 = split1[0].split("-");

        return split2[0] + "/" + split2[1] + "/" + split2[2];
    }

    private static String getTime(String startTime, String stopTime) {
        String[] startsplit1 = startTime.split(" ");
        String[] stopsplit1 = stopTime.split(" ");

        String[] startsplit2 = startsplit1[1].split(":");
        String[] stopsplit2 = stopsplit1[1].split(":");

        return startsplit2[0] + ":" + startsplit2[1] + " - " +
                stopsplit2[0] + ":" + stopsplit2[1];
    }
}
