package nus.edu.sg.iss.app.PizzaOrdering.model;

import java.io.Serializable;
import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Order implements Serializable{
    
    public static final long serialVersionUID=1L;

    private float totalCost = -1; //-1 because of the decimal
    private String orderId; 
    private Pizza pizza; 
    private Delivery delivery;

    //constructor for order. pass in pizza object and delivery object 
    public Order(Pizza pizza, Delivery delivery) {
        this.pizza = pizza;
        this.delivery = delivery;
    }

    public float getTotalCost() {
        return totalCost;
    }
    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public Pizza getPizza() {
        return pizza;
    }
    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }
    public Delivery getDelivery() {
        return delivery;
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    //provide a helper method to include, if it is not rush, to exclude the additional cost. 
    public float getPizzaCost(){
        return this.isRush()? getTotalCost()-2 : this.getTotalCost();
    }

    public String getName() {return this.getDelivery().getName();}
    public String getPizzaName() {return this.getPizza().getPizza();}
    public String getAddress() {return this.getDelivery().getAddress();}
    public String getPhone() {return this.getDelivery().getPhone();}
    public boolean isRush() {return this.getDelivery().isRush();}
    public String getComment() {return this.getDelivery().getComment();}
    public String getSize() {return this.getPizza().getSize();}
    public int getQuantity() {return this.getPizza().getQuantity();}

    //this is the main object, we have to create the json string here to bring in the details
    public static JsonObject toJOSN(String json){
        JsonReader r = Json.createReader(new StringReader(json)); 
        return r.readObject(); // reader read object, this case, JsonObject is used. 
    }

    public static Order create(String jsonStr){
        JsonObject o = toJSON(jsonStr); 
        Pizza p = Pizza.create(o);
        Delivery d = Delivery.create(o); 
        Order ord = new Order(p,d);
        ord.setOrderId(o.getString("orderId"));
        ord.setTotalCost((float)o.getJsonNumber("total").doubleValue());
        return ord;
    }

    private static JsonObject toJSON(String jsonStr) {
        return null;
    }

    public JsonObject toJSON(){ //pass JsonObject to JSON
        return Json.createObjectBuilder()
                    .add("orderId", this.orderId)
                    .add("name", this.getName())
                    .add("address", this.getAddress())
                    .add("phone", this.getPhone())
                    .add("rush", this.isRush())
                    .add("comment", this.getComment())
                    .add("pizza", this.getPizzaName())
                    .add("size", this.getSize())
                    .add("quantity", this.getQuantity())
                    .add("total", this.getTotalCost())
                    .build();
    } 

}
