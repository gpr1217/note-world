package apps.gpr.noteworld.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogsManager {

    static Context _context;
    static AlertDialog.Builder builder;
    public DialogsManager(Context c) {
        this._context = c;
    }

    public static void openSimpleAlertDialog(String title){
        builder = new AlertDialog.Builder(_context);
        builder.setTitle(title);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.create();

        builder.show();
    }

    public static void openYesNoAlertDialog(String title){

    }
}
