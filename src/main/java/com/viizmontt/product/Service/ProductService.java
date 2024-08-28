package com.viizmontt.product.Service;

import com.viizmontt.product.Model.Product;
import com.viizmontt.product.Repository.ProductRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void deleteById(int id) {
        productRepository.deleteById(id);
    }
    
    public boolean existsByName(String name) {
        return productRepository.findByName(name).isPresent();
    }
    
    public boolean existsByNameExcludingId(String name, Integer id) {
        return productRepository.existsByNameAndIdNot(name, id);
    }
    
}