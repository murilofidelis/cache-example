package br.com.github.product.service.service;

import br.com.github.product.service.dto.CategoryDTO;
import br.com.github.product.service.dto.CategoryFilterDTO;
import br.com.github.product.service.exception.NotFoundException;
import br.com.github.product.service.mapper.CategoryMapper;
import br.com.github.product.service.repository.CategoryRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryMapper mapper;
    private final CategoryRepository repository;

    public CategoryService(CategoryMapper mapper, CategoryRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @CacheEvict(
            value = {"categories", "categoriesFilter"},
            allEntries = true
    )
    public CategoryDTO create(CategoryDTO category) {
        return mapper.toDto(repository.save(mapper.toEntity(category)));
    }

    @Cacheable(
            value = "category",
            cacheNames = "category",
            keyGenerator = "categoryKeyGenerator",
            cacheManager = "myCustomCacheManager")
    public CategoryDTO getById(long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("Category not found!"));
    }

    @CachePut(
            value = "categoryUpdate",
            cacheNames = "categoryUpdate",
            unless = "#result == null",
            keyGenerator = "categoryUpdateKeyGenerator"
    )
    @CacheEvict(
            value = {"categories", "categoriesFilter"},
            allEntries = true
    )
    public CategoryDTO update(CategoryDTO category) {
        return mapper.toDto(repository.save(mapper.toEntity(category)));
    }

    @Cacheable(
            value = "categories",
            cacheNames = "categories",
            cacheManager = "myCustomCacheManager"
    )
    public List<CategoryDTO> getAll() {
        return mapper.toListDto(repository.findAll());
    }

    @Cacheable(
            value = "categoriesFilter",
            cacheNames = "categoriesFilter",
            keyGenerator = "categoriesFilterKeyGenerator",
            cacheManager = "myCustomCacheManager"
    )
    public List<CategoryDTO> findByFilter(CategoryFilterDTO filter) {
        return mapper.toListDto(repository.findByFilter(filter.getDescription(), filter.getCod(), filter.isStatus()));
    }
}
