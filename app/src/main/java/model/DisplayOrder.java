package model;

/**
 * Created by A10 on 29/01/2017.
 */
import android.os.Parcel;
import android.os.Parcelable;

public class DisplayOrder implements Parcelable {
    public String id;
    public String name;
    public String thumbnailUrl;
    public String price1;
    public String price2;
    public String ket;
    public String qty;
    public String unit;
    public String total;
    private boolean selected;

    public String price;
    public String user;

    public DisplayOrder() {
    }

    public DisplayOrder(String user, String price, String id,String name, String thumbnailUrl, String price1, String price2,
                             String ket, String qty, String unit, String total) {
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.price1 = price1;
        this.price2 = price2;
        this.ket = ket;
        this.qty = qty;
        this.unit = unit;
        this.total = total;
        this.price = price;
        this.user = user;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user){
        this.user = user;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
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

    public void setKet(String ket) {
        this.ket = ket;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(ket);
        dest.writeString(name);
        dest.writeString(qty);
        dest.writeString(price1);
        dest.writeString(price2);
        dest.writeString(total);

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
    public DisplayOrder(Parcel in) {
        id = in.readString();
        name = in.readString();
        thumbnailUrl = in.readString();
        price1 = in.readString();
        price2 = in.readString();
        ket = in.readString();
        unit = in.readString();
    }
}