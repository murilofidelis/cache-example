package br.com.github.product.service.service;

import br.com.github.product.service.dto.ProductDTO;
import br.com.github.product.service.mapper.ProductMapper;
import br.com.github.product.service.repository.CacheRepository;
import br.com.github.product.service.repository.ProductRepository;
import br.com.github.product.service.repository.impl.CacheRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private static final String CACHE_KEY = "product:%s";

    private final ProductMapper productMapper;
    private final ProductRepository repository;
    private final CacheRepository cacheRepository;

    @Autowired
    public ProductService(CacheRepositoryImpl cacheRepository, ProductMapper productMapper, ProductRepository repository) {
        this.cacheRepository = cacheRepository;
        this.productMapper = productMapper;
        this.repository = repository;
    }

    public ProductDTO create(ProductDTO dto) {
        ProductDTO productDTO = productMapper.toDto(repository.save(productMapper.toEntity(dto)));
        setInCache(productDTO);
        return productDTO;
    }

    public List<ProductDTO> getAll() {
        return productMapper.toListDto(repository.findAll());
    }

    public ProductDTO getById(long id) {
        ProductDTO product = cacheRepository.getCachedValue(String.format(CACHE_KEY, id), ProductDTO.class);
        if (product == null) {
            ProductDTO productDTO = productMapper.toDto(repository.findById(id).orElse(null));
            setInCache(productDTO);
            return productDTO;
        }
        return product;
    }

    public ProductDTO update(ProductDTO product) {
        ProductDTO productDTO = productMapper.toDto(repository.save(productMapper.toEntity(product)));
        setInCache(productDTO);
        return productDTO;
    }

    private void setInCache(ProductDTO productDTO) {
        if (productDTO != null) {
            cacheRepository.setCache(String.format(CACHE_KEY, productDTO.getId()), productDTO);
        }
    }

    public List<ProductDTO> findByParams(String manufacturer) {
        return null;
    }
}
