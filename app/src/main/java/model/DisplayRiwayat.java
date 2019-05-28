package model;

/**
 * Created by A10 on 29/01/2017.
 */

public class DisplayRiwayat{

    public String status;
    public String created;
    public String total;

    public DisplayRiwayat() {
    }

    public DisplayRiwayat(String created,  String status, String total) {
        this.created = created;
        this.status = status;
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}