package apps.gpr.noteworld.views.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.adapters.ColorsAdapter;
import apps.gpr.noteworld.model.Colors;
import apps.gpr.noteworld.model.Settings;
import apps.gpr.noteworld.utils.BaseActivity;
import apps.gpr.noteworld.utils.CommonUtilities;
import apps.gpr.noteworld.utils.Const;
import apps.gpr.noteworld.utils.PrefUtils;
import apps.gpr.noteworld.views.SettingsActivity;


public class SettingsFragment extends Fragment {

    private ListView colors_list;
    private List<String> colorsArr;

    private ArrayAdapter<Colors> colorsArrayAdapter;
    private ArrayList<Colors> colorsArrayList;
    private ColorsAdapter colorsAdapter;

    private PrefUtils prefUtils;

    //private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefUtils = new PrefUtils(getActivity());
    }

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings,container, false);

        colorsArr = Arrays.asList(getResources().getStringArray(R.array.colors));
        colors_list = view.findViewById(R.id.colors_list);
        colorsArrayList = new ArrayList<>();

        final String theme = prefUtils.getStringPreference(PrefUtils.KEY_THEME_COLOR);

        int resId;
        for (String c : colorsArr ){

            if (c.equals(getResources().getString(R.string.color_black)))
                resId = R.color.colorBlack;
            else if (c.equals(getResources().getString(R.string.color_blue)))
                resId = R.color.colorBlue;
            else if (c.equals(getResources().getString(R.string.color_pink)))
                resId = R.color.colorPink;
            else if (c.equals(getResources().getString(R.string.color_gray)))
                resId = R.color.colorGray;
            else if (c.equals(getResources().getString(R.string.color_yellow)))
                resId = R.color.colorYellow;
            else if (c.equals(getResources().getString(R.string.color_teal)))
                resId = R.color.colorTeal;
            else if (c.equals(getResources().getString(R.string.color_brown)))
                resId = R.color.colorBrown;
            else if (c.equals(getResources().getString(R.string.color_green)))
                resId = R.color.colorGreen;
            else if (c.equals(getResources().getString(R.string.color_indigo)))
                resId = R.color.colorIndigo;
            else if (c.equals(getResources().getString(R.string.color_cyan)))
                resId = R.color.colorCyan;
            else if (c.equals(getResources().getString(R.string.color_lime)))
                resId = R.color.colorLime;
            else if (c.equals(getResources().getString(R.string.color_purple)))
                resId = R.color.colorPurple;
            else
                resId = R.color.colorOrange;

            Colors colors = new Colors(c,resId);
            colorsArrayList.add(colors);
        }

        colorsAdapter = new ColorsAdapter(getActivity(), colorsArrayList, theme);
        colors_list.setAdapter(colorsAdapter);

        return view;
    }

    public void saveThemeColor(String color){
        if (!color.equals(""))
            prefUtils.savePreference(PrefUtils.KEY_THEME_COLOR, color);

        getActivity().finish();
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        intent.putExtra("StartFragment",true);
        startActivity(intent);
    }

    /*public class ColorsAdapter extends ArrayAdapter<Colors>{

        List<Colors> list;
        String savedTheme;
        Colors colors;

        public ColorsAdapter(Context c,ArrayList<Colors> colorsList, String theme) {
            super(c, 0, colorsList);
            this.list = colorsList;
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

            if (savedTheme.equals(colors.getColor()) && colors.getColor().equals(getResources().getString(R.string.color_black))){
                view.setBackgroundResource(colors.getColorResId());
                txt_color.setTextColor(getResources().getColor(R.color.colorWhite));
            }
            else if (savedTheme.equals(colors.getColor()) && !colors.getColor().equals(getResources().getString(R.string.color_black))) {
                view.setBackgroundResource(colors.getColorResId());
                if (PrefUtils.getModePreference(getContext()).equals(Const.SCREEN_DARK)){
                    txt_color.setTextColor(Color.WHITE);
                }else
                    txt_color.setTextColor(getResources().getColor(R.color.colorBlack));
            }
            else {
                view.setBackgroundColor(Color.TRANSPARENT);
                if (PrefUtils.getModePreference(getContext()).equals(Const.SCREEN_DARK)){
                    txt_color.setTextColor(Color.WHITE);
                }else
                    txt_color.setTextColor(getResources().getColor(R.color.colorBlack));
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Colors colors = getItem(position);
                        view.setBackgroundColor(Color.TRANSPARENT);
                        view.setBackgroundResource(colors.getColorResId());
                        if (colors.getColor().equals(getResources().getString(R.string.color_black))) {
                            txt_color.setTextColor(getResources().getColor(R.color.colorWhite));
                        }

                        saveThemeColor(colors.getColor());
                }
            });

            return view;
        }
    }*/

}
