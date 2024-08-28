package com.viizmontt.product.Service;

import com.viizmontt.product.Model.Brand;
import com.viizmontt.product.Repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public Brand findById(int id) {
        return brandRepository.findById(id).orElse(null);
    }

    public void save(Brand brand) {
        brandRepository.save(brand);
    }

    public String deleteById(int id) {
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        if (optionalBrand.isPresent()) {
            Brand brand = optionalBrand.get();
            if (brand.getProducts().isEmpty()) {
                brandRepository.deleteById(id);
                return "Marca eliminada exitosamente";
            } else {
                return "No se puede eliminar la marca porque tiene productos asociados";
            }
        } else {
            return "Marca no encontrada";
        }
    }

    public boolean existsByName(String name) {
        return brandRepository.findByName(name).isPresent();
    }

    public boolean existsByNameExcludingId(String name, Integer id) {
        return brandRepository.existsByNameAndIdNot(name, id);
    }
}
