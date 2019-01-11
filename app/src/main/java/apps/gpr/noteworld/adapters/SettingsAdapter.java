package apps.gpr.noteworld.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.utils.CommonUtilities;
import apps.gpr.noteworld.utils.Const;
import apps.gpr.noteworld.utils.PrefUtils;
import apps.gpr.noteworld.views.SettingsActivity;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>{

    private final List<String> settingsList;
    Context _context;

    public class SettingsViewHolder extends RecyclerView.ViewHolder{

        final TextView itemText;
        final ImageView rightArrow;
        final Switch aSwitch;

        SettingsViewHolder(View itemView) {
            super(itemView);

            itemText = itemView.findViewById(R.id.settings_item);
            rightArrow = itemView.findViewById(R.id.arrow_item);
            aSwitch = itemView.findViewById(R.id.lock_switch_item);
        }
    }

    public SettingsAdapter(Context context, List<String> settingsList, SettingsAdapterListener listener) {
        this._context = context;
        this.settingsList = settingsList;
    }

    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_list_row, parent, false);

        return new SettingsViewHolder(view);
    }

    View lastItem = null;
    @Override
    public void onBindViewHolder(@NonNull final SettingsAdapter.SettingsViewHolder holder, final int position) {
        final String selectedRow = settingsList.get(position);

        holder.itemText.setText(selectedRow);
        holder.rightArrow.setVisibility(View.VISIBLE);
        holder.aSwitch.setVisibility(View.GONE);

        if (PrefUtils.getModePreference(_context).equals(Const.SCREEN_DARK)){
            holder.itemText.setTextColor(Color.WHITE);
            holder.rightArrow.setImageResource(R.drawable.outline_navigate_next_white_24);
        }else {
            holder.itemText.setTextColor(Color.BLACK);
            holder.rightArrow.setImageResource(R.drawable.outline_keyboard_arrow_right_black_24);
        }
    }

    @Override
    public int getItemCount() {
        return settingsList.size();
    }

    public interface SettingsAdapterListener{
    }
}
