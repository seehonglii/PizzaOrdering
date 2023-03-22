package nus.edu.sg.iss.app.PizzaOrdering.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import nus.edu.sg.iss.app.PizzaOrdering.model.Order;
import nus.edu.sg.iss.app.PizzaOrdering.service.PizzaSvc;

@RestController
@RequestMapping("/order")
public class PizzaRestController {
    @Autowired
    private PizzaSvc pizzasvc;
    
    @GetMapping(path="{orderId}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrder(@PathVariable String orderId){
        Optional<Order> op = pizzasvc.getOrderByOrderId(orderId);
        if(op.isEmpty()){
            JsonObject error = Json.createObjectBuilder()
                .add("message", "Order %s not found".formatted(orderId))
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(error.toString());
        }
        return ResponseEntity.ok(op.get().toJSON().toString());
    }
}