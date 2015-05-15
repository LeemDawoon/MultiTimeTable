package app.com.multitimetable.android.multitimetable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import app.com.multitimetable.android.multitimetable.data.MTTContract;


public class ScheduleInsertDialogFragment extends android.support.v4.app.DialogFragment {
    private View mDialogView;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        mDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_schedule_insert_dialog, null);

        builder.setTitle(R.string.insert_schedule)
                .setView(mDialogView)
                // Add action buttons
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String scheduleName = ((EditText) mDialogView.findViewById(R.id.newScheduleName)).getText().toString();

                        if (Utility.validate(scheduleName)) {
                            ContentValues scheduleValues = new ContentValues();
                            scheduleValues.put(MTTContract.ScheduleEntry.COLUMN_NAME, scheduleName);
                            scheduleValues.put(MTTContract.ScheduleEntry.COLUMN_IS_OFF, 0);

                            Uri insertedUri = getActivity().getContentResolver().insert(
                                    MTTContract.ScheduleEntry.CONTENT_URI,
                                    scheduleValues
                            );

                            Toast.makeText(getActivity(), "Insert ID :" + ContentUris.parseId(insertedUri), Toast.LENGTH_LONG).show();
                        }


                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ScheduleInsertDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();


    }



}
