package app.com.multitimetable.android.multitimetable;



import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import app.com.multitimetable.android.multitimetable.data.MTTContract;


public class SubjectInsertActivity extends ActionBarActivity {

    public final static String SELECTED_SCHEDULE_ID="selected_schedule_id";
    public final static String SECTION_NUMBER="section_number";

//    public static MainActivity mainActivity;

    public class ViewHolderForTime {
        public Spinner day;
        public EditText starTime;
        public EditText endTime;
        public EditText place;
    }

//    public ViewHolderForTime[] mViewHoldersForTime;
    public ArrayList<ViewHolderForTime> mViewHoldersForTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_insert);

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

        /*ImageButton makeNewFormBtn = (ImageButton) this.findViewById(R.id.makeNewFormBtn);
        makeNewFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout timeFormLinearLayout = (LinearLayout) SubjectInsertActivity.this.findViewById(R.id.timeFormLinearLayout);
                Spinner day = new Spinner(SubjectInsertActivity.this);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SubjectInsertActivity.this, android.R.layout.simple_spinner_item, new ArrayList(R.array.day_array));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                day.setAdapter(adapter);
                timeFormLinearLayout.addView(day);

            }
        });*/

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.action_bar_for_edit);
        Button cancelBtn = (Button) actionBar.getCustomView().findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SubjectInsertActivity.this, "ddddd",Toast.LENGTH_LONG).show();
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
                    subjectValues.put(MTTContract.SubjectEntry.COLUMN_SCHEDULE_KEY, getIntent().getExtras().getInt(SELECTED_SCHEDULE_ID));

                    Uri insertedSubjectUri = getContentResolver().insert(
                            MTTContract.SubjectEntry.CONTENT_URI,
                            subjectValues
                    );

                    Toast.makeText(getBaseContext(), "Insert ID :" + ContentUris.parseId(insertedSubjectUri), Toast.LENGTH_LONG).show();

                    for(int i=0; i< mViewHoldersForTime.size(); i++){
                        int day = mViewHoldersForTime.get(i).day.getSelectedItemPosition();
                        int startTime = Utility.getFormattedTimeForDB(mViewHoldersForTime.get(i).starTime.getText().toString());
                        int endTime = Utility.getFormattedTimeForDB(mViewHoldersForTime.get(i).endTime.getText().toString());
                        String place = mViewHoldersForTime.get(i).place.getText().toString();

                        if(day != 0 && startTime != 0 && endTime !=0 ) {
                            ContentValues timeValues = new ContentValues();
                            timeValues.put(MTTContract.TimeEntry.COLUMN_SUBJECT_KEY, ContentUris.parseId(insertedSubjectUri));
                            timeValues.put(MTTContract.TimeEntry.COLUMN_DAY, day);
                            timeValues.put(MTTContract.TimeEntry.COLUMN_START_TIME, startTime);
                            timeValues.put(MTTContract.TimeEntry.COLUMN_END_TIME, endTime);
                            timeValues.put(MTTContract.TimeEntry.COLUMN_PLACE, place);
                            Uri insertedSubjectTimeUri = getContentResolver().insert(
                                    MTTContract.TimeEntry.CONTENT_URI,
                                    timeValues
                            );
                            Toast.makeText(getBaseContext(), "Insert ID :" + ContentUris.parseId(insertedSubjectTimeUri), Toast.LENGTH_LONG).show();
                        }else {
                            break;
                        }
                    }
                }else {
                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(SubjectInsertActivity.this);

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("과목 이름, 요일, 시작시간, 종료시간 데이터는 과목 추가를 위한 필수 데이터 입니다.");
//                            .setTitle(R.string.dialog_title);

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                Intent intent = new Intent();
                intent.putExtra(SECTION_NUMBER,getIntent().getExtras().getInt(SECTION_NUMBER));
                setResult(RESULT_OK,intent);
                finish();

            }
        });

    }



    private View.OnClickListener timeviewOnclickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            new TimePickerDialog(SubjectInsertActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    EditText parentText = (EditText)v;
                    String msg = String.format("%02d:%02d", hourOfDay, minute);
                    parentText.setText(msg);
                }
            }, 9, 0, false).show();
        }
    };


}
