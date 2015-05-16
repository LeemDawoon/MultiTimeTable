package app.com.multitimetable.android.multitimetable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import app.com.multitimetable.android.multitimetable.data.MTTContract;


public class SubjectDeleteDialogFragment extends android.support.v4.app.DialogFragment {
    public static final String ARG_SUBJECT_ID = "subject_id";
    public static final String ARG_SUBJECT_NAME = "subject_name";

    public interface SubjectDeleteDialogFragmentCallback{
        public void onSubjectDeleteComplete();
    }

    private View mDialogView;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        mDialogView = getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_activated_1, null);
        TextView textView = (TextView)mDialogView.findViewById(android.R.id.text1);
        textView.setText("'"+getArguments().getString(ARG_SUBJECT_NAME)+"'를 삭제하시겠습니까?");

        builder.setTitle(R.string.delete_subject)
                .setView(mDialogView)
                // Add action buttons
                .setPositiveButton(R.string.btn_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                       int count = getActivity().getContentResolver().delete(
                                MTTContract.SubjectEntry.CONTENT_URI,
                                MTTContract.SubjectEntry._ID + " = ? ",
                                new String[]{Integer.toString(getArguments().getInt(ARG_SUBJECT_ID))}
                        );
                        ((SubjectDeleteDialogFragmentCallback)getActivity()).onSubjectDeleteComplete();

//                        Toast.makeText(getActivity(), "count:" + count, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SubjectDeleteDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();


    }



}
