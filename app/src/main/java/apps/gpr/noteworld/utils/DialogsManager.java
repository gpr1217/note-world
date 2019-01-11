package apps.gpr.noteworld.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

class DialogsManager {

    private static AlertDialog.Builder builder;
    public DialogsManager() {

    }

    public static void openSimpleAlertDialog(String title, Context _context){
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

}
