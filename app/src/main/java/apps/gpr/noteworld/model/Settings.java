package apps.gpr.noteworld.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity
public class Settings implements Parcelable{

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String mode;
    private String passcode;
    private String first_launch;
    private String last_login;
    private String view_type;
    private String created_date;
    private String modified_date;

    public Settings() {
    }

    public Settings(Parcel readParcel) {
        id = readParcel.readInt();
        mode = readParcel.readString();
        passcode = readParcel.readString();
        first_launch = readParcel.readString();
        last_login = readParcel.readString();
        view_type = readParcel.readString();
        created_date = readParcel.readString();
        modified_date = readParcel.readString();
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getFirst_launch() {
        return first_launch;
    }

    public void setFirst_launch(String first_launch) {
        this.first_launch = first_launch;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getView_type() {
        return view_type;
    }

    public void setView_type(String view_type) {
        this.view_type = view_type;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "id=" + id +
                ", mode='" + mode + '\'' +
                ", passcode='" + passcode + '\'' +
                ", first_launch='" + first_launch + '\'' +
                ", last_login='" + last_login + '\'' +
                ", view_type='" + view_type + '\'' +
                ", created_date='" + created_date + '\'' +
                ", modified_date='" + modified_date + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(mode);
        parcel.writeString(passcode);
        parcel.writeString(first_launch);
        parcel.writeString(last_login);
        parcel.writeString(view_type);
        parcel.writeString(created_date);
        parcel.writeString(modified_date);
    }

    public static final Parcelable.Creator<Settings> CREATOR = new Parcelable.Creator<Settings>(){
        @Override
        public Settings createFromParcel(Parcel parcel) {
            return new Settings(parcel);
        }

        @Override
        public Settings[] newArray(int i) {
            return new Settings[i];
        }
    };
}
