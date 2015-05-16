package app.com.multitimetable.android.multitimetable;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wable on 15. 5. 17..
 */
public class SelectScheduleAdapter extends CursorAdapter  {
    public SelectScheduleAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public static class ViewHolder {
        public final TextView mScheduleTitleView;
//        public final CheckBox mScheduleisOffCheckBox;

        public ViewHolder(View view) {
            mScheduleTitleView = (TextView) view.findViewById(android.R.id.text1);
//            mScheduleTitleView = (TextView) view.findViewById(R.id.scheduleName);
//            mScheduleisOffCheckBox = (CheckBox) view.findViewById(R.id.isOffCheckBox);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_activated_1, viewGroup, false);
//        View view = LayoutInflater.from(context).inflate(R.layout.list_item_schedule, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.mScheduleTitleView.setText(cursor.getString(SelectScheduleFragment.COL_SCHEDULE_NAME));
    }
}
