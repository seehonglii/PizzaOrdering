package nus.edu.sg.iss.app.PizzaOrdering.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import nus.edu.sg.iss.app.PizzaOrdering.model.Delivery;
import nus.edu.sg.iss.app.PizzaOrdering.model.Order;
import nus.edu.sg.iss.app.PizzaOrdering.model.Pizza;
import nus.edu.sg.iss.app.PizzaOrdering.service.PizzaSvc;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PizzaController {
    
    @Autowired
    private PizzaSvc pizzaSvc;

    @GetMapping(path={"/", "/index.html"})
    public String getIndex(Model model, HttpSession sess) {
        sess.invalidate();
        model.addAttribute("pizza", new Pizza());
        return "index";
    }

    @PostMapping(path="/pizza")
    public String postPizza(Model model, HttpSession sess, @Valid Pizza pizza, BindingResult bindings){
        if(bindings.hasErrors()){
            return "index";
        }
        
        List<ObjectError> errors = pizzaSvc.validatePizzaOrder(pizza);
        if(!errors.isEmpty()){
            for(ObjectError e: errors){
                bindings.addError(e);
            }
            return "index";
        }

        sess.setAttribute("pizza", pizza);
        model.addAttribute("delivery", new Delivery());
        return "delivery";
    }

    @PostMapping(path = "/pizza/order")
    public String postPizzaOrder(Model model, HttpSession sess, @Valid Delivery delivery, BindingResult bindings){
        if(bindings.hasErrors()){
            return "delivery";
        }

        Pizza pizza = (Pizza)sess.getAttribute("pizza");
        Order o = pizzaSvc.savePizzaOrder(pizza,delivery);
        model.addAttribute("order", o);
        return "order";
    }  

    //call your own API
    @GetMapping(path="/pizza/details/{orderId}")
    public String getOrderDetails(Model model, @PathVariable String orderId){
        Optional<Order> o = pizzaSvc.getOrderDetails(orderId);
        model.addAttribute("order", o.get());
        return "order";
    }

}
