package app.com.multitimetable.android.multitimetable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import app.com.multitimetable.android.multitimetable.data.MTTContract;


public class AddSubjectActivity extends Activity {

    private AddSubjectActivityCallbacks mCallback = new AddSubjectActivityCallbacks() {
        @Override
        public void onAddSubjectComplete(int drawerPosition) {

        }
    };

    public interface AddSubjectActivityCallbacks {
        public void onAddSubjectComplete(int drawerPosition);
    }

    public final static String SELECTED_SCHEDULE_ID="selected_schedule_id";
    public final static String SECTION_NUMBER="section_number";

    public class ViewHolderForTime {
        public Spinner day;
        public EditText starTime;
        public EditText endTime;
        public EditText place;
    }

    public ViewHolderForTime[] mViewHoldersForTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        int a =  getIntent().getExtras().getInt(SELECTED_SCHEDULE_ID);
        Toast.makeText(this, a+" ", Toast.LENGTH_LONG).show();

        mViewHoldersForTime = new ViewHolderForTime[5];

        mViewHoldersForTime[0] = new ViewHolderForTime();
        mViewHoldersForTime[0].day = (Spinner) this.findViewById(R.id.daySpinner0);
        mViewHoldersForTime[0].starTime = (EditText)this.findViewById(R.id.startTime0);
        mViewHoldersForTime[0].starTime.setOnClickListener(timeviewOnclickListener);
        mViewHoldersForTime[0].endTime = (EditText)this.findViewById(R.id.endTime0);
        mViewHoldersForTime[0].endTime.setOnClickListener(timeviewOnclickListener);
        mViewHoldersForTime[0].place = (EditText)this.findViewById(R.id.newClassroomName0);

        mViewHoldersForTime[1] = new ViewHolderForTime();
        mViewHoldersForTime[1].day = (Spinner) this.findViewById(R.id.daySpinner1);
        mViewHoldersForTime[1].starTime = (EditText)this.findViewById(R.id.startTime1);
        mViewHoldersForTime[1].starTime.setOnClickListener(timeviewOnclickListener);
        mViewHoldersForTime[1].endTime = (EditText)this.findViewById(R.id.endTime1);
        mViewHoldersForTime[1].endTime.setOnClickListener(timeviewOnclickListener);
        mViewHoldersForTime[1].place = (EditText)this.findViewById(R.id.newClassroomName1);

        mViewHoldersForTime[2] = new ViewHolderForTime();
        mViewHoldersForTime[2].day = (Spinner) this.findViewById(R.id.daySpinner2);
        mViewHoldersForTime[2].starTime = (EditText)this.findViewById(R.id.startTime2);
        mViewHoldersForTime[2].starTime.setOnClickListener(timeviewOnclickListener);
        mViewHoldersForTime[2].endTime = (EditText)this.findViewById(R.id.endTime2);
        mViewHoldersForTime[2].endTime.setOnClickListener(timeviewOnclickListener);
        mViewHoldersForTime[2].place = (EditText)this.findViewById(R.id.newClassroomName2);

        mViewHoldersForTime[3] = new ViewHolderForTime();
        mViewHoldersForTime[3].day = (Spinner) this.findViewById(R.id.daySpinner3);
        mViewHoldersForTime[3].starTime = (EditText)this.findViewById(R.id.startTime3);
        mViewHoldersForTime[3].starTime.setOnClickListener(timeviewOnclickListener);
        mViewHoldersForTime[3].endTime = (EditText)this.findViewById(R.id.endTime3);
        mViewHoldersForTime[3].endTime.setOnClickListener(timeviewOnclickListener);
        mViewHoldersForTime[3].place = (EditText)this.findViewById(R.id.newClassroomName3);

        mViewHoldersForTime[4] = new ViewHolderForTime();
        mViewHoldersForTime[4].day = (Spinner) this.findViewById(R.id.daySpinner4);
        mViewHoldersForTime[4].starTime = (EditText)this.findViewById(R.id.startTime4);
        mViewHoldersForTime[4].starTime.setOnClickListener(timeviewOnclickListener);
        mViewHoldersForTime[4].endTime = (EditText)this.findViewById(R.id.endTime4);
        mViewHoldersForTime[4].endTime.setOnClickListener(timeviewOnclickListener);
        mViewHoldersForTime[4].place = (EditText)this.findViewById(R.id.newClassroomName4);


        // cancel Button
        Button cancelBtn = (Button) this.findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // OK Button
        Button okButton = (Button) this.findViewById(R.id.btnOK);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newSubjectName = ((EditText)findViewById(R.id.newSubjectName)).getText().toString();
                String professorName = ((EditText)findViewById(R.id.professorName)).getText().toString();

                int day0 = mViewHoldersForTime[0].day.getSelectedItemPosition();
                int startTime0 = Utility.getFormattedTimeForDB(mViewHoldersForTime[0].starTime.getText().toString());
                int endTime0 = Utility.getFormattedTimeForDB(mViewHoldersForTime[0].endTime.getText().toString());

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

                    for(int i=0; i< mViewHoldersForTime.length; i++){
                        int day = mViewHoldersForTime[i].day.getSelectedItemPosition();
                        int startTime = Utility.getFormattedTimeForDB(mViewHoldersForTime[i].starTime.getText().toString());
                        int endTime = Utility.getFormattedTimeForDB(mViewHoldersForTime[i].endTime.getText().toString());
                        String place = mViewHoldersForTime[i].place.getText().toString();

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddSubjectActivity.this);

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("과목 이름, 요일, 시작시간, 종료시간 데이터는 과목 추가를 위한 필수 데이터 입니다.");
//                            .setTitle(R.string.dialog_title);

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                mCallback.onAddSubjectComplete(getIntent().getExtras().getInt(SECTION_NUMBER)-1);
                onBackPressed();





            }
        });


    }

    private View.OnClickListener timeviewOnclickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            new TimePickerDialog(AddSubjectActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    EditText parentText = (EditText)v;
                    String msg = String.format("%02d:%02d", hourOfDay, minute);
                    parentText.setText(msg);
                }
            }, 9, 0, false).show();
        }
    };


    /*
    public View makeNewForm(){
        placeAndTimeFormIndex++;
        View dayPicker = new Spinner(this, )

    }
    */


}
