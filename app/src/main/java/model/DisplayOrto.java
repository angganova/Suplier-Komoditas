package model;

/**
 * Created by A10 on 29/01/2017.
 */

public class DisplayOrto{

    public String name;
    public String qty;
    public String total;
    public String record_id;
    public String price;
    public String user;
    public String thumbnailUrl;

    private boolean selected;


    public DisplayOrto() {
    }

    public DisplayOrto(String user, String price, String thumbnailUrl, String name, String record_id,  String total, String qty) {
        this.name = name;
        this.total = total;
        this.qty = qty;
        this.record_id = record_id;
        this.price = price;
        this.user = user;
        this.thumbnailUrl = thumbnailUrl;

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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }


    public String getId() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setIsSelected(boolean selected) {
        this.selected = selected;
    }

}