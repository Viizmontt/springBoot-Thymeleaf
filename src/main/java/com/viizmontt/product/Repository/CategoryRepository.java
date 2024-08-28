package com.viizmontt.product.Repository;

import com.viizmontt.product.Model.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);
}
