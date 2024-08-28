package com.viizmontt.product.Repository;

import com.viizmontt.product.Model.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);
}
