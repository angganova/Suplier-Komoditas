package model;

/**
 * Created by A10 on 29/01/2017.
 */
public class DisplaySyarat{
    public String id;
    public String name;
    public DisplaySyarat() {
    }

    public DisplaySyarat(String id, String name) {
        this.id = id;
        this.name = name;

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

}