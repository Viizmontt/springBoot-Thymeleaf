package com.viizmontt.product.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

public class ProductDTO {
    private int id;

    @NotEmpty(message = "El nombre es requerido")
    private String name;
    
    @NotNull(message = "La marca es requerida")
    private Integer brandId;
    
    @NotNull(message = "La categoría es requerida")
    private Integer categoryId;
    
    @Min(value = 0, message = "El precio debe ser mayor o igual a cero")
    private double price;
    
    private String description;
    private Date createdAt;
    
    private MultipartFile imageFile;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    
}
