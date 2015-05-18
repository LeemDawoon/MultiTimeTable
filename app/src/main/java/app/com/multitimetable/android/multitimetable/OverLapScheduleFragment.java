package app.com.multitimetable.android.multitimetable;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ShareActionProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.multitimetable.android.multitimetable.data.MTTContract;

import static android.widget.GridLayout.spec;


public class OverLapScheduleFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SCHEDULE_ID_ARRAY="schedule_id_array";




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


    public interface TimeTableFragmentCallbacks {
        public void onSubjectInsertComplete(int sectionNumber);
        public void onSubjectDeleteComplete(int sectionNumber);
    }

    private  ShareActionProvider mShareActionProvider;
//    private int mSelectedScheduleID;
//    private String mSelectedScheduleName;
//    private int mSectionNumber;             // drawer 에서 선택된 섹션변호. 시간표 화면 새로고침 할때 필요함.

    public ArrayList<Integer> mScheduleIdArr;
    private View mRootView;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static OverLapScheduleFragment newInstance(ArrayList<Integer> scheduleIdArr) {
        OverLapScheduleFragment fragment = new OverLapScheduleFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_SCHEDULE_ID_ARRAY, scheduleIdArr);
        fragment.setArguments(args);
        return fragment;
    }

    public OverLapScheduleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        mScheduleIdArr = getArguments().getIntegerArrayList(ARG_SCHEDULE_ID_ARRAY);
//        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.timetablefragment, menu);

