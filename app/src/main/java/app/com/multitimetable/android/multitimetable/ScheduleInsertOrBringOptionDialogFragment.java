package app.com.multitimetable.android.multitimetable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;


public class ScheduleInsertOrBringOptionDialogFragment extends DialogFragment {


    private View mDialogView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        mDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_schedule_update_or_delete_option_dialog, null);

        Button updateScheduleOption = (Button) mDialogView.findViewById(R.id.updateScheduleOption);
        updateScheduleOption.setText("Make a New TimeTable");
        updateScheduleOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment scheduleInsertDialogFragment = new ScheduleInsertDialogFragment();
                scheduleInsertDialogFragment.show(getActivity().getSupportFragmentManager(),"SCHEDULE_INSERT_DIALOG_FRAGMENT");
                ScheduleInsertOrBringOptionDialogFragment.this.getDialog().cancel(); // 현재 다이알로그를 종료한다.
            }
        });

        Button deleteScheduleOption = (Button) mDialogView.findViewById(R.id.deleteScheduleOption);
        deleteScheduleOption.setText("Bring a TimeTable");
        deleteScheduleOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScheduleInsertOrBringOptionDialogFragment.this.getDialog().cancel(); // 현재 다이알로그를 종료한다.
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
                        ScheduleInsertOrBringOptionDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();


    }



}
