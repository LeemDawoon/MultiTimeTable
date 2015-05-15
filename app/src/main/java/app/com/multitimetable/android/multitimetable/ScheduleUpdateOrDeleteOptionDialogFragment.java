package app.com.multitimetable.android.multitimetable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.com.multitimetable.android.multitimetable.data.MTTContract;


public class ScheduleUpdateOrDeleteOptionDialogFragment extends android.support.v4.app.DialogFragment {

    public static final String ARG_SCHEDULE_ID = "schedule_id";
    public static final String ARG_SCHEDULE_NAME = "schedule_name";

    private View mDialogView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        mDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_schedule_update_or_delete_option_dialog, null);

        Button updateScheduleOption = (Button) mDialogView.findViewById(R.id.updateScheduleOption);
        updateScheduleOption.setText("Update '"+getArguments().getString(ARG_SCHEDULE_NAME)+"'");
        updateScheduleOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment scheduleUpdateDialogFragment = new ScheduleUpdateDialogFragment();
                Bundle args = new Bundle();
                args.putInt(ScheduleUpdateDialogFragment.ARG_SCHEDULE_ID, getArguments().getInt(ARG_SCHEDULE_ID));
                args.putString(ScheduleUpdateDialogFragment.ARG_SCHEDULE_NAME, getArguments().getString(ARG_SCHEDULE_NAME));
                scheduleUpdateDialogFragment.setArguments(args);
                scheduleUpdateDialogFragment.show(getActivity().getSupportFragmentManager(),"SCHEDULE_UPDATE_DIALOG_FRAGMENT");
                ScheduleUpdateOrDeleteOptionDialogFragment.this.getDialog().cancel(); // 현재 다이알로그를 종료한다.
            }
        });

        Button deleteScheduleOption = (Button) mDialogView.findViewById(R.id.deleteScheduleOption);
        deleteScheduleOption.setText("Delete '"+getArguments().getString(ARG_SCHEDULE_NAME)+"'");
        deleteScheduleOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment scheduleDeleteDialogFragment = new ScheduleDeleteDialogFragment();
                Bundle args = new Bundle();
                args.putInt(ScheduleUpdateDialogFragment.ARG_SCHEDULE_ID, getArguments().getInt(ARG_SCHEDULE_ID));
                args.putString(ScheduleUpdateDialogFragment.ARG_SCHEDULE_NAME, getArguments().getString(ARG_SCHEDULE_NAME));
                scheduleDeleteDialogFragment.setArguments(args);
                scheduleDeleteDialogFragment.show(getActivity().getSupportFragmentManager(), "SCHEDULE_DELETE_DIALOG_FRAGMENT");
                ScheduleUpdateOrDeleteOptionDialogFragment.this.getDialog().cancel(); // 현재 다이알로그를 종료한다.
            }
        });


        builder.setView(mDialogView)
                // Add action buttons
                /*.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String scheduleName = ((EditText) mDialogView.findViewById(R.id.newScheduleName)).getText().toString();

                        if (scheduleName != null && scheduleName != "") {
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
                })*/
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ScheduleUpdateOrDeleteOptionDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();


    }



}
