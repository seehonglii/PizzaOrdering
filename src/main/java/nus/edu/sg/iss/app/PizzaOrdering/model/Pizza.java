package nus.edu.sg.iss.app.PizzaOrdering.model;

import java.io.Serializable;

import jakarta.json.JsonObject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Pizza implements Serializable{
    
    public static final long serialVersionUID=1L;

    @NotNull(message="Please chhose a pizza type")
    private String pizza; 

    @NotNull(message="Please select a pizza size")
    private String size; 

    @Min(value = 1, message="minimum order quantity is 1")
    @Max(value = 10, message = "maximum order quantity is 10")
    private Integer quantity;

    public String getPizza() {
        return pizza;
    }
    public void setPizza(String pizza) {
        this.pizza = pizza;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    } 
    //for debug purpose, write a toString method
    @Override
    public String toString(){
        return "pizza=" + this.getPizza() + " size=" + this.getSize() + " quantity=" + this.getQuantity();
    }

    // mainly to take in json object 
    public static Pizza create(JsonObject o){
        Pizza pizza = new Pizza(); 
        pizza.setPizza(o.getString("pizza"));
        pizza.setSize(o.getString("size"));
        pizza.setQuantity(o.getInt("quantity"));

        return pizza;        
    }
}
