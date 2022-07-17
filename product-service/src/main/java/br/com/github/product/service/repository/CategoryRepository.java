package br.com.github.product.service.repository;

import br.com.github.product.service.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE 0=0 " +
            " AND (:description IS NULL OR c.description =: description) " +
            " AND (:cod IS NULL OR c.cod = :cod) " +
            " AND (:status IS NULL OR c.status = :status)")
    List<Category> findByFilter(@Param("description") String description, @Param("cod") String cod, @Param("status") Boolean status);
}
