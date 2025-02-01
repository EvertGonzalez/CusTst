/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.testCus.shoppingcart.service;
import com.testCus.shoppingcart.model.OrderDetail;
import com.testCus.shoppingcart.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Mohamed
 */
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Guardar una nueva orden
    public OrderDetail saveOrder(OrderDetail order) {
        return orderRepository.save(order);
    }

    // Modificar una orden existente
    public OrderDetail updateOrder(OrderDetail order) {
        if (orderRepository.existsById(order.getOrderId())) {
            return orderRepository.save(order);
        } else {
            throw new RuntimeException("Order not found with ID: " + order.getOrderId());
        }
    }

    // Eliminar una orden
    public void deleteOrder(int orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
    }

    // Verificar si existe una orden pendiente para un cliente
    public Optional<OrderDetail> findPendingOrder(int customerId) {
        return orderRepository.findByCustomerIdAndStatus(customerId, "Pending");
    }

    // Obtener todas las órdenes con un estado específico
    public List<OrderDetail> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);  // Filtra las órdenes por el estado proporcionado
    }
}


