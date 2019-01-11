package apps.gpr.noteworld.utils;

import java.util.Arrays;
import java.util.List;

public class Const {

    public static final String TAG = "NOTE_WORLD";

    public static final String INSERT = "insert";
    public static final String FETCH = "fetch";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    public static final String SEARCH = "search";

    public static final String TABLE_NOTES = "notes";
    public static final String TABLE_SETTINGS = "settings";

    public static final String FILTER_ACTION_DATA = "Action_Data";
    public static final String FILTER_ACTION_SETTINGS_DATA = "Settings_Action_Data";

    public static final String FORMAT_DEFAULT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DEFAULT_DATE = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DEFAULT_TIME = "HH:mm:ss";
    public static final String FORMAT_LIST_ROW_DATE = "MMM d";
    public static final String FORMAT_LIST_ROW_DATE_TIME = "MMM d h:mm a";
    public static final String FORMAT_h_mm_a = "h:mm a"; // 12:08 PM

    public static final List<String> settingsList = Arrays.asList("Theme and Mode","Passcode Lock", "Add Email");
    public static final List<String> lockSettingsList = Arrays.asList("Enable Passcode","Set Passcode");
    public static final List<String> lockEnabledSettingsList = Arrays.asList("Disable Passcode","Change Passcode");
    public static final String THEME_AND_MODE = "Theme and Mode";
    public static final String PASSWORD_LOCK = "Passcode Lock";
    public static final String ADD_EMAIL = "Add Email";
    public static final String SETTINGS_PASSWORD_LOCK = "Passcode Lock";
    public static final String SETTINGS_THEME_MODE = "Theme and Mode";
    public static final String SETTINGS_ADD_EMAIL = "Add Email";

    public static final String SET_PASSCODE = "Set Passcode";
    public static final String ENABLE_PASSCODE = "Enable Passcode";
    public static final String DISABLE_PASSCODE = "Disable Passcode";
    public static final String CHANGE_PASSCODE = "Change Passcode";

    public static final String SCREEN_SIZE_XLARGE = "XLarge";
    public static final String SCREEN_SIZE_LARGE = "Large";
    public static final String SCREEN_SIZE_NORMAL = "Normal";
    public static final String SCREEN_SIZE_SMALL = "Small";
    public static final String SCREEN_SIZE_UNDEFINED = "Undefined";

    public static final String ACTION_PASSCODE = "action";
    public static final String PASSCODE_CHANGE = "change";
    public static final String PASSCODE_NEW = "new";

    public static final String SCREEN_LIGHT = "light";
    public static final String SCREEN_DARK = "dark";

    public static final String INTENT_ACTION_NOTES = "action_notes";
    public static final String ACTION_NEW_NOTE = "new note";
    public static final String ACTION_EDIT_NOTE = "edit note";

}
