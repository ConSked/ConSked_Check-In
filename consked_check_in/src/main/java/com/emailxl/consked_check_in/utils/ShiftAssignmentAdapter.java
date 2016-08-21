package com.emailxl.consked_check_in.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.emailxl.consked_check_in.R;
import com.emailxl.consked_check_in.internal_db.ShiftAssignmentInt;
import com.emailxl.consked_check_in.internal_db.WorkerHandler;
import com.emailxl.consked_check_in.internal_db.WorkerInt;

import java.util.List;

/**
 * @author ECG
 */

public class ShiftAssignmentAdapter extends ArrayAdapter<ShiftAssignmentInt> {
    private LayoutInflater mInflater;
    private List<ShiftAssignmentInt> mShiftAssignmentInts;
    private int mViewResourceId;
    private Context mContext;

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
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(mViewResourceId, null);

            holder = new ViewHolder();
            holder.tvWorkerName = (TextView) convertView.findViewById(R.id.workerName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ShiftAssignmentInt shiftAssignmentInt = mShiftAssignmentInts.get(position);

        int workerIdExt = shiftAssignmentInt.getWorkerIdExt();

        WorkerHandler dbw = new WorkerHandler(mContext);
        WorkerInt workerInt = dbw.getWorkerIdExt(workerIdExt);

        String name = workerInt.getFirstName() + " " + workerInt.getLastName();
        holder.tvWorkerName.setText(name);

        return convertView;
    }

    private static class ViewHolder {
        public TextView tvWorkerName;
    }
}
