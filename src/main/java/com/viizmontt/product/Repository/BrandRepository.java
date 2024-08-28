package com.viizmontt.product.Repository;

import com.viizmontt.product.Model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

    Optional<Brand> findByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);
}