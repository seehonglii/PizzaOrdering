package nus.edu.sg.iss.app.PizzaOrdering.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import nus.edu.sg.iss.app.PizzaOrdering.model.Delivery;
import nus.edu.sg.iss.app.PizzaOrdering.model.Order;
import nus.edu.sg.iss.app.PizzaOrdering.model.Pizza;
import nus.edu.sg.iss.app.PizzaOrdering.repository.PizzaRepo;

@Service
public class PizzaSvc {

    @Autowired
    private PizzaRepo pizzaRepo;

    public static final String[] PIZZA_NAME = {"bella", "margherita", "marinara", "spianatacalabrese", "trioformaggio"};

    public static final String[] PIZZA_SIZE={"sm", "md", "lg"};
    
    //for frontend drop down 
    private final Set<String> pizzaName;
    private final Set<String> pizzaSize; 

    @Value("${revision.pizza.api.url}")
    private String restPizzaUrl;

    public PizzaSvc(){
        pizzaName = new HashSet<String>(Arrays.asList(PIZZA_NAME));
        pizzaSize = new HashSet<String>(Arrays.asList(PIZZA_SIZE));
    }

    public Optional<Order> getOrderByOrderId(String orderId){
        return pizzaRepo.get(orderId);
    }

    public Order createPizzaOrder(Pizza p, Delivery d){
        String orderId = UUID.randomUUID().toString().substring(0,8);
        Order o = new Order(p,d);
        o.setOrderId(orderId);
        return o;
    }

    public float calculateCost(Order o) {
        float total = 0f;
        switch(o.getPizzaName()){
            case "margherita": total+=22;
                break;
            case "trioformaggio": total+=25;
                break;
            case "bella", "marinara", "spianatacalabrese": total+=30;
                break;
        }

        switch(o.getSize()){
            case "md": total *= 1.2;
                break;
            case "lg": total *= 1.5;
                break;
        }

        total *= (o.getQuantity());
        if(o.isRush()){
            total += 2;            
        }
        o.setTotalCost(total);
        return total;
    }

    public List<ObjectError> validatePizzaOrder(Pizza p){
        List<ObjectError> errors = new LinkedList<>();
        FieldError error;

        if(!pizzaName.contains(p.getPizza().toLowerCase())){
            error = new FieldError("pizza", "pizza", "We do not have %s pizza".formatted(p.getPizza()));
            errors.add(error);
        }

        if(!pizzaSize.contains(p.getSize().toLowerCase())){
            error = new FieldError("pizza", "size", "We do not have %s pizza size".formatted(p.getSize()));
            errors.add(error);
        }

        return errors;
    }

    public Order savePizzaOrder(Pizza pizza, @Valid Delivery delivery) {
        Order o = createPizzaOrder(pizza, delivery);
        calculateCost(o);
        pizzaRepo.save(o);
        return o;
    }

    public Optional<Order> getOrderDetails(String orderId){
        String url = UriComponentsBuilder.fromUriString(this.restPizzaUrl + orderId)
                                        .toString(); 
        RequestEntity req = RequestEntity.get(url).build();

        RestTemplate template = new RestTemplate(); 
        ResponseEntity<String> resp = template.exchange(req, String.class);
        Order o = Order.create(resp.getBody());
        if(null == o){
            return Optional.empty();
        }
        return Optional.of(o);
    }

}
