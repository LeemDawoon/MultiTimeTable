package app.com.multitimetable.android.multitimetable;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import app.com.multitimetable.android.multitimetable.data.MTTContract;


public class SubjectUpdateActivity extends ActionBarActivity {
    public static final String ARG_SUBJECT_ID = "subject_id";

    private static final String[] SUBJECT_COLUMNS = {
            MTTContract.SubjectEntry.COLUMN_NAME,
            MTTContract.SubjectEntry.COLUMN_PROFESSOR,
            MTTContract.SubjectEntry.COLUMN_COLOR
    };
    static final int COL_NAME=0;
    static final int COL_PROCESSOR=1;
    static final int COL_COLOR=2;

    private static final String[] TIME_COLUMNS = {
            MTTContract.TimeEntry._ID,
            MTTContract.TimeEntry.COLUMN_DAY,
            MTTContract.TimeEntry.COLUMN_START_TIME,
            MTTContract.TimeEntry.COLUMN_END_TIME,
            MTTContract.TimeEntry.COLUMN_PLACE
    };
    static final int COL_TIME_ID=0;
    static final int COL_TIME_DAY=1;
    static final int COL_TIME_START_TIME=2;
    static final int COL_TIME_END_TIME=3;
    static final int COL_TIME_PLACE=4;


    public class ViewHolderForTime {
        public Spinner day;
        public EditText starTime;
        public EditText endTime;
        public EditText place;
    }

    public ArrayList<ViewHolderForTime> mViewHoldersForTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_insert);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.action_bar_for_edit);
        Button cancelBtn = (Button) actionBar.getCustomView().findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Button okBtn = (Button) actionBar.getCustomView().findViewById(R.id.btnOK);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newSubjectName = ((EditText)findViewById(R.id.subjectName)).getText().toString();
                String professorName = ((EditText)findViewById(R.id.professorName)).getText().toString();

                int day0 = mViewHoldersForTime.get(0).day.getSelectedItemPosition();
                int startTime0 = Utility.getFormattedTimeForDB(mViewHoldersForTime.get(0).starTime.getText().toString());
                int endTime0 = Utility.getFormattedTimeForDB(mViewHoldersForTime.get(0).endTime.getText().toString());

                if (newSubjectName != null && newSubjectName != "" && day0!=0 && startTime0!=0 && endTime0!=0) {
                    ContentValues subjectValues = new ContentValues();
                    subjectValues.put(MTTContract.SubjectEntry.COLUMN_NAME, newSubjectName);
                    subjectValues.put(MTTContract.SubjectEntry.COLUMN_PROFESSOR, professorName);

                    int count = getContentResolver().update(
                            MTTContract.SubjectEntry.CONTENT_URI,
                            subjectValues,
                            MTTContract.SubjectEntry.TABLE_NAME+"."+MTTContract.SubjectEntry._ID+" = ? ",
                            new String[]{Integer.toString(getIntent().getIntExtra(ARG_SUBJECT_ID,0))}
                    );

//                    Toast.makeText(getBaseContext(), "Subject update count :" + count, Toast.LENGTH_LONG).show();

                    for(int i=0; i< mViewHoldersForTime.size(); i++){
                        int day = mViewHoldersForTime.get(i).day.getSelectedItemPosition();
                        int startTime = Utility.getFormattedTimeForDB(mViewHoldersForTime.get(i).starTime.getText().toString());
                        int endTime = Utility.getFormattedTimeForDB(mViewHoldersForTime.get(i).endTime.getText().toString());
                        String place = mViewHoldersForTime.get(i).place.getText().toString();

                        if(day != 0 && startTime != 0 && endTime !=0 ) {
                            ContentValues timeValues = new ContentValues();
                            timeValues.put(MTTContract.TimeEntry.COLUMN_DAY, day);
                            timeValues.put(MTTContract.TimeEntry.COLUMN_START_TIME, startTime);
                            timeValues.put(MTTContract.TimeEntry.COLUMN_END_TIME, endTime);
                            timeValues.put(MTTContract.TimeEntry.COLUMN_PLACE, place);
                            int updateCount = getContentResolver().update(
                                    MTTContract.TimeEntry.CONTENT_URI,
                                    timeValues,
                                    MTTContract.TimeEntry.TABLE_NAME+"."+MTTContract.TimeEntry._ID+" = ? ",
                                    new String[]{Integer.toString((int)mViewHoldersForTime.get(i).day.getTag())}
                            );
//                            Toast.makeText(getBaseContext(), "Time update ID :" + updateCount, Toast.LENGTH_LONG).show();
                        }else {
                            break;
                        }
                    }
                }else {
                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(SubjectUpdateActivity.this);

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("과목 이름, 요일, 시작시간, 종료시간 데이터는 과목 추가를 위한 필수 데이터 입니다.");
//                            .setTitle(R.string.dialog_title);

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                Intent intent = new Intent(SubjectUpdateActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });


        mViewHoldersForTime = new ArrayList<ViewHolderForTime>();

        ViewHolderForTime viewHolderForTime = new ViewHolderForTime();
        viewHolderForTime.day = (Spinner) this.findViewById(R.id.daySpinner);
        viewHolderForTime.starTime = (EditText)this.findViewById(R.id.startTime);
        viewHolderForTime.starTime.setOnClickListener(timeviewOnclickListener);
        viewHolderForTime.endTime = (EditText)this.findViewById(R.id.endTime);
        viewHolderForTime.endTime.setOnClickListener(timeviewOnclickListener);
        viewHolderForTime.place = (EditText)this.findViewById(R.id.classroomName);
        mViewHoldersForTime.add(viewHolderForTime);

        ViewHolderForTime viewHolderForTime1 = new ViewHolderForTime();
        viewHolderForTime1.day = (Spinner) this.findViewById(R.id.daySpinner1);
        viewHolderForTime1.starTime = (EditText)this.findViewById(R.id.startTime1);
        viewHolderForTime1.starTime.setOnClickListener(timeviewOnclickListener);
        viewHolderForTime1.endTime = (EditText)this.findViewById(R.id.endTime1);
        viewHolderForTime1.endTime.setOnClickListener(timeviewOnclickListener);
        viewHolderForTime1.place = (EditText)this.findViewById(R.id.classroomName1);
        mViewHoldersForTime.add(viewHolderForTime1);

        setData((LinearLayout)findViewById(R.id.activityAddSubjectLayout));
    }


    private void setData(ViewGroup layout){
        Uri subjectUri = MTTContract.SubjectEntry.buildSubjectWithSubjectIdUri(getIntent().getIntExtra(ARG_SUBJECT_ID,0));
        Cursor subjectCursor = this.getContentResolver().query(
                subjectUri,
                SUBJECT_COLUMNS,
                null,
                null,
                null);

        subjectCursor.moveToFirst();

        EditText subjectName = (EditText)layout.findViewById(R.id.subjectName);
        subjectName.setText(subjectCursor.getString(COL_NAME));

        EditText professorName = (EditText)layout.findViewById(R.id.professorName);
        professorName.setText(subjectCursor.getString(COL_PROCESSOR));



        Cursor timeCursor = this.getContentResolver().query(
                MTTContract.TimeEntry.CONTENT_URI,
                TIME_COLUMNS,
                MTTContract.TimeEntry.TABLE_NAME+"."+ MTTContract.TimeEntry.COLUMN_SUBJECT_KEY+" = ? ",
                new String[]{Integer.toString(getIntent().getIntExtra(ARG_SUBJECT_ID, 0))},
                MTTContract.TimeEntry.COLUMN_DAY+" ASC");

        int i = 0;
        while(timeCursor.moveToNext()) {
            mViewHoldersForTime.get(i).day.setSelection(timeCursor.getInt(COL_TIME_DAY));
            mViewHoldersForTime.get(i).day.setTag(timeCursor.getInt(COL_TIME_ID));
            mViewHoldersForTime.get(i).starTime.setText(Utility.getFormattedTimeForView(timeCursor.getInt(COL_TIME_START_TIME)));
            mViewHoldersForTime.get(i).endTime.setText(Utility.getFormattedTimeForView(timeCursor.getInt(COL_TIME_END_TIME)));
            mViewHoldersForTime.get(i).place.setText(timeCursor.getString(COL_TIME_PLACE));
            i++;
        }





    }


    private View.OnClickListener timeviewOnclickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            new TimePickerDialog(SubjectUpdateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    EditText parentText = (EditText)v;
                    String msg = String.format("%02d:%02d", hourOfDay, minute);
                    parentText.setText(msg);
                }
            }, 9, 0, false).show();
        }
    };
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subject_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
