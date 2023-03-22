package nus.edu.sg.iss.app.PizzaOrdering.model;

import java.io.Serializable;

import jakarta.json.JsonObject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Delivery implements Serializable{

    public static final long serialVersionUID=1L;
    
    @NotNull(message="Please enter your name")
    @Size(min=3, message="Minimum 3 characters")
    private String name;

    @NotNull(message="Please enter your address")
    @NotEmpty(message="this field is mandatory")
    private String address;

    @NotNull(message="this field is mandatory")
    @Pattern(regexp = "^[0-9]{8,}$", message="must be a valid phone number")
    private String phone;

    private boolean rush=false; 

    private String comment;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public boolean isRush() {
        return rush;
    }
    public void setRush(boolean rush) {
        this.rush = rush;
    } 
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString(){
        return "name: " + this.name + " address: " + this.address + " phone: " + this.phone + " rush: " + this.rush + "comment: " + this.comment;
    }
    
    public static Delivery create(JsonObject o){
        Delivery d = new Delivery();
        d.setName(o.getString("name"));
        d.setAddress(o.getString("address"));
        d.setPhone(o.getString("phone"));
        d.setRush(o.getBoolean("rush"));
        d.setComment(o.getString("comment"));
        return d;
    }
    
}
