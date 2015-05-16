package app.com.multitimetable.android.multitimetable;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity
        implements TimeTableDrawerFragment.NavigationDrawerCallbacks,
        TimeTableFragment.TimeTableFragmentCallbacks{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    public static TimeTableDrawerFragment mTimeTableDrawerFragment;
//    public static boolean isEmptySchedule = false;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private int mSelectedScheduleID;
    private String mSelectedScheduleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimeTableDrawerFragment = (TimeTableDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mTimeTableDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        onNavigationDrawerItemSelected(1);

        // 디바이스 너비 값 초기화
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Utility.setDisplayWidth(displayMetrics.widthPixels);


        /* 액션바 56dp값이 pixel로 재대로 변환되지 않음.. 일단 보류 . */
//        int actionBarPixel = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,R.dimen.abc_action_bar_default_height_material,displayMetrics);
//        Utility.setActionBarHeight(actionBarPixel);
        Utility.setDisplayHeight(displayMetrics.heightPixels);
    }

    /* TimeTableDrawerFragment 콜백
     * 시간표 선택시 호출 */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        setTitleAndSelectedScheduleID(position);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, TimeTableFragment.newInstance(position + 1, mSelectedScheduleID, mSelectedScheduleName))
                .commit();
    }

    /* TimeTableDrawerFragment 콜백
     * 시간표 롱클릭시 호출 */
    @Override
    public void onNavigationDrawerItemSelectedForUpdateOrDelete(int position) {
        setTitleAndSelectedScheduleID(position);
        DialogFragment scheduleUpdateOrDeleteOptionDialogFragment = new ScheduleUpdateOrDeleteOptionDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ScheduleUpdateOrDeleteOptionDialogFragment.ARG_SCHEDULE_ID, mSelectedScheduleID);
        args.putString(ScheduleUpdateOrDeleteOptionDialogFragment.ARG_SCHEDULE_NAME, mSelectedScheduleName);
        scheduleUpdateOrDeleteOptionDialogFragment.setArguments(args);
        scheduleUpdateOrDeleteOptionDialogFragment.show(this.getSupportFragmentManager(), "SCHEDULE_UPDATE_OR_DELETE_DIALOG_FRAGMENT");
    }

    public void onSectionAttached(int number) {
        setTitleAndSelectedScheduleID(number - 1);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mTimeTableDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /* 과목 추가 콜백 함수. TimeTableFragment를 reload 하기 위함. */
    @Override
    public void onSubjectInsertComplete(int sectionNumber) {
        onNavigationDrawerItemSelected(sectionNumber-1);
    }

    @Override
    public void onSubjectDeleteComplete(int sectionNumber) {
        onNavigationDrawerItemSelected(sectionNumber-1);
    }


    private void setTitleAndSelectedScheduleID(int index) {

        if (index != 0) --index; // header 위치값을 추가로 뺴준다.
        if (mTimeTableDrawerFragment != null && mTimeTableDrawerFragment.mTimeTableDrawerAdapter != null) {
            Cursor cursor = (Cursor) (mTimeTableDrawerFragment.mTimeTableDrawerAdapter.getItem(index));

            if (cursor != null && cursor.getCount() != 0) {
                mSelectedScheduleID = cursor.getInt(TimeTableDrawerFragment.COL_SCHEDULE_ID);
                mSelectedScheduleName = cursor.getString(TimeTableDrawerFragment.COL_SCHEDULE_NAME);
                mTitle = cursor.getString(TimeTableDrawerFragment.COL_SCHEDULE_NAME);
            }

        } else {
//            mSelectedScheduleID=1;
        }

    }


}



