package model;

/**
 * Created by A10 on 29/01/2017.
 */
import android.os.Parcel;
import android.os.Parcelable;

public class DisplayPoin implements Parcelable{
    public String id;
    public String name;
    public String thumbnailUrl;
    public String poin;
    public String ket;
    public String unit;
    private boolean isSelected;
    public String username;

    public DisplayPoin() {
    }

    public DisplayPoin(String id, String name, String thumbnailUrl, String poin,
                           String ket, String unit) {
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.poin = poin;
        this.ket = ket;
        this.unit = unit;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPoin() {
        return poin;
    }

    public void setPoin(String poin) {
        this.poin = poin;
    }

    public String getKet() {
        return ket;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(thumbnailUrl);
        dest.writeString(poin);
        dest.writeString(ket);
        dest.writeString(unit);

    }
    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public DisplayOrder createFromParcel(Parcel in) {
            return new DisplayOrder(in);
        }

        public DisplayOrder[] newArray(int size) {
            return new DisplayOrder[size];
        }
    };

}