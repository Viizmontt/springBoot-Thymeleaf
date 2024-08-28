package com.viizmontt.product.Controller;

import com.viizmontt.product.Model.Brand;
import com.viizmontt.product.Model.Category;
import com.viizmontt.product.Model.Product;
import com.viizmontt.product.Model.ProductDTO;
import com.viizmontt.product.Service.BrandService;
import com.viizmontt.product.Service.CategoryService;
import com.viizmontt.product.Service.FileStorageService;
import com.viizmontt.product.Service.ProductService;
import jakarta.validation.Valid;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    //private static final String UPLOAD_DIR = "public/images/";

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileStorageService fileStorageService; // Servicio para manejar archivos

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product/products";
    }

    private void populateSelects(Model model) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("categories", categoryService.findAll());
    }

    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        populateSelects(model);
        return "product/product";
    }

    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("productDTO") ProductDTO productDTO,
            BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            populateSelects(model);
            return "product/product";
        }
        if (productService.existsByNameExcludingId(productDTO.getName(), productDTO.getId())) {
            result.rejectValue("name", "error.product", "Ya existe un Producto con ese nombre");
            populateSelects(model);
            return "product/product";
        }
        if (productDTO.getImageFile().isEmpty()) {
            result.rejectValue("imageFile", "error.product", "Debe ingresar una imagen");
            populateSelects(model);
            return "product/product";
        }
        MultipartFile image = productDTO.getImageFile();
        String storageFileName = fileStorageService.storeFile(image);
        if (storageFileName == null) {
            result.rejectValue("imageFile", "error.product", "Error al guardar la imagen");
            populateSelects(model);
            return "product/product";
        }
        Brand brand = brandService.findById(productDTO.getBrandId());
        Category category = categoryService.findById(productDTO.getCategoryId());
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setBrand(brand);
        product.setCategory(category);
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCreatedAt(new Date());
        product.setImageFileName(storageFileName);
        productService.save(product);
        redirectAttributes.addFlashAttribute("message", "Producto agregado exitosamente.");
        redirectAttributes.addFlashAttribute("color", "alert alert-primary mt-3");
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable("id") int id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        if (product == null) {
            return "redirect:/products";
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setBrandId(product.getBrand().getId());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        model.addAttribute("productDTO", productDTO);
        populateSelects(model);
        return "product/product";
    }

    @PostMapping("/{id}")
    public String updateProduct(
            Model model,
            @PathVariable("id") int id,
            @Valid @ModelAttribute ProductDTO productDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        Product product = productService.findById(id);
        Brand brand = brandService.findById(productDTO.getBrandId());
        Category category = categoryService.findById(productDTO.getCategoryId());
        
        if (result.hasErrors()) {  
            return "product/product";
        }
        if (productService.existsByNameExcludingId(productDTO.getName(), id)) {
            result.rejectValue("name", "error.product", "Ya existe un Producto con ese nombre");
            //return showEditPage(id, model);
            //populateSelects(model);
            return "redirect:/products/edit/" + id;
        }
        if (!productDTO.getImageFile().isEmpty()) {
            fileStorageService.deleteFile(product.getImageFileName());
            String storageFileName = fileStorageService.storeFile(productDTO.getImageFile());
            if (storageFileName == null) {
                result.rejectValue("imageFile", "error.product", "Error al guardar la imagen");
                populateSelects(model);
                return "product/product";
            }
            product.setImageFileName(storageFileName);
        }
        product.setName(productDTO.getName());
        product.setBrand(brand);
        product.setCategory(category);
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        productService.save(product);
        redirectAttributes.addFlashAttribute("message", "Producto actualizado correctamente");
        redirectAttributes.addFlashAttribute("color", "alert alert-primary mt-3");
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        productService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Producto eliminado exitosamente");
        redirectAttributes.addFlashAttribute("color", "alert alert-info mt-3");
        return "redirect:/products";
    }
}
