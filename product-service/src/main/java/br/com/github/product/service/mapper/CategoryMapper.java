package br.com.github.product.service.mapper;

import br.com.github.product.service.domain.Category;
import br.com.github.product.service.dto.CategoryDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryDTO toDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDTO c = new CategoryDTO();
        c.setId(category.getId());
        c.setDescription(category.getDescription());
        c.setCod(category.getCod());
        c.setStatus(category.getStatus());
        return c;
    }

    public Category toEntity(CategoryDTO category) {
        if (category == null) {
            return null;
        }
        Category c = new Category();
        c.setId(category.getId());
        c.setDescription(category.getDescription());
        c.setCod(category.getCod());
        c.setStatus(category.getStatus());
        return c;
    }

    public List<CategoryDTO> toListDto(List<Category> categories) {
        return categories != null ? categories.stream().map(this::toDto).collect(Collectors.toList()) : Collections.emptyList();
    }
}
