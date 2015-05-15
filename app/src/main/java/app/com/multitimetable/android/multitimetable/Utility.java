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


}
