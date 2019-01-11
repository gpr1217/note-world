package apps.gpr.noteworld.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.utils.Const;
import apps.gpr.noteworld.utils.PrefUtils;
import apps.gpr.noteworld.views.ScreenModeActivity;
import apps.gpr.noteworld.views.SettingsActivity;

public class ModeListFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modes_list, container, false);
        final Switch light = view.findViewById(R.id.screen_mode_light);
        light.setVisibility(View.GONE);
        final Switch dark = view.findViewById(R.id.screen_mode_dark);

        if (getMode(getActivity()).equals(Const.SCREEN_DARK)){
            dark.setChecked(true);
        }else {
            dark.setChecked(false);
        }

        dark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (dark.isChecked()) {
                    dark.setChecked(true);
                    setMode(Const.SCREEN_DARK, getActivity());
                }else {
                    dark.setChecked(false);
                    setMode(Const.SCREEN_LIGHT, getActivity());
                }
                Intent intent = new Intent(getActivity(), ScreenModeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void setMode(String mode, Context activity){
        PrefUtils.setModePreference(mode,activity);
    }

    private String getMode(Context context){
        return PrefUtils.getModePreference(context);
    }

}
