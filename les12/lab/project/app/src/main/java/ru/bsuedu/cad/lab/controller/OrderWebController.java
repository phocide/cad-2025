package ru.bsuedu.cad.lab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Controller
@RequestMapping("/orders")
public class OrderWebController {
    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderWebController(OrderService orderService, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("statuses", List.of("NEW", "SHIPPED", "DELIVERED", "CANCELLED"));
        model.addAttribute("products", productRepository.findAll());
        return "order-form";
    }

    @PostMapping
    public String createOrder(@ModelAttribute Order order, @RequestParam("productIds") List<Integer> productIds, RedirectAttributes redirectAttributes) {
        List<OrderDetail> details = new ArrayList<>();
        double total = 0.0;
        for (Integer pid : productIds) {
            Product product = productRepository.findById(pid).orElse(null);
            if (product != null) {
                OrderDetail detail = new OrderDetail();
                detail.setProduct(product);
                detail.setQuantity(1);
                detail.setPrice(product.getPrice());
                detail.setOrder(order);
                details.add(detail);
                total += product.getPrice();
            }
        }
        order.setOrderDetails(details);
        order.setTotalPrice(total);
        orderService.save(order);
        redirectAttributes.addFlashAttribute("success", "Заказ успешно создан!");
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Order> order = orderService.findById(id);
        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            model.addAttribute("customers", customerRepository.findAll());
            model.addAttribute("statuses", List.of("NEW", "SHIPPED", "DELIVERED", "CANCELLED"));
            model.addAttribute("products", productRepository.findAll());
            return "order-form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Заказ не найден");
            return "redirect:/orders";
        }
    }

    @PostMapping("/{id}")
    public String updateOrder(@PathVariable Integer id, @ModelAttribute Order order, @RequestParam("productIds") List<Integer> productIds, RedirectAttributes redirectAttributes) {
        if (!orderService.existsById(id)) {
            redirectAttributes.addFlashAttribute("error", "Заказ не найден");
            return "redirect:/orders";
        }
        order.setOrderId(id);
        List<OrderDetail> details = new ArrayList<>();
        double total = 0.0;
        for (Integer pid : productIds) {
            Product product = productRepository.findById(pid).orElse(null);
            if (product != null) {
                OrderDetail detail = new OrderDetail();
                detail.setProduct(product);
                detail.setQuantity(1);
                detail.setPrice(product.getPrice());
                detail.setOrder(order);
                details.add(detail);
                total += product.getPrice();
            }
        }
        order.setOrderDetails(details);
        order.setTotalPrice(total);
        orderService.save(order);
        redirectAttributes.addFlashAttribute("success", "Заказ обновлён!");
        return "redirect:/orders";
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        if (!orderService.existsById(id)) {
            redirectAttributes.addFlashAttribute("error", "Заказ не найден");
        } else {
            orderService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Заказ удалён!");
        }
        return "redirect:/orders";
    }
} 