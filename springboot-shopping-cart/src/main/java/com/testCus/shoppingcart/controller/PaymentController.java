/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.testCus.shoppingcart.controller;

import com.testCus.shoppingcart.dto.OrderPaymentDTO;
import com.testCus.shoppingcart.dto.PaymentDTO;
import com.testCus.shoppingcart.model.OrderDetail;
import com.testCus.shoppingcart.model.Product;
import com.testCus.shoppingcart.repository.OrderRepository;
import com.testCus.shoppingcart.service.OrderService;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
 *
 * @author Mohamed
 */

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private OrderService orderService;

    public PaymentController() {
        this.orderService = new OrderService(new OrderRepository());
    }

    // Endpoint para procesar el pago de la orden
    @PostMapping
    public PaymentDTO processPayment(@RequestBody OrderPaymentDTO orderPaymentDTO) {
        // Verificar si hay una orden pendiente para el cliente
        Optional<OrderDetail> existingOrder = orderService.findPendingOrder(orderPaymentDTO.getCustomer().getCustomerId());

        if (!existingOrder.isPresent()) {
            throw new RuntimeException("No pending order found for the customer.");
        }

        OrderDetail order = existingOrder.get();

        // Validar que la orden tenga productos
        if (order.getProducts() == null || order.getProducts().isEmpty()) {
            throw new RuntimeException("Order does not have products.");
        }

        // Calcular el total de la orden sumando los precios de los productos
        double totalAmount = order.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();
        
        // Asignar el total a la orden
        order.setTotal(totalAmount);

        // Simulamos el proceso de pago
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setCustomerId(orderPaymentDTO.getCustomer().getCustomerId());
        paymentDTO.setAmount(totalAmount);
        paymentDTO.setStatus("Pending");

        // Simulación de pago exitoso
        paymentDTO.setStatus("Completed");

        // Cambiar el estado de la orden a "Completed"
        order.setStatus("Completed");

        // Guardar la orden después del procesamiento
        orderService.updateOrder(order);

        return paymentDTO;
    }
}


//
//@RestController
//@RequestMapping("/api/payments")
//public class PaymentController {
//
//    // Endpoint para procesar el pago de la orden
//    @PostMapping
//    public PaymentDTO processPayment(@RequestBody OrderPaymentDTO orderPaymentDTO) {
//        // Convertir cada ProductDTO en Product
//        List<Product> productList = orderPaymentDTO.getProducts().stream()
//                .map(this::convertDTOToProduct)  // Convertir cada ProductDTO a Product
//                .collect(Collectors.toList());
//
//        // Calcular el total de la orden sumando los precios de los productos
//        double totalAmount = productList.stream()
//                .mapToDouble(Product::getPrice)  // Obtener el precio de cada producto
//                .sum();  // Sumar los precios de todos los productos
//        
//        // Crear un nuevo objeto de orden (con datos del cliente)
//        OrderDetail order = new OrderDetail();
//        order.setCustomer(createCustomerFromDTO(orderPaymentDTO.getCustomer()));  // Asignamos los datos del cliente
//        order.setProducts(productList);  // Asignar la lista de productos convertidos
//        order.setTotal(totalAmount);  // Asignar el total calculado
//        order.setStatus("Pending");  // Estado inicial de la orden (pendiente)
//        
//        // Simulamos el proceso de pago
//        PaymentDTO paymentDTO = new PaymentDTO();
//        paymentDTO.setCustomerId(orderPaymentDTO.getCustomer().getCustomerId());
//        paymentDTO.setAmount(totalAmount);
//        paymentDTO.setStatus("Pending");  // Inicialmente "Pending"
//        
//        // Lógica para cambiar el estado a "Paid" (simulación)
//        // Esta lógica dependería de si el pago se completa con éxito
//        paymentDTO.setStatus("Completed");  // Simulamos un pago exitoso
//
//        // Cambiar el estado de la orden a "Completed"
//        order.setStatus("Completed");
//
//        // Devuelves la información de pago
//        return paymentDTO;
//    }
//
//    // Método para crear el cliente desde el DTO
//    private OrderDetail.Customer createCustomerFromDTO(OrderPaymentDTO.CustomerDTO customerDTO) {
//        OrderDetail.Customer customer = new OrderDetail.Customer();
//        customer.setCustomerId(customerDTO.getCustomerId());
//        customer.setFirstName(customerDTO.getFirstName());
//        customer.setLastName(customerDTO.getLastName());
//        customer.setEmail(customerDTO.getEmail());
//        customer.setPhone(customerDTO.getPhone());
//        customer.setAddress(createAddressFromDTO(customerDTO.getAddress()));
//        return customer;
//    }
//
//    // Método para crear la dirección del cliente desde el DTO
//    private OrderDetail.Customer.Address createAddressFromDTO(OrderPaymentDTO.CustomerDTO.AddressDTO addressDTO) {
//        OrderDetail.Customer.Address address = new OrderDetail.Customer.Address();
//        address.setStreet(addressDTO.getStreet());
//        address.setCity(addressDTO.getCity());
//        address.setZipCode(addressDTO.getZipCode());
//        return address;
//    }
//
//    // Método para convertir ProductDTO a Product
//    private Product convertDTOToProduct(ProductDTO productDTO) {
//        Product product = new Product();
//        product.setId(productDTO.getId());
//        product.setTitle(productDTO.getTitle());
//        product.setPrice(productDTO.getPrice());
//        product.setDescription(productDTO.getDescription());
//        product.setCategory(productDTO.getCategory());
//        product.setImage(productDTO.getImage());
//
//        // Convertir la valoración
//        Product.Rating rating = new Product.Rating();
//        rating.setRate(productDTO.getRating().getRate());
//        rating.setCount(productDTO.getRating().getCount());
//        product.setRating(rating);
//
//        return product;
//    }
//}



