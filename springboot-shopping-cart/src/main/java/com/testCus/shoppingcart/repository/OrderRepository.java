/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.testCus.shoppingcart.repository;

import com.testCus.shoppingcart.model.OrderDetail;

/**
 *
 * @author Mohamed
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderRepository {

    private static List<OrderDetail> orders = new ArrayList<>();

    // Guardar una orden
    public OrderDetail save(OrderDetail order) {
        orders.add(order);
        return order;
    }

    // Verificar si existe una orden con un ID
    public boolean existsById(int orderId) {
        return orders.stream().anyMatch(order -> order.getOrderId() == orderId);
    }

    // Eliminar una orden
    public void deleteById(int orderId) {
        orders.removeIf(order -> order.getOrderId() == orderId);
    }

    // Buscar una orden pendiente de un cliente específico
    public Optional<OrderDetail> findByCustomerIdAndStatus(int customerId, String status) {
        return orders.stream()
                .filter(order -> order.getCustomer().getCustomerId() == customerId && order.getStatus().equals(status))
                .findFirst();
    }

    // Obtener todas las órdenes con un estado específico
    public List<OrderDetail> findByStatus(String status) {
        return orders.stream()
                .filter(order -> order.getStatus().equals(status))  // Filtramos por estado
                .collect(Collectors.toList());  // Devolvemos la lista filtrada
    }
}

