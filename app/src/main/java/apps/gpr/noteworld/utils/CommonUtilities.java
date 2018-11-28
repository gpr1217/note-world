package apps.gpr.noteworld.utils;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.Context.ACTIVITY_SERVICE;

public class CommonUtilities {

    Context context;
    public CommonUtilities(Context c) {
        this.context = c;
    }

    public static String getCurrentDate(){
        Date rightNow = Calendar.getInstance().getTime();

        return rightNow.toString();
    }

    /**
     * Fetches tablet's default timezone datetime
     * @param format is the format that you want the date to be in
     * @return Returns string of datetime
     */
        public static String getCurrentDate(String format){
        String current_date = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(format, Locale.ENGLISH);
            Calendar calobj = Calendar.getInstance();
            current_date = df.format(calobj.getTime());

        }catch (Exception e){
            e.printStackTrace();
        }
        return current_date;
    }

    public String getCurrentUTCDate(){
        Date rightNow = Calendar.getInstance().getTime();

        return rightNow.toString();
    }

    /**
     * Coverts default date time format to target format
     * @param dateStr
     * @param targetFormat
     * @return
     */
    public static String formatDate(String dateStr,String targetFormat) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(Const.FORMAT_DEFAULT_DATE_TIME);
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat(targetFormat);
            return fmtOut.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String formatDate(String dateStr,String sourceFormat,String targetFormat) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(sourceFormat);
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat(targetFormat);
            return fmtOut.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Converts UTC datetime to tablet's local time
     * @param utcTime - UTC datetime
     * @param sourceFormat - UTC datetime format
     * @param targetFormat - format needed
     * @return - Returns String of datetime in required format
     */
    public static String convertUTCToLocal(String utcTime,String sourceFormat, String targetFormat){
        String desireDatetime = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat(sourceFormat);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(utcTime);
            df.setTimeZone(TimeZone.getDefault());
            desireDatetime = df.format(date);
            //desireDatetime = getDesireDateFormat(desireDatetime, sourceFormat, targetFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return desireDatetime;
    }

    /**
     * Converts UTC datetime to tablet's local time
     * @param nDate - UTC datetime
     * @param sourceFormat - UTC datetime format
     * @return - Returns String of datetime in required format
     */
    public static String getNoteDateTimeFormat(String sourceFormat, String nDate){
        try {
            CommonUtilities.log("nDate",nDate);
            SimpleDateFormat sdf1 = new SimpleDateFormat(sourceFormat);
            Date date1 = sdf1.parse(nDate);

            SimpleDateFormat sdf2 = new SimpleDateFormat(sourceFormat);
            Date date2 = sdf2.parse(CommonUtilities.getCurrentDate(Const.FORMAT_DEFAULT_DATE));

            if (date1.compareTo(date2) > 0){
                log("when","tomorrow");
                return "Tomorrow";
            }else if (date1.compareTo(date2) < 0){
                log("when","Yesterday");
                return "Yesterday";
            }else if (date1.compareTo(date2) == 0){
                log("when","today");
                return "Today";
            }else{
                return "None";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void log(String message){
        try{
            Log.d(Const.TAG,message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void log(String tag,String message){
        try{
            Log.d(tag,message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean isIntentServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("apps.gpr.noteworld.core.DatabaseService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
