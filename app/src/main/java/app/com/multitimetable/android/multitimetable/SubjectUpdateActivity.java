package app.com.multitimetable.android.multitimetable;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
            MTTContract.TimeEntry.COLUMN_DAY,
            MTTContract.TimeEntry.COLUMN_START_TIME,
            MTTContract.TimeEntry.COLUMN_END_TIME,
            MTTContract.TimeEntry.COLUMN_PLACE
    };
    static final int COL_DAY=0;
    static final int COL_START_TIME=1;
    static final int COL_END_TIME=2;
    static final int COL_PLACE=3;

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
                Intent intent = new Intent(SubjectUpdateActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

        setData((LinearLayout)findViewById(R.id.activityAddSubjectLayout));
    }


    private void setData(ViewGroup layout){
        Uri subjectUri = MTTContract.SubjectEntry.buildSubjectWithSubjectIdUri(getIntent().getIntExtra(ARG_SUBJECT_ID,0));
        Cursor cursor = this.getContentResolver().query(
                subjectUri,
                SUBJECT_COLUMNS,
                null,
                null,
                null);

        cursor.moveToFirst();

        EditText subjectName = (EditText)layout.findViewById(R.id.newSubjectName);
        subjectName.setText(cursor.getString(COL_NAME));

        EditText professorName = (EditText)layout.findViewById(R.id.professorName);
        professorName.setText(cursor.getString(COL_PROCESSOR));





    }
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
