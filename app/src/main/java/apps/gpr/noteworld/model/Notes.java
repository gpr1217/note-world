package apps.gpr.noteworld.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity
public class Notes implements Parcelable{

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String note;
    private String noteType;
    private String color;
    private String status;
    private String timeStamp;
    private String modifiedTimeStamp;

    public Notes() {
    }

    public Notes(Parcel readParcel) {
        id = readParcel.readInt();
        note = readParcel.readString();
        status = readParcel.readString();
        timeStamp = readParcel.readString();
        modifiedTimeStamp = readParcel.readString();
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getModifiedTimeStamp() {
        return modifiedTimeStamp;
    }

    public void setModifiedTimeStamp(String modifiedTimeStamp) {
        this.modifiedTimeStamp = modifiedTimeStamp;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "id=" + id +
                ", note='" + note + '\'' +
                ", noteType='" + noteType + '\'' +
                ", color='" + color + '\'' +
                ", status='" + status + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", modifiedTimeStamp='" + modifiedTimeStamp + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(note);
        parcel.writeString(status);
        parcel.writeString(timeStamp);
        parcel.writeString(modifiedTimeStamp);
    }

    public static final Parcelable.Creator<Notes> CREATOR = new Parcelable.Creator<Notes>(){
        @Override
        public Notes createFromParcel(Parcel parcel) {
            return new Notes(parcel);
        }

        @Override
        public Notes[] newArray(int i) {
            return new Notes[i];
        }
    };
}
