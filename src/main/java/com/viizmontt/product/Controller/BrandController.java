package com.viizmontt.product.Controller;

import com.viizmontt.product.Model.Brand;
import com.viizmontt.product.Model.BrandDTO;
import com.viizmontt.product.Service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/brands")
public class BrandController {

    private static final Logger logger = Logger.getLogger(BrandController.class.getName());

    @Autowired
    private BrandService brandService;

    @GetMapping
    public String listBrands(Model model) {
        model.addAttribute("brands", brandService.findAll());
        return "brand/brands";
    }

    @GetMapping("/create")
    public String createBrandForm(Model model) {
        model.addAttribute("brandDTO", new BrandDTO());
        return "brand/brand";
    }

    @PostMapping("/save")
    public String saveBrand(@Valid @ModelAttribute("brandDTO") BrandDTO brandDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "brand/brand";
        }
        if (brandService.existsByName(brandDTO.getName())) {
            result.rejectValue("name", "error.brandDTO", "La marca ya existe.");
            logger.info("No se puede agregar la marca. Existe ya una marca con el mismo nombre");
            return "brand/brand";
        }
        Brand brand = new Brand();
        brand.setId(brandDTO.getId());
        brand.setName(brandDTO.getName());
        brand.setDescription(brandDTO.getDescription());
        brandService.save(brand);
        redirectAttributes.addFlashAttribute("message", "Marca agregada exitosamente.");
        redirectAttributes.addFlashAttribute("color", "alert alert-primary mt-3");
        return "redirect:/brands";
    }

    @GetMapping("/edit/{id}")
    public String editBrandForm(@PathVariable("id") int id, Model model) {
        Brand brand = brandService.findById(id);
        if (brand == null) {
            return "redirect:/brands";
        }
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(brand.getId());
        brandDTO.setName(brand.getName());
        brandDTO.setDescription(brand.getDescription());
        model.addAttribute("brandDTO", brandDTO);
        return "brand/brand";
    }

    @PostMapping("/{id}")
    public String updateBrand(@PathVariable int id, @Valid @ModelAttribute BrandDTO brandDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "brand/brand";
        }
        if (brandService.existsByNameExcludingId(brandDTO.getName(), id)) {
            result.rejectValue("name", "error.brandDTO", "Ya existe una marca con el mismo nombre.");
            return "brand/brand";
        }
        Brand brand = new Brand();
        brand.setId(brandDTO.getId());
        brand.setName(brandDTO.getName());
        brand.setDescription(brandDTO.getDescription());
        brandService.save(brand);
        redirectAttributes.addFlashAttribute("message", "Marca actualizada exitosamente.");
        redirectAttributes.addFlashAttribute("color", "alert alert-warning mt-3");
        return "redirect:/brands";
    }

    @GetMapping("/delete/{id}")
    public String deleteBrand(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        String message = brandService.deleteById(id);
        String messageType = "warning";
        if (message.equals("Marca eliminada exitosamente")) {
            messageType = "alert alert-info mt-3";
        } else if (message.equals("No se puede eliminar la marca porque tiene productos asociados")) {
            messageType = "alert alert-danger mt-3";
        }
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("color", messageType);
        return "redirect:/brands";
    }

}