//        MenuItem menuItem = menu.findItem(R.id.action_share);
//        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//        mShareActionProvider.setShareIntent(createShareTimeTableIntent(mSelectedScheduleID));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_new) {
            return true;
        }
        if (id == R.id.action_compare) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View rootView;

        mRootView = inflater.inflate(R.layout.fragment_compare_schedule, container, false);
        GridLayout backgroundGridLayout = (GridLayout)mRootView.findViewById(R.id.backgroundGridLayout);

        FrameLayout compareScheduleFragmentFrameLayout = (FrameLayout) mRootView.findViewById(R.id.compareScheduleFragmentFrameLayout);

        setTimeTableBackground(backgroundGridLayout);
        setTimeTableMain(compareScheduleFragmentFrameLayout, mScheduleIdArr);


        return mRootView;
    }


    public void refreshView(){
//        GridLayout backgroundGridLayout = (GridLayout)mRootView.findViewById(R.id.backgroundGridLayout);

        FrameLayout compareScheduleFragmentFrameLayout = (FrameLayout) mRootView.findViewById(R.id.compareScheduleFragmentFrameLayout);

//        setTimeTableBackground(backgroundGridLayout);
        setTimeTableMain(compareScheduleFragmentFrameLayout, mScheduleIdArr);

    }





    //----- Helper Method

    /* 시간표 배경 그리기 */
    private  void setTimeTableBackground(GridLayout gridLayout ){
        int columnCount = 8;
        int rowCount = 24;

        gridLayout.setColumnCount(8);

        TextView[] day = new TextView[columnCount];
        for (int i =0, length=day.length; i<length; i++) {
            day[i] = new TextView(getActivity());
            day[i].setWidth(Utility.getDisplayWidth() / columnCount);
            day[i].setHeight(30);
            day[i].setText(Utility.getDayAbbrResource(i));
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
//                timeColumn.setHeight(Utility.getDisplayHeight()/(rowCount+1));
                timeColumn.setHeight(Utility.getHourCellHeight(rowCount+1));

                timeColumn.setBackgroundDrawable(gd);

                if(j==0) timeColumn.setText(i + " ");

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
    private  void setTimeTableMain(FrameLayout rootLayout, ArrayList<Integer> scheduleIdArr ){

        int columnCount = 8;
        int rowCount = 24;
        int startTime = 0;
        int endTime = 23;
        int cellWidth = Utility.getDisplayWidth() / columnCount;
        int cellHeight = Utility.getHourCellHeight(rowCount+1);

        for(int i=0, length = scheduleIdArr.size(); i<length ; i++) {
            GridLayout gridLayout = new GridLayout(getActivity());
            gridLayout.setColumnCount(columnCount);

            TextView[] day = new TextView[columnCount];
            for (int j =0, dayLength=day.length; j<dayLength; j++) {
                day[j] = new TextView(getActivity());
                day[j].setWidth(cellWidth);
                day[j].setHeight(30);
                gridLayout.addView(day[j],j);
            }


            TextView timeColumn = new TextView(getActivity());
            timeColumn.setWidth(cellWidth);
            timeColumn.setHeight(cellHeight*rowCount);
            timeColumn.setText(" ");
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams();
            gridLayoutParam.columnSpec = spec(0);
            gridLayoutParam.rowSpec = spec(1);
            gridLayout.addView(timeColumn,gridLayoutParam);

            Cursor cursor=null;
            try {
                Uri subjectAndTimeByScheduleUri = MTTContract.SubjectEntry.buildSubjectTimeWithScheduleIdUri(scheduleIdArr.get(i));
                String sortOrder = MTTContract.TimeEntry.COLUMN_DAY + " ASC, " +
                        MTTContract.TimeEntry.COLUMN_START_TIME + " ASC";
                cursor = getActivity().getContentResolver().query(
                        subjectAndTimeByScheduleUri,
                        TIMETABLE_COLUMNS,
                        null,
                        null,
                        sortOrder);

                LinearLayout[] colLinearLayout = new LinearLayout[7];
                for (int j=0, colLength=colLinearLayout.length; j<colLength; j++){
                    colLinearLayout[j]= new LinearLayout(getActivity());
                    colLinearLayout[j].setOrientation(LinearLayout.VERTICAL);
                }

                int previousCol = 0;
                float previousEnd = 0.0f;
                while(cursor.moveToNext()){
                    int col = cursor.getInt(COL_TIME_DAY);
                    float start = (float)cursor.getInt(COL_TIME_START_TIME);
                    float end = (float)cursor.getInt(COL_TIME_END_TIME);
                    float rowSpan = (end-start)/60.0f;// + ((end-start)%60)/60;;


                    TextView subject = new TextView(getActivity());
                    subject.setWidth(cellWidth);
                    subject.setHeight((int)(cellHeight*rowSpan));
                    subject.setBackgroundResource(R.color.subject_10);


                    // 이전 과목 데이터가 지금 과목 데이터와 다른 요일임.
                    if(previousCol != col && previousCol !=0){
                        GridLayout.LayoutParams subjectGridLayoutParam = new GridLayout.LayoutParams();
                        subjectGridLayoutParam.columnSpec = spec(previousCol);
                        subjectGridLayoutParam.rowSpec = spec(1);
                        gridLayout.addView(colLinearLayout[previousCol-1],subjectGridLayoutParam);
                        previousEnd = 0.0f;

                    }

                    if(previousEnd!=start) {
                        TextView blank = new TextView(getActivity());
                        blank.setWidth(cellWidth);
                        blank.setHeight((int) (cellHeight * (start - previousEnd) / 60.0f));
                        colLinearLayout[col-1].addView(blank);
                    }
                    colLinearLayout[col-1].addView(subject);

                    previousCol = col;
                    previousEnd = end;


                    if (cursor.isLast()){
                        GridLayout.LayoutParams subjectGridLayoutParam = new GridLayout.LayoutParams();
                        subjectGridLayoutParam.columnSpec = spec(col);
                        subjectGridLayoutParam.rowSpec = spec(1);
                        gridLayout.addView(colLinearLayout[col-1],subjectGridLayoutParam);
                    }

                }//end of while loop

                LinearLayout.LayoutParams scrollViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                ScrollView scrollView = new ScrollView(getActivity());
//                scrollView.addView(gridLayout,scrollViewParams);

                rootLayout.addView(gridLayout,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            }
            finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        }//end of for loop
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


//        shareIntent.putExtra(Intent.EXTRA_TEXT, "schedule name" + getArguments().getString(ARG_SELECTED_SCHEDULE_NAME));


        // 파일 첨부
//        i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/mic_rec3.wav"));

        return shareIntent;
    }

}