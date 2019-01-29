package apps.gpr.noteworld.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.HashMap;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.utils.CommonUtilities;
import apps.gpr.noteworld.utils.Const;
import apps.gpr.noteworld.views.FullScreenActivity;

public class ImagesAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater inflater;
    String mode;

    public ImagesAdapter(Context c, String mode) {
        this.context = c;
        inflater = LayoutInflater.from(context);
        this.mode = mode;
        setUpRawData();
    }

    @Override
    public int getCount() {
        return screenShotIds.length;
    }

    @Override
    public Object getItem(int i) {
        return screenShotIds[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {
            view = inflater.inflate(R.layout.grid_item,viewGroup,false);
            view.setTag(R.id.picture, view.findViewById(R.id.picture));
        }

        imageView  = (ImageView) view.getTag(R.id.picture);
        imageView.setImageResource(screenShotIds[position]);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, FullScreenActivity.class);
                in.putExtra("title", (String) screenShots.get(screenShotIds[position]));
                in.putExtra("image_key",screenShotIds[position]);
                context.startActivity(in);
            }
        });

        return view;
    }

    private Integer[] screenShotIds;
    private SparseArray<Object> screenShots;
    private void setUpRawData() {
        screenShots = new SparseArray<>();

        if (screenShots.size() > 0)
            screenShots.clear();

        if (screenShotIds != null) {
            if (screenShotIds.length > 0)
                screenShotIds = null;
        }

        if (mode.equals(Const.SCREEN_DARK)) {
            /*screenShotIds = new Integer[]{R.raw.dark_notes_list_indigo, R.raw.dark_add_note_indigo, R.raw.dark_theme_color_indigo,
                    R.raw.dark_notes_list_cyan, R.raw.dark_notes_list_lime, R.raw.dark_notes_list_purple,
                    R.raw.dark_notes_ist_teal, R.raw.dark_notes_list_black, R.raw.dark_notes_list_brown};

            screenShots.put(R.raw.dark_notes_list_indigo,context.getResources().getString(R.string.str_Dark_mode_Indigo_color));
            screenShots.put(R.raw.dark_add_note_indigo,context.getResources().getString(R.string.str_Dark_mode_Indigo_color));
            screenShots.put(R.raw.dark_theme_color_indigo,context.getResources().getString(R.string.str_Dark_mode_Indigo_color));
            screenShots.put(R.raw.dark_notes_list_cyan,context.getResources().getString(R.string.str_Dark_mode_Cyan_color));
            screenShots.put(R.raw.dark_notes_list_lime,context.getResources().getString(R.string.str_Dark_mode_Lime_color));
            screenShots.put(R.raw.dark_notes_list_purple,context.getResources().getString(R.string.str_Dark_mode_Purple_color));
            screenShots.put(R.raw.dark_notes_ist_teal,context.getResources().getString(R.string.str_Dark_mode_Teal_color));
            screenShots.put(R.raw.dark_notes_list_black,context.getResources().getString(R.string.str_Dark_mode_Black_color));
            screenShots.put(R.raw.dark_notes_list_brown,context.getResources().getString(R.string.str_Dark_mode_Brown_color));*/
        }
        else{
           /* screenShotIds = new Integer[]{R.raw.light_notes_blue, R.raw.light_notes_cyan, R.raw.light_add_note_cyan,
                    R.raw.light_settings_pink, R.raw.light_notes_pink, R.raw.light_themecolor_purple,
                    R.raw.light_notes_purple, R.raw.light_notes_green, R.raw.light_notes_black};

            screenShots.put(R.raw.light_notes_blue,context.getResources().getString(R.string.str_Light_mode_Blue_color));
            screenShots.put(R.raw.light_notes_cyan,context.getResources().getString(R.string.str_Light_mode_Cyan_color));
            screenShots.put(R.raw.light_add_note_cyan,context.getResources().getString(R.string.str_Light_mode_Cyan_color));
            screenShots.put(R.raw.light_settings_pink,context.getResources().getString(R.string.str_Light_mode_Pink_color));
            screenShots.put(R.raw.light_notes_pink,context.getResources().getString(R.string.str_Light_mode_Pink_color));
            screenShots.put(R.raw.light_themecolor_purple,context.getResources().getString(R.string.str_Light_mode_Purple_color));
            screenShots.put(R.raw.light_notes_purple,context.getResources().getString(R.string.str_Light_mode_Purple_color));
            screenShots.put(R.raw.light_notes_green,context.getResources().getString(R.string.str_Light_mode_Green_color));
            screenShots.put(R.raw.light_notes_black,context.getResources().getString(R.string.str_Light_mode_Black_color));*/
        }
    }
}
