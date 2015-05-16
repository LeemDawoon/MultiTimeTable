package app.com.multitimetable.android.multitimetable;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.com.multitimetable.android.multitimetable.data.MTTContract;


public class SelectScheduleActivity extends ActionBarActivity {
    public static final String LOG_TAG = TimeTableDrawerFragment.class.getSimpleName();

    public SelectScheduleAdapter mSelectScheduleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_schedule);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, SelectScheduleFragment.newInstance())
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if ( id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        if (id == R.id.action_accept) {

            SparseBooleanArray checkedItemPositions = SelectScheduleFragment.mFragment.mListView.getCheckedItemPositions();
            int count = SelectScheduleFragment.mFragment.mListView.getCount();
            ArrayList<Integer> scheduleIdArr = new ArrayList<Integer>();
            for (int i=0; i<count ; i++) {
                if (checkedItemPositions.get(i)) {
                    Cursor cursor = (Cursor) SelectScheduleFragment.mFragment.mListView.getItemAtPosition(i);
                    scheduleIdArr.add(cursor.getInt(SelectScheduleFragment.COL_SCHEDULE_ID));
                    Toast.makeText(this,"item position :" +i +"  tag :" +cursor.getString(SelectScheduleFragment.COL_SCHEDULE_NAME) ,Toast.LENGTH_SHORT).show();
                }
            }

            if (scheduleIdArr.size()>1) {
                Intent intent = new Intent(this, CompareScheduleActivity.class);
                intent.putIntegerArrayListExtra(CompareScheduleFragment.ARG_SCHEDULE_ID_ARRAY, scheduleIdArr);
                startActivity(intent);
            } else {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectScheduleActivity.this);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("시간표를 2개 이상 선택해 주세요! ")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("주의")
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
