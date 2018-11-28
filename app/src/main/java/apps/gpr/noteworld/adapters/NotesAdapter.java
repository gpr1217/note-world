package apps.gpr.noteworld.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.model.Notes;
import apps.gpr.noteworld.utils.CommonUtilities;
import apps.gpr.noteworld.utils.Const;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NViewHolder> implements Filterable {

    private Context _c;
    private List<Notes> noteList;
    private List<Notes> noteListFiltered;
    private NotesAdapterListener listener;

    public NotesAdapter(List<Notes> notesList) {
        this.noteList = notesList;
    }

    public class NViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public TextView timestamp;

        public NViewHolder(View itemView) {
            super(itemView);

            note = itemView.findViewById(R.id.note);
            timestamp = itemView.findViewById(R.id.timestamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNoteSelected(noteListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public NotesAdapter(Context context, List<Notes> notes, NotesAdapterListener listener) {
        this._c = context;
        this.listener = listener;
        this.noteList = notes;
        this.noteListFiltered = notes;
    }

    public void updateData(List<Notes> notes){
        noteList = notes;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row, parent, false);
        return new NViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NViewHolder holder, int position) {
        Notes note = noteListFiltered.get(position);
        holder.note.setText(note.getNote());

        CommonUtilities.log("ModifiedDate",note.getModifiedTimeStamp());
        if (note.getModifiedTimeStamp() != null)
            holder.timestamp.setText(CommonUtilities.formatDate(note.getModifiedTimeStamp(), Const.FORMAT_LIST_ROW_DATE_TIME));
        else
            holder.timestamp.setText(CommonUtilities.formatDate(note.getTimeStamp(), Const.FORMAT_LIST_ROW_DATE_TIME));

    }

    private String formatDatetime(String date){
        String formatDate = "";
        try {
            String day = CommonUtilities.getNoteDateTimeFormat(Const.FORMAT_DEFAULT_DATE, date);
            if (day != null) {
                if (day.equalsIgnoreCase("Today"))
                    formatDate = day + ", " + CommonUtilities.formatDate(date, Const.FORMAT_h_mm_a);
            }
            else
               formatDate = CommonUtilities.formatDate(date, Const.FORMAT_LIST_ROW_DATE_TIME);

            return formatDate;
        }catch (Exception e){
            e.printStackTrace();
        }
        return formatDate;
    }

    @Override
    public int getItemCount() {
        return noteListFiltered.size();
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                CommonUtilities.log("SearchQuery::getFilter-charString",charString);

                for (Notes n : noteListFiltered){
                    CommonUtilities.log("Before-",n.getNote());
                }

                if (charString.isEmpty()){
                    noteListFiltered = noteList;
                }else {
                    List<Notes> filteredList = new ArrayList<>();

                    for (Notes n : noteList){
                        String note = n.getNote().toLowerCase();
                        CommonUtilities.log("contains", String.valueOf(note.contains(charString.toLowerCase())));
                        if (n.getNote().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(n);
                        }
                    }
                    for (Notes n : filteredList){
                        CommonUtilities.log("filteredList-",n.getNote());
                    }
                    noteListFiltered = filteredList;
                    for (Notes n : noteListFiltered){
                        CommonUtilities.log("after-",n.getNote());
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = noteListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                noteListFiltered = (List<Notes>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface NotesAdapterListener{
        void onNoteSelected(Notes note);
    }
}
