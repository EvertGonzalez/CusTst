/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.testCus.shoppingcart.service;

import com.testCus.shoppingcart.dto.ProductDTO;
import com.testCus.shoppingcart.exception.ProductNotFoundException;
import com.testCus.shoppingcart.model.Product;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Mohamed
 */

@Service
public class ProductService {

    private static final String PRODUCT_API_URL = "https://fakestoreapi.com/products";

public List<ProductDTO> getProductDetails(Integer id) {
    RestTemplate restTemplate = new RestTemplate();
    
    // Si el id es nulo o 0, obtenemos todos los productos
    if (id == null || id == 0) {
        // Obtener todos los productos
        ResponseEntity<List<ProductDTO>> response = restTemplate.exchange(
            PRODUCT_API_URL, // URL de la API externa
            HttpMethod.GET,  // Método GET
            null,  // Sin cuerpo para GET
            new ParameterizedTypeReference<List<ProductDTO>>() {} // Especificar que la respuesta es una lista de ProductDTO
        );

        List<ProductDTO> products = response.getBody();

        if (products == null || products.isEmpty()) {
            throw new ProductNotFoundException("No products found.");
        }

        return products;  // Retornar todos los productos como lista
    } else {
        // Si el id no es nulo ni 0, obtenemos un producto específico
        String url = PRODUCT_API_URL + "/" + id;
        ResponseEntity<ProductDTO> response = restTemplate.exchange(
            url,  // URL con el id específico
            HttpMethod.GET,  // Método GET
            null,  // Sin cuerpo para GET
            ProductDTO.class // Especificamos que esperamos una respuesta de tipo ProductDTO
        );

        ProductDTO product = response.getBody();

        if (product == null) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }

        return List.of(product);  // Si solo hay un producto, lo retornamos como una lista
    }
}

//    public List<ProductDTO> getProductDetails(int id) {
//        RestTemplate restTemplate = new RestTemplate();
//        List<ProductDTO>  product = (List<ProductDTO>) restTemplate.getForObject(PRODUCT_API_URL + "/" + id, ProductDTO.class);
//        if (product == null) {
//            throw new ProductNotFoundException("Product not found with ID: " + id);
//        }
//        return product;
//    }
}
 


