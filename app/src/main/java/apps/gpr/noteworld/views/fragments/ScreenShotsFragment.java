package apps.gpr.noteworld.views.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.adapters.ImagesAdapter;
import apps.gpr.noteworld.utils.Const;
import apps.gpr.noteworld.utils.PrefUtils;

public class ScreenShotsFragment extends Fragment {

    GridView gridView;

    public ScreenShotsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen_shots, container, false);

        TextView theme_desc = view.findViewById(R.id.theme_desc);

        if (PrefUtils.getModePreference(getActivity()).equals(Const.SCREEN_DARK)){
            theme_desc.setTextColor(getResources().getColor(R.color.colorWhite));
            theme_desc.setText(getResources().getString(R.string.str_dark_theme_screens));
        }else {
            theme_desc.setTextColor(getResources().getColor(R.color.colorBlack));
            theme_desc.setText(getResources().getString(R.string.str_light_theme_screens));
        }

        gridView = view.findViewById(R.id.screen_shots_grid);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().findViewById(R.id.progressBar).setVisibility(View.GONE);
                gridView.setAdapter(new ImagesAdapter(getActivity(), PrefUtils.getModePreference(getActivity())));
            }
        }, 2000);

        return view;
    }
}

