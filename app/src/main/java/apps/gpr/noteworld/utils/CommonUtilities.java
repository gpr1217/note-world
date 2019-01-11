package apps.gpr.noteworld.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.CpuUsageInfo;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import apps.gpr.noteworld.model.Notes;

import static android.content.Context.ACTIVITY_SERVICE;

public class CommonUtilities {

    private static Context context;
    public CommonUtilities(Context c) {
        this.context = c;
    }
    public static String folder = "NWdata";


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
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat(Const.FORMAT_DEFAULT_DATE_TIME);
            Date date = fmt.parse(dateStr);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmtOut = new SimpleDateFormat(targetFormat);
            return fmtOut.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String formatDate(String dateStr,String sourceFormat,String targetFormat) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat(sourceFormat);
            Date date = fmt.parse(dateStr);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmtOut = new SimpleDateFormat(targetFormat);
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
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(sourceFormat);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(utcTime);
            df.setTimeZone(TimeZone.getDefault());
            desireDatetime = df.format(date);
            //desireDatetime = getDesireDateFormat(desireDatetime, sourceFormat, targetFormat);
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
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat(sourceFormat);
            Date date1 = sdf1.parse(nDate);

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat(sourceFormat);
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

    public static String getScreenSize(Context context){
        int screenSize = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenSize){
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                return Const.SCREEN_SIZE_XLARGE;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return Const.SCREEN_SIZE_LARGE;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return Const.SCREEN_SIZE_NORMAL;
            case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
                return Const.SCREEN_SIZE_UNDEFINED;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return Const.SCREEN_SIZE_SMALL;
            default:
                return "";
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * READ and WRITE to External Storage
     */

    private static boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            return true;
        }
        return false;
    }

    private static File createFileInExternalStorage(String filename){
        log(String.valueOf(Environment.getExternalStorageDirectory()));
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + folder);
        if (dir.mkdirs()){
            log("mkdir");
        }

        File noteFile = new File(Environment.getExternalStorageDirectory() + "/" + folder , filename);
        log("noteFile", noteFile.getPath());
        return noteFile;
    }

    public static File getAttachment(String data, String filename){
        if (isExternalStorageWritable()){
            try {

                File file = createFileInExternalStorage(filename);
                log("getAttachment",file.getPath());
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                fileOutputStream.write(data.getBytes());
                fileOutputStream.close();

                if (isExternalStorageReadable()){
                    File root = Environment.getExternalStorageDirectory();
                    String filePath =  folder + "/" + filename;

                    return new File(root,filePath);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static boolean deleteAttachment(String filename){
        File file = null;
        try{
            file = new File(Environment.getExternalStorageDirectory() + "/" + folder , filename);
        }catch (Exception e){
            e.printStackTrace();
        }

        return file.delete();
    }

    /*private String createFileInInternalStorage(Notes selectedItem){
        String filename = "note_"+selectedItem.getId();
        String note = selectedItem.getNote();
        FileOutputStream outputStream;

        try{
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(note.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filename;
    }

    private File fileToSend(String filename){
        String filePath = this.getFilesDir() + "/" + filename;

        File file = new File(filePath);

        return file;
    }*/

}
