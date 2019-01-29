package apps.gpr.noteworld.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.model.Colors;
import apps.gpr.noteworld.utils.Const;
import apps.gpr.noteworld.utils.PrefUtils;
import apps.gpr.noteworld.views.ScreenModeActivity;
import apps.gpr.noteworld.views.fragments.ThemeColorFragment;

public class ColorsAdapter extends ArrayAdapter<Colors> {

    List<Colors> list;
    String savedTheme;
    Context _context;

    public ColorsAdapter(Context c, ArrayList<Colors> colorsList, String theme) {
        super(c, 0, colorsList);
        this.list = colorsList;
        this._context = c;
        this.savedTheme = theme;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Colors colors = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.settings_colors_list_row, parent, false);
        }

        final TextView txt_color = view.findViewById(R.id.txt_color);
        txt_color.setText(colors.getColor());

        savedTheme = PrefUtils.getStringPreference(PrefUtils.KEY_THEME_COLOR,_context);

        if (savedTheme.equals(colors.getColor()) && colors.getColor().equals(_context.getResources().getString(R.string.color_black))){
            view.setBackgroundResource(colors.getColorResId());
            txt_color.setTextColor(_context.getResources().getColor(R.color.colorWhite));
        }
        else if (savedTheme.equals(colors.getColor()) && !colors.getColor().equals(_context.getResources().getString(R.string.color_black))) {
            view.setBackgroundResource(colors.getColorResId());
            if (PrefUtils.getModePreference(getContext()).equals(Const.SCREEN_DARK)){
                txt_color.setTextColor(Color.WHITE);
            }else
                txt_color.setTextColor(_context.getResources().getColor(R.color.colorBlack));
        }
        else {
            view.setBackgroundColor(Color.TRANSPARENT);
            if (PrefUtils.getModePreference(getContext()).equals(Const.SCREEN_DARK)){
                txt_color.setTextColor(Color.WHITE);
            }else
                txt_color.setTextColor(_context.getResources().getColor(R.color.colorBlack));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Colors colors = getItem(position);
                view.setBackgroundColor(Color.TRANSPARENT);
                view.setBackgroundResource(colors.getColorResId());
                if (colors.getColor().equals(_context.getResources().getString(R.string.color_black))) {
                    txt_color.setTextColor(_context.getResources().getColor(R.color.colorWhite));
                }

                ((ScreenModeActivity) _context).saveThemeColor(colors.getColor());
            }
        });

        return view;
    }
}