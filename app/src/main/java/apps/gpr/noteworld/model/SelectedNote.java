package apps.gpr.noteworld.model;

public class SelectedNote {

    private int id;
    private String editedNote;
    private String changedColor;

    public SelectedNote(String editedNote, String changedColor) {
        this.editedNote = editedNote;
        this.changedColor = changedColor;
    }

    public SelectedNote() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEditedNote() {
        return editedNote;
    }

    public void setEditedNote(String editedNote) {
        this.editedNote = editedNote;
    }

    public String getChangedColor() {
        return changedColor;
    }

    public void setChangedColor(String changedColor) {
        this.changedColor = changedColor;
    }

    @Override
    public String toString() {
        return "SelectedNote{" +
                "editedNote='" + editedNote + '\'' +
                ", changedColor='" + changedColor + '\'' +
                '}';
    }
}
