package apps.gpr.noteworld.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.adapters.ColorsAdapter;
import apps.gpr.noteworld.model.Colors;
import apps.gpr.noteworld.utils.PrefUtils;

public class ThemeColorFragment extends Fragment {

    private ListView colors_list;
    private List<String> colorsArr;

    private ArrayList<Colors> colorsArrayList;
    private ColorsAdapter colorsAdapter;

    private PrefUtils prefUtils;

    public ThemeColorFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefUtils = new PrefUtils(getActivity());
    }

    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings,container, false);

        RelativeLayout add_email_layout = view.findViewById(R.id.add_email_layout);
        add_email_layout.setVisibility(View.GONE);

        colors_list = view.findViewById(R.id.colors_list);
        colors_list.setVisibility(View.VISIBLE);

        colorsArr = Arrays.asList(getResources().getStringArray(R.array.colors));
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
}