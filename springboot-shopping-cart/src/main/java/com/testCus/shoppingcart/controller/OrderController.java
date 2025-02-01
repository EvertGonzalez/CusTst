/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.testCus.shoppingcart.controller;

import com.testCus.shoppingcart.dto.OrderDTO;
import com.testCus.shoppingcart.dto.OrderPaymentDTO;
import com.testCus.shoppingcart.dto.ProductDTO;
import com.testCus.shoppingcart.model.Order;
import com.testCus.shoppingcart.model.OrderDetail;
import com.testCus.shoppingcart.model.Product;
import com.testCus.shoppingcart.repository.OrderRepository;
import com.testCus.shoppingcart.service.OrderService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 *
 * @author Mohamed
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController() {
        this.orderService = new OrderService(new OrderRepository());
    }

    // Endpoint para crear una nueva orden
    @PostMapping
    public OrderDetail createOrder(@RequestBody OrderPaymentDTO orderPaymentDTO) {
        // Convertir cada ProductDTO a Product
        List<Product> products = orderPaymentDTO.getProducts().stream()
                .map(this::convertDTOToProduct)  // Convertir cada ProductDTO a Product
                .collect(Collectors.toList());

        // Crear la orden
        OrderDetail order = new OrderDetail();
        order.setCustomer(createCustomerFromDTO(orderPaymentDTO.getCustomer()));  // Asignamos los datos del cliente
        order.setProducts(products);  // Asignar la lista de productos convertidos
        order.setTotal(0);  // Total inicial (puede actualizarse después)
        order.setStatus("Pending");  // Estado inicial de la orden (pendiente)
        
        // Guardar la nueva orden
        return orderService.saveOrder(order);
    }

    // Método para obtener todas las órdenes con un estado específico (Pending o Completed)
    @GetMapping("/status/{status}")
    public List<OrderDetail> getOrdersByStatus(@PathVariable String status) {
        // Obtener las órdenes según el estado
        List<OrderDetail> orders = orderService.getOrdersByStatus(status);

        if (orders.isEmpty()) {
            throw new RuntimeException("No orders found with status: " + status);
        }

        return orders;
    }

    // Método para crear el cliente desde el DTO
    private OrderDetail.Customer createCustomerFromDTO(OrderPaymentDTO.CustomerDTO customerDTO) {
        OrderDetail.Customer customer = new OrderDetail.Customer();
        customer.setCustomerId(customerDTO.getCustomerId());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(createAddressFromDTO(customerDTO.getAddress()));
        return customer;
    }

    // Método para crear la dirección del cliente desde el DTO
    private OrderDetail.Customer.Address createAddressFromDTO(OrderPaymentDTO.CustomerDTO.AddressDTO addressDTO) {
        OrderDetail.Customer.Address address = new OrderDetail.Customer.Address();
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setZipCode(addressDTO.getZipCode());
        return address;
    }

    // Método para convertir ProductDTO a Product
    private Product convertDTOToProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setTitle(productDTO.getTitle());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCategory(productDTO.getCategory());
        product.setImage(productDTO.getImage());

        // Convertir la valoración
        Product.Rating rating = new Product.Rating();
        rating.setRate(productDTO.getRating().getRate());
        rating.setCount(productDTO.getRating().getCount());
        product.setRating(rating);

        return product;
    }
}

