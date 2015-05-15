package app.com.multitimetable.android.multitimetable;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import app.com.multitimetable.android.multitimetable.data.MTTContract;

import static android.widget.GridLayout.*;


public class TimeTableFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SELECTED_SCHEDULE_ID = "selected_schedule_id";
    private static final String ARG_SELECTED_SCHEDULE_NAME = "selected_schedule_name";


    private static final String[] TIMETABLE_COLUMNS = {
        MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry._ID,
        MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry.COLUMN_DAY,
        MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry.COLUMN_START_TIME,
        MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry.COLUMN_END_TIME,
        MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry.COLUMN_PLACE,
        MTTContract.SubjectEntry.TABLE_NAME + "." + MTTContract.SubjectEntry._ID,
        MTTContract.SubjectEntry.TABLE_NAME + "." + MTTContract.SubjectEntry.COLUMN_NAME,
        MTTContract.SubjectEntry.TABLE_NAME + "." + MTTContract.SubjectEntry.COLUMN_PROFESSOR,
        MTTContract.SubjectEntry.TABLE_NAME + "." + MTTContract.SubjectEntry.COLUMN_COLOR,

    };
    static final int COL_TIME_ID=0;
    static final int COL_TIME_DAY=1;
    static final int COL_TIME_START_TIME=2;
    static final int COL_TIME_END_TIME=3;
    static final int COL_TIME_PLACE=4;
    static final int COL_SUBJECT_ID=5;
    static final int COL_SUBJECT_NAME=6;
    static final int COL_SUBJECT_PROFESSOR=7;
    static final int COL_SUBJECT_COLOR=8;



    private static final String[] TIMETABLE_LAYOUT_COLUMNS = {
            "MAX("+MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry.COLUMN_DAY+")",
            "MIN("+MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry.COLUMN_START_TIME+")",
            "MAX("+MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry.COLUMN_END_TIME+")"
    };
    static final int COL_LAST_DAY=0;
    static final int COL_EARLIEST_TIME=1;
    static final int COL_LATEST_TIME=2;



    private static final String[] SUBJECT_COLUMNS = {
        MTTContract.SubjectEntry.COLUMN_NAME,
        MTTContract.SubjectEntry.COLUMN_PROFESSOR,
        MTTContract.SubjectEntry.COLUMN_COLOR
    };
    static final int COL_NAME=0;
    static final int COL_PROCESSOR=1;
    static final int COL_COLOR=2;


    private static final String[] TIME_COLUMNS = {
        MTTContract.TimeEntry.COLUMN_DAY,
        MTTContract.TimeEntry.COLUMN_START_TIME,
        MTTContract.TimeEntry.COLUMN_END_TIME,
        MTTContract.TimeEntry.COLUMN_PLACE
    };
    static final int COL_DAY=0;
    static final int COL_START_TIME=1;
    static final int COL_END_TIME=2;
    static final int COL_PLACE=3;



//    public View rootView;
    private  ShareActionProvider mShareActionProvider;
    private int mSelectedScheduleID;
    private String mSelectedScheduleName;
    private int mSectionNumber;             // drawer 에서 선택된 섹션변호. 시간표 화면 새로고침 할때 필요함.

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TimeTableFragment newInstance(int sectionNumber, int selectedScheduleID, String selectedScheduleName) {
        TimeTableFragment fragment = new TimeTableFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt(ARG_SELECTED_SCHEDULE_ID, selectedScheduleID);
        args.putString(ARG_SELECTED_SCHEDULE_NAME, selectedScheduleName);
        fragment.setArguments(args);
        return fragment;
    }

    public TimeTableFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        mSelectedScheduleID = getArguments().getInt(ARG_SELECTED_SCHEDULE_ID);
        mSelectedScheduleName = getArguments().getString(ARG_SELECTED_SCHEDULE_NAME);
        mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.timetablefragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
//        if (mForecast != null) {
            mShareActionProvider.setShareIntent(createShareTimeTableIntent(mSelectedScheduleID));
//        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_new) {

            Intent intent = new Intent(getActivity(),AddSubjectActivity.class)
//                    .putExtra(AddSubjectActivity.SELECTED_SCHEDULE_ID,getArguments().getInt(ARG_SELECTED_SCHEDULE_ID))
//                    .putExtra(AddSubjectActivity.SECTION_NUMBER, getArguments().getInt(ARG_SECTION_NUMBER));
                    .putExtra(AddSubjectActivity.SELECTED_SCHEDULE_ID, mSelectedScheduleID)
                    .putExtra(AddSubjectActivity.SECTION_NUMBER, mSectionNumber);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_compare) {
            Intent intent = new Intent(getActivity(), SelectScheduleActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        // 앱에 시간표 데이터가 없다면, 시간표 생성화면을 보여준다.
        if (MainActivity.mTimeTableDrawerFragment.mTimeTableDrawerAdapter.getCount()==0) {
            rootView = inflater.inflate(R.layout.fragment_create_first_time_table, container, false);

            final EditText editText = (EditText)rootView.findViewById(R.id.text_schedule_name);
            Button btnCreateSchedule = (Button)rootView.findViewById(R.id.btn_create_schedule);

            btnCreateSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String scheduleName =  editText.getText().toString();

                    if (scheduleName != null && scheduleName != "") {
                        ContentValues scheduleValues = new ContentValues();
                        scheduleValues.put(MTTContract.ScheduleEntry.COLUMN_NAME, scheduleName);
                        scheduleValues.put(MTTContract.ScheduleEntry.COLUMN_IS_OFF, 0);

                        Uri insertedUri = getActivity().getContentResolver().insert(
                                MTTContract.ScheduleEntry.CONTENT_URI,
                                scheduleValues
                        );
                        Toast.makeText(getActivity(),"Insert ID :" + ContentUris.parseId(insertedUri), Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
        // 앱에 시간표 데이터가 있다면,시간표를 보여준다.
        // 데이터 가져올때 요일별, 시간별로, 정렬해서 가져오기
        else {
            rootView = inflater.inflate(R.layout.fragment_time_table, container, false);
//            int scheduleID = getArguments().getInt(ARG_SELECTED_SCHEDULE_ID);
            if(mSelectedScheduleID != 0){
                GridLayout backgroundGridLayout = (GridLayout)rootView.findViewById(R.id.backgroundGridLayout);
                GridLayout timetableGridLayout = (GridLayout)rootView.findViewById(R.id.timetableGridLayout);

                setTimeTableBackground(backgroundGridLayout, mSelectedScheduleID);
                setTimeTableMain(timetableGridLayout, mSelectedScheduleID);

            }
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }



    //----- Helper Method

    /* 시간표 배경 그리기 */
    private  void setTimeTableBackground(GridLayout gridLayout, int scheduleID ){
        Uri subjectAndTimeByScheduleUri = MTTContract.SubjectEntry.buildSubjectTimeWithScheduleIdUri(scheduleID);

        Cursor cursorForLayout = getActivity().getContentResolver().query(
                subjectAndTimeByScheduleUri,
                TIMETABLE_LAYOUT_COLUMNS,
                null,
                null,
                null);

        cursorForLayout.moveToFirst();
        int startTime = cursorForLayout.getInt(COL_EARLIEST_TIME)/60;
        int endTime = cursorForLayout.getInt(COL_LATEST_TIME)/60;
        int columnCount = cursorForLayout.getInt(COL_LAST_DAY)+1;
        int rowCount = endTime-startTime+1;
        cursorForLayout.close();

        gridLayout.setColumnCount(columnCount);

        int [] dayArray = new int[8];
        dayArray[0] = R.string.time_and_day;
        dayArray[1] = R.string.time_mon;
        dayArray[2] = R.string.time_tue;
        dayArray[3] = R.string.time_wed;
        dayArray[4] = R.string.time_thu;
        dayArray[5] = R.string.time_fri;
        dayArray[6] = R.string.time_sat;
        dayArray[7] = R.string.time_sun;


        TextView[] day = new TextView[columnCount];
        for (int i =0, length=day.length; i<length; i++) {
            day[i] = new TextView(getActivity());
            day[i].setWidth(Utility.getDisplayWidth() / columnCount);
            day[i].setHeight(30);
            day[i].setText(dayArray[i]);
            day[i].setGravity(Gravity.CENTER);
            day[i].setTextSize(20);
            day[i].setBackgroundColor(R.color.abc_primary_text_material_light);
            gridLayout.addView(day[i],i);
        }

        /*TextView tv = new TextView(getActivity());
        tv.setWidth(Utility.getDisplayWidth() / columnCount);
        tv.setBackgroundColor(R.color.abc_primary_text_material_light);
        tv.setTextSize(20);
        gridLayout.addView(tv,0);
*/

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.TRANSPARENT); // Changes this drawbale to use a single color instead of a gradient
        gd.setCornerRadius(1);
        gd.setStroke(1, 0xFF000000);


        for (int i=0; i<rowCount; i++) {
            for(int j=0; j<columnCount; j++) {
                TextView timeColumn = new TextView(getActivity());
                timeColumn.setWidth(Utility.getDisplayWidth() / columnCount);
                timeColumn.setHeight(Utility.getDisplayHeight()/(rowCount+1));
//            timeColumn.setBackgroundColor(R.color.abc_primary_text_material_light);

                timeColumn.setBackgroundDrawable(gd);

                if(j==0) timeColumn.setText(startTime + i + " ");

                timeColumn.setGravity(Gravity.RIGHT);
                timeColumn.setTextSize(20);
                GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams();
                gridLayoutParam.columnSpec = spec(j);
                gridLayoutParam.rowSpec = spec(i+1);
                gridLayout.addView(timeColumn,gridLayoutParam);
            }

        }

    }

    /* 시간표 그리기 */
    private  void setTimeTableMain(GridLayout gridLayout, int scheduleID ){
        Uri subjectAndTimeByScheduleUri = MTTContract.SubjectEntry.buildSubjectTimeWithScheduleIdUri(scheduleID);
        Cursor cursorForLayout = getActivity().getContentResolver().query(
                subjectAndTimeByScheduleUri,
                TIMETABLE_LAYOUT_COLUMNS,
                null,
                null,
                null);

        cursorForLayout.moveToFirst();
        int startTime = cursorForLayout.getInt(COL_EARLIEST_TIME)/60;
        int endTime = cursorForLayout.getInt(COL_LATEST_TIME)/60;
        int columnCount = cursorForLayout.getInt(COL_LAST_DAY)+1;
        int rowCount = endTime-startTime+1;
        int cellWidth = Utility.getDisplayWidth() / columnCount;
        int cellHeight = Utility.getDisplayHeight()/(rowCount+1);


        cursorForLayout.close();

        gridLayout.setColumnCount(columnCount);


        TextView[] day = new TextView[columnCount];
        for (int i =0, length=day.length; i<length; i++) {
            day[i] = new TextView(getActivity());
            day[i].setWidth(cellWidth);
            day[i].setHeight(30);
            gridLayout.addView(day[i],i);
        }


        for (int i=0; i<rowCount; i++) {
            TextView timeColumn = new TextView(getActivity());
            timeColumn.setWidth(cellWidth);
            timeColumn.setHeight(cellHeight);
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams();
            gridLayoutParam.columnSpec = spec(0);

            gridLayoutParam.rowSpec = spec(i + 1);
            gridLayout.addView(timeColumn,gridLayoutParam);
        }

        String sortOrder = MTTContract.TimeEntry.COLUMN_DAY + " ASC, " +
                MTTContract.TimeEntry.COLUMN_START_TIME + " ASC";
        Cursor cursor = getActivity().getContentResolver().query(
                subjectAndTimeByScheduleUri,
                TIMETABLE_COLUMNS,
                null,
                null,
                sortOrder);
//
//        static final int COL_TIME_ID=0;
//        static final int COL_TIME_DAY=1;
//        static final int COL_TIME_START_TIME=2;
//        static final int COL_TIME_END_TIME=3;
//        static final int COL_TIME_PLACE=4;
//        static final int COL_SUBJECT_ID=5;
//        static final int COL_SUBJECT_NAME=6;
//        static final int COL_SUBJECT_PROFESSOR=7;
//        static final int COL_SUBJECT_COLOR=8;
        if (cursor.moveToFirst()) {
            int col0 = cursor.getInt(COL_TIME_DAY);
            int start0 = cursor.getInt(COL_TIME_START_TIME);
            int row0 = (start0/60)-startTime+1;
            int end0 = cursor.getInt(COL_TIME_END_TIME);
            int rowSpan0 = (end0-start0)/60 + (int)Math.ceil((end0-start0)%60/60);

            TextView subject0 = new TextView(getActivity());
            subject0.setWidth(cellWidth);
            subject0.setHeight(cellHeight*rowSpan0);
            subject0.setText(cursor.getString(COL_SUBJECT_NAME));
            subject0.setTextSize(30);
            subject0.setBackgroundColor(R.color.abc_background_cache_hint_selector_material_light);
            subject0.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), SubjectDetailActivity.class);
                    startActivity(intent);
                }
            });

            GridLayout.LayoutParams gridLayoutParam0 = new GridLayout.LayoutParams();
            gridLayoutParam0.columnSpec = spec(col0);
            gridLayoutParam0.rowSpec = spec(row0, rowSpan0);
            gridLayout.addView(subject0,gridLayoutParam0);

            while(cursor.moveToNext()){

                int col = cursor.getInt(COL_TIME_DAY);
                int start = cursor.getInt(COL_TIME_START_TIME);
                int end = cursor.getInt(COL_TIME_END_TIME);
                int row = (start/60)-startTime+1;
                int rowSpan = (end-start)/60;

                TextView subject = new TextView(getActivity());
                subject.setWidth(cellWidth);
                subject.setHeight(cellHeight*rowSpan);
                subject.setText(cursor.getString(COL_SUBJECT_NAME));
                subject.setTextSize(30);
                subject.setBackgroundColor(R.color.abc_background_cache_hint_selector_material_light);
                subject.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), SubjectDetailActivity.class);
                        startActivity(intent);
                    }
                });

                GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams();
                gridLayoutParam.columnSpec = spec(col);
                gridLayoutParam.rowSpec = spec(row,rowSpan);
                gridLayout.addView(subject,gridLayoutParam);

            }
        }






    }

    /* 공유기능 */
    /*
    * 데이턴 변경시,
    * if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
            새로 해줘야함...
    * */
    private Intent createShareTimeTableIntent(int scheduleID) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");

        /*Uri subjectAndTimeByScheduleUri = MTTContract.SubjectEntry.buildSubjectTimeWithScheduleIdUri(scheduleID);
        String sortOrder = MTTContract.TimeEntry.COLUMN_DAY + " ASC, " +
                MTTContract.TimeEntry.COLUMN_START_TIME + " ASC";
        Cursor cursor = getActivity().getContentResolver().query(
                subjectAndTimeByScheduleUri,
                TIMETABLE_COLUMNS,
                null,
                null,
                sortOrder);*/

        /* 과목 빼오고, 거에맡는 시간 들, 과목, 시간 요 순서로 넣야 겠음.. */

        Uri subjectByScheduleIdUri = MTTContract.SubjectEntry.buildSubjectWithScheduleIdUri(scheduleID);
        Cursor cursor = getActivity().getContentResolver().query(
            subjectByScheduleIdUri,
            SUBJECT_COLUMNS,
            null,
            null,
            null
        );
        StringBuffer contents = new StringBuffer();


        shareIntent.putExtra(Intent.EXTRA_TEXT, "schedule name" + getArguments().getString(ARG_SELECTED_SCHEDULE_NAME));


        // 파일 첨부
//        i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/mic_rec3.wav"));

        return shareIntent;
    }

}