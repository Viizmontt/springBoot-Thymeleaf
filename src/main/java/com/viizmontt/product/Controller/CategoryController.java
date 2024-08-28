package com.viizmontt.product.Controller;

import com.viizmontt.product.Model.Category;
import com.viizmontt.product.Model.CategoryDTO;
import com.viizmontt.product.Service.CategoryService;
import jakarta.validation.Valid;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    
    private static final Logger logger = Logger.getLogger(BrandController.class.getName());
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "category/categories";
    }

    @GetMapping("/create")
    public String createCategoryForm(Model model) {
        model.addAttribute("categoryDTO", new CategoryDTO());
        return "category/category";
    }
    
    @PostMapping("/save")
    public String saveBrand(@Valid @ModelAttribute("categoryDTO") CategoryDTO categoryDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "brand/brand";
        }
        if (categoryService.existsByName(categoryDTO.getName())) {
            result.rejectValue("name", "error.brandDTO", "La marca ya existe.");
            logger.info("No se puede agregar la marca. Existe ya una marca con el mismo nombre");
            return "brand/brand";
        }
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        categoryService.save(category);
        redirectAttributes.addFlashAttribute("message", "Marca agregada exitosamente.");
        redirectAttributes.addFlashAttribute("color", "alert alert-primary mt-3");
        return "redirect:/brands";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable("id") int id, Model model) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return "redirect:/categories";
        }
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        model.addAttribute("categoryDTO", categoryDTO);
        return "category/category";
    }
    
    @PostMapping("/{id}")
    public String updateCategory(@PathVariable int id, @Valid @ModelAttribute CategoryDTO categoryDTO, BindingResult result, RedirectAttributes redirectAttributes){
        if (result.hasErrors()) {
            logger.info("Vacio");
            return "category/category";
        }
        if (categoryService.existsByNameExcludingId(categoryDTO.getName(), id)) {
            result.rejectValue("name", "error.categoryDTO", "Ya existe una Categoria con ese nombre");
            logger.info("Nombre repetido");
            return "category/category";
        }
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        categoryService.save(category);
        logger.info("SIN ERRORES");
        redirectAttributes.addFlashAttribute("message", "Marca actualizada exitosamente.");
        redirectAttributes.addFlashAttribute("color", "alert alert-warning mt-3");
        return "redirect:/categories";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        String message = categoryService.deleteById(id);
        String messageType = "warning";
        if (message.equals("Categoria eliminada exitosamente")) {
            messageType = "alert alert-info mt-3";
        } else if (message.equals("No se puede eliminar la marca porque tiene productos asociados")) {
            messageType = "alert alert-danger mt-3";
        }
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("color", messageType);
        return "redirect:/categories";
    }
}