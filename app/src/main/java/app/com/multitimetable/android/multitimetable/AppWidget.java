package app.com.multitimetable.android.multitimetable;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import app.com.multitimetable.android.multitimetable.data.MTTContract;

import static android.widget.GridLayout.spec;


/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    private static final String[] TIMETABLE_COLUMNS = {
            MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry._ID,
            MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry.COLUMN_DAY,
            MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry.COLUMN_START_TIME,
            MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry.COLUMN_END_TIME,
            MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry.COLUMN_PLACE,
            MTTContract.SubjectEntry.TABLE_NAME + "." + MTTContract.SubjectEntry._ID,
            MTTContract.SubjectEntry.TABLE_NAME + "." + MTTContract.SubjectEntry.COLUMN_NAME,
            MTTContract.SubjectEntry.TABLE_NAME + "." + MTTContract.SubjectEntry.COLUMN_PROFESSOR,
            MTTContract.SubjectEntry.TABLE_NAME + "." + MTTContract.SubjectEntry.COLUMN_COLOR,

    };
    static final int COL_TIME_ID=0;
    static final int COL_TIME_DAY=1;
    static final int COL_TIME_START_TIME=2;
    static final int COL_TIME_END_TIME=3;
    static final int COL_TIME_PLACE=4;
    static final int COL_SUBJECT_ID=5;
    static final int COL_SUBJECT_NAME=6;
    static final int COL_SUBJECT_PROFESSOR=7;
    static final int COL_SUBJECT_COLOR=8;



    private static final String[] TIMETABLE_LAYOUT_COLUMNS = {
            "MAX("+MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry.COLUMN_DAY+")",
            "MIN("+MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry.COLUMN_START_TIME+")",
            "MAX("+MTTContract.TimeEntry.TABLE_NAME + "." + MTTContract.TimeEntry.COLUMN_END_TIME+")"
    };
    static final int COL_LAST_DAY=0;
    static final int COL_EARLIEST_TIME=1;
    static final int COL_LATEST_TIME=2;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);

        RemoteViews background = getTimeTableBackground(context,15);
        RemoteViews timetable = getTimeTable(context,15);


        views.addView(R.id.rootView,background);
        views.addView(R.id.rootView,timetable);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /* 시간표 배경 그리기 */
    private static RemoteViews getTimeTableBackground(Context context, int scheduleID){

        RemoteViews background = new RemoteViews(context.getPackageName(), R.layout.app_widget_timetable_background);


        Uri subjectAndTimeByScheduleUri = MTTContract.SubjectEntry.buildSubjectTimeWithScheduleIdUri(scheduleID);

        Cursor cursorForLayout = context.getContentResolver().query(
                subjectAndTimeByScheduleUri,
                TIMETABLE_LAYOUT_COLUMNS,
                null,
                null,
                null);

        cursorForLayout.moveToFirst();
        int startTime = cursorForLayout.getInt(COL_EARLIEST_TIME)/60;
        int endTime = cursorForLayout.getInt(COL_LATEST_TIME)/60;
        int columnCount = 8;
        int rowCount = endTime-startTime+1;
        cursorForLayout.close();




        RemoteViews[] day = new RemoteViews[columnCount];
        for (int i =0, length=day.length; i<length; i++) {
            day[i] = new RemoteViews(context.getPackageName(), R.layout.app_widget_textview);
            day[i].setTextViewText(R.id.appwidget_text, Utility.getDayAbbrString(i));
//            day[i].setGravity(Gravity.CENTER);

//            day[i].setTextSize(20);
//            day[i].setBackgroundColor(R.color.abc_primary_text_material_light);

            background.addView(R.id.backgroundGridLayout, day[i]);
        }


//        GradientDrawable gd = new GradientDrawable();
//        gd.setColor(Color.TRANSPARENT); // Changes this drawbale to use a single color instead of a gradient
//        gd.setCornerRadius(1);
//        gd.setStroke(1, 0xFF000000);
//
//
        for (int i=0; i<rowCount; i++) {
            for(int j=0; j<columnCount; j++) {
                RemoteViews timeColumn = new RemoteViews(context.getPackageName(), R.layout.app_widget_textview);
//                timeColumn.setWidth(Utility.getDisplayWidth() / columnCount);
//                timeColumn.setHeight(Utility.getDisplayHeight()/(rowCount+1));
//
//                timeColumn.setBackgroundDrawable(gd);
//
                if(j==0) timeColumn.setTextViewText(R.id.appwidget_text, startTime + i + " ");
//
//                timeColumn.setGravity(Gravity.RIGHT);
//                timeColumn.setTextSize(20);
//                GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams();
//                gridLayoutParam.columnSpec = spec(j);
//                gridLayoutParam.rowSpec = spec(i+1);
//                gridLayout.addView(timeColumn,gridLayoutParam);
                background.addView(R.id.backgroundGridLayout, timeColumn);
            }
//
        }

        return background;

    }



    /* 시간표 그리기 Linear Layout */
    private  static RemoteViews getTimeTable(Context context, int scheduleID ){
        RemoteViews gridLayout = new RemoteViews(context.getPackageName(), R.layout.app_widget_timetable);

        Uri subjectAndTimeByScheduleUri = MTTContract.SubjectEntry.buildSubjectTimeWithScheduleIdUri(scheduleID);
        Cursor cursorForLayout = context.getContentResolver().query(
                subjectAndTimeByScheduleUri,
                TIMETABLE_LAYOUT_COLUMNS,
                null,
                null,
                null);

        cursorForLayout.moveToFirst();
        int startTime = cursorForLayout.getInt(COL_EARLIEST_TIME)/60;
        int endTime = cursorForLayout.getInt(COL_LATEST_TIME)/60;
        int columnCount =8;
        int rowCount = endTime-startTime+1;
        int cellWidth = Utility.getDisplayWidth() / columnCount;
        int cellHeight = Utility.getDisplayHeight()/(rowCount+1);


        RemoteViews[] day = new RemoteViews[columnCount];
        for (int i =0, length=day.length; i<length; i++) {
            day[i] = new RemoteViews(context.getPackageName(), R.layout.app_widget_textview);
            gridLayout.addView(R.id.timetableGridLayout, day[i]);
        }

/*
        RemoteViews timeColumn = new RemoteViews(context.getPackageName(), R.layout.app_widget_textview);
        timeColumn.set
        timeColumn.setHeight(cellHeight*rowCount);
        timeColumn.setText(" ");
        GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams();
        gridLayoutParam.columnSpec = spec(0);
        gridLayoutParam.rowSpec = spec(1);
        gridLayout.addView(timeColumn,gridLayoutParam);


        String sortOrder = MTTContract.TimeEntry.COLUMN_DAY + " ASC, " +
                MTTContract.TimeEntry.COLUMN_START_TIME + " ASC";
        final Cursor cursor = context.getContentResolver().query(
                subjectAndTimeByScheduleUri,
                TIMETABLE_COLUMNS,
                null,
                null,
                sortOrder);

        LinearLayout[] colLinearLayout = new LinearLayout[7];
        for (int i=0, length=colLinearLayout.length; i<length; i++){
            colLinearLayout[i]= new LinearLayout(context);
            colLinearLayout[i].setOrientation(LinearLayout.VERTICAL);
        }


        int previousCol = 0;
        float previousEnd = (float)(cursorForLayout.getInt(COL_EARLIEST_TIME));
        while(cursor.moveToNext()){

            int col = cursor.getInt(COL_TIME_DAY);
            float start = (float)cursor.getInt(COL_TIME_START_TIME);
            float end = (float)cursor.getInt(COL_TIME_END_TIME);
            float rowSpan = (end-start)/60.0f;// + ((end-start)%60)/60;;

            final int subjectIdForDetail = cursor.getInt(COL_SUBJECT_ID);

            TextView subject = new TextView(context);
            subject.setWidth(cellWidth);
            subject.setHeight((int)(cellHeight*rowSpan));
            subject.setText(cursor.getString(COL_SUBJECT_NAME));
            subject.setTextSize(25);
            subject.setGravity(Gravity.CENTER);
            subject.setBackgroundResource(Utility.getColorResource(subjectIdForDetail));



            // 이전 과목 데이터가 지금 과목 데이터와 다른 요일임.
            if(previousCol != col && previousCol !=0){
                GridLayout.LayoutParams subjectGridLayoutParam = new GridLayout.LayoutParams();
                subjectGridLayoutParam.columnSpec = spec(previousCol);
                subjectGridLayoutParam.rowSpec = spec(1);
                gridLayout.addView(colLinearLayout[previousCol-1],subjectGridLayoutParam);
                previousEnd = (float)(cursorForLayout.getInt(COL_EARLIEST_TIME));

            }

            if(previousEnd !=0.0f && previousEnd!=start) {
                TextView blank = new TextView(context);
                blank.setWidth(cellWidth);
                blank.setHeight((int)(cellHeight*(start-previousEnd)/60.0f));
                blank.setText("blank");
                colLinearLayout[col-1].addView(blank);
            }

            colLinearLayout[col-1].addView(subject);

            previousCol = col;
            previousEnd = end;


            if (cursor.isLast()){
                GridLayout.LayoutParams subjectGridLayoutParam = new GridLayout.LayoutParams();
                subjectGridLayoutParam.columnSpec = spec(col);
                subjectGridLayoutParam.rowSpec = spec(1);
                gridLayout.addView(colLinearLayout[col-1],subjectGridLayoutParam);
            }

        }
        cursorForLayout.close();
        cursor.close();*/
        return gridLayout;
    }
}


