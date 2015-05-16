package app.com.multitimetable.android.multitimetable;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import app.com.multitimetable.android.multitimetable.data.MTTContract;


public class SelectScheduleFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>
{

//    private OnFragmentInteractionListener mListener;
    private SelectScheduleAdapter mSelectScheduleAdapter;

    private static final int SCHEDULE_LOADER = 0;

    private static final String[] SCHEDULE_COLUMNS = {
            MTTContract.ScheduleEntry.TABLE_NAME + "." + MTTContract.ScheduleEntry._ID,
            MTTContract.ScheduleEntry.COLUMN_NAME,
            MTTContract.ScheduleEntry.COLUMN_IS_OFF,
    };
    static final int COL_SCHEDULE_ID = 0;
    static final int COL_SCHEDULE_NAME = 1;
    static final int COL_IS_OFF = 2;

    public ListView mListView;
    public static SelectScheduleFragment mFragment;

    public static SelectScheduleFragment newInstance() {
        if (mFragment==null) {
            mFragment = new SelectScheduleFragment();
        }
//        SelectScheduleFragment fragment = new SelectScheduleFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return mFragment;
    }

    public SelectScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        // initLoader : Ensures a loader is initialized and active.
        getLoaderManager().initLoader(SCHEDULE_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);

        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // mSelectScheduleAdapter 초기화
        Cursor cursor = getActivity().getContentResolver().query(MTTContract.ScheduleEntry.CONTENT_URI, null,null,null,null);
        mSelectScheduleAdapter = new SelectScheduleAdapter(getActivity(), cursor, 0);

        // Inflate the layout for this fragment
        mListView = (ListView) inflater.inflate(R.layout.fragment_select_schedule, container, false);
        mListView.setAdapter(mSelectScheduleAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return mListView;
    }

   /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

   /* @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /* Cursor Loarder Callbacks */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder = MTTContract.ScheduleEntry._ID + " ASC";

        return new CursorLoader(getActivity(),
                MTTContract.ScheduleEntry.CONTENT_URI,
                SCHEDULE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mSelectScheduleAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mSelectScheduleAdapter.swapCursor(null);
    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

}
