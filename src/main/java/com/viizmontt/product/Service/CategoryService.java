package com.viizmontt.product.Service;

import com.viizmontt.product.Model.Category;
import com.viizmontt.product.Repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }
    
    public String deleteById(int id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            if (category.getProducts().isEmpty()) {
                categoryRepository.deleteById(id);
                return "Catery eliminada exitosamente";
            } else {
                return "No se puede eliminar la marca porque tiene productos asociados";
            }
        } else {
            return "Categoria no encontrada";
        }
    }
    
    public boolean existsByName(String name){
        return categoryRepository.findByName(name).isPresent();
    }
    
    public boolean existsByNameExcludingId(String name, Integer id){
        return categoryRepository.existsByNameAndIdNot(name, id);
    }
}
