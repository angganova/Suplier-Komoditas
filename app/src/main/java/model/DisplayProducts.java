package model;

/**
 * Created by A10 on 29/01/2017.
 */
import android.os.Parcel;
import android.os.Parcelable;

public class DisplayProducts implements Parcelable{
    public String id;
    public String name;
    public String thumbnailUrl;
    public String price1;
    public String price2;
    public String ket;
    public String unit;
    private boolean isSelected;
    public String type;
    public String sale;
    public String qty;
    public DisplayProducts() {
    }

    public DisplayProducts(String id, String name, String thumbnailUrl, String price1, String price2,
                             String ket, String qty, String unit, String type, String sale) {
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.price1 = price1;
        this.price2 = price2;
        this.ket = ket;
        this.unit = unit;
        this.type = type;
        this.sale = sale;
        this.qty = qty;
    }


    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
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
        dest.writeString(price1);
        dest.writeString(price2);
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

    // "De-parcel object
    public DisplayProducts(Parcel in) {
        id = in.readString();
        name = in.readString();
        thumbnailUrl = in.readString();
        price1 = in.readString();
        price2 = in.readString();
        ket = in.readString();
        unit = in.readString();
    }
}