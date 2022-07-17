package br.com.github.product.service.mapper;

import br.com.github.product.service.domain.Product;
import br.com.github.product.service.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Autowired
    private CategoryMapper categoryMapper;

    public ProductDTO toDto(Product product) {
        if (product == null) {
            return null;
        }
        ProductDTO p = new ProductDTO();
        p.setId(product.getId());
        p.setDescription(product.getDescription());
        p.setManufacturer(product.getManufacturer());
        p.setPrice(product.getPrice());
        p.setCategory(categoryMapper.toDto(product.getCategory()));
        return p;
    }

    public Product toEntity(ProductDTO product) {
        if (product == null) {
            return null;
        }
        Product p = new Product();
        p.setId(product.getId());
        p.setDescription(product.getDescription());
        p.setManufacturer(product.getManufacturer());
        p.setPrice(product.getPrice());
        p.setCategory(categoryMapper.toEntity(product.getCategory()));
        return p;
    }

    public List<ProductDTO> toListDto(List<Product> products) {
        return products != null ? products.stream().map(this::toDto).collect(Collectors.toList()) : Collections.emptyList();
    }
}
