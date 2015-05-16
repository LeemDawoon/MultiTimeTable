package app.com.multitimetable.android.multitimetable;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by wable on 15. 5. 5..
 */
public class Utility {

    private static int displayWidth;
    private static int displayHeight;
    private static int actionBarHeight;

    public static int getDisplayWidth() {
        return displayWidth;
    }
    public static void setDisplayWidth(int displayWidth) {
        Utility.displayWidth = displayWidth;
    }

    public static int getDisplayHeight() {return displayHeight;}
    public static void setDisplayHeight(int displayHeight) { Utility.displayHeight = displayHeight;}

    public static int getHourCellHeight(int rowCount){
        int hourCellHeight = (displayHeight-actionBarHeight)/rowCount;
        if (hourCellHeight<actionBarHeight) {
            hourCellHeight = actionBarHeight;
        }
        return hourCellHeight;
    };

    public static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

    public static int getActionBarHeight() {return actionBarHeight;}
    public static void setActionBarHeight(int actionBarHeight) {Utility.actionBarHeight = actionBarHeight;}

    public static int getFormattedTimeForDB(String textOfView) {
        if(textOfView==null|| textOfView=="" || textOfView.length()!=5) {
            return 0;
        }
        int hour = Integer.parseInt(textOfView.substring(0,2));
        int min = Integer.parseInt(textOfView.substring(3));
        return hour*60+min;
    }
    public static String getFormattedTimeForView(int dataOfDB ) {
        int hour = dataOfDB/60;
        int min = dataOfDB%60;
        return String.format("%02d:%02d", hour, min);
    }



    // validation
    public static boolean validate(String stringData) {
        if (null !=stringData && stringData.length() > 0){
            return true;
        } else {
            return false;
        }

    }
    /*public static int getDeviceWidth(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return  displayMetrics.widthPixels;// 가로
    }*/


    public static int getDayAbbrResource(int day) {
        int res=0;
        switch (day){
            case 0:
            {
                res = R.string.time_and_day;
                break;
            }
            case 1:
            {
                res = R.string.time_mon;
                break;
            }
            case 2:
            {
                res = R.string.time_tue;
                break;
            }
            case 3:
            {
                res = R.string.time_wed;
                break;
            }
            case 4:
            {
                res = R.string.time_thu;
                break;
            }
            case 5:
            {
                res = R.string.time_fri;
                break;
            }
            case 6:
            {
                res = R.string.time_sat;
                break;
            }
            case 7:
            {
                res = R.string.time_sun;
                break;
            }
        }
        return res;
    }
    public static int getDayResource(int day) {
        int res=0;
        switch (day){
            case 1:
            {
                res = R.string.monday;
                break;
            }
            case 2:
            {
                res = R.string.tuesday;
                break;
            }
            case 3:
            {
                res = R.string.wednesday;
                break;
            }
            case 4:
            {
                res = R.string.thursday;
                break;
            }
            case 5:
            {
                res = R.string.friday;
                break;
            }
            case 6:
            {
                res = R.string.saturday;
                break;
            }
            case 7:
            {
                res = R.string.sunday;
                break;
            }
        }
        return res;
    }
    public static int getColorResource(int subjectId) {
        int res=0;
        subjectId=subjectId%12;
        switch (subjectId){
            case 0:
            {
                res = R.color.subject_12;
                break;
            }
            case 1:
            {
                res = R.color.subject_01;
                break;
            }
            case 2:
            {
                res = R.color.subject_02;
                break;
            }
            case 3:
            {
                res = R.color.subject_03;
                break;
            }
            case 4:
            {
                res = R.color.subject_04;
                break;
            }
            case 5:
            {
                res = R.color.subject_05;
                break;
            }
            case 6:
            {
                res = R.color.subject_06;
                break;
            }
            case 7:
            {
                res = R.color.subject_07;
                break;
            }
            case 8:
            {
                res = R.color.subject_08;
                break;
            }
            case 9:
            {
                res = R.color.subject_09;
                break;
            }
            case 10:
            {
                res = R.color.subject_10;
                break;
            }
            case 11:
            {
                res = R.color.subject_11;
                break;
            }
        }
        return res;
    }
}
