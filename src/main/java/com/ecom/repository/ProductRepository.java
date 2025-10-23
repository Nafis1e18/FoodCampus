package com.ecom.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecom.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	List<Product> findByIsActiveTrue();

	Page<Product> findByIsActiveTrue(Pageable pageable);

	List<Product> findByCategory(String category);

	List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2);

	Page<Product> findByCategory(Pageable pageable, String category);

	Page<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2,
																				Pageable pageable);

	Page<Product> findByisActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2,
																							   Pageable pageable);

	// ADD THESE NEW METHODS FOR RECOMMENDATION FEATURE

	// Find products by title containing keyword (for recommendation matching)
	@Query("SELECT p FROM Product p WHERE p.isActive = true " +
			"AND LOWER(p.title) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<Product> findByTitleContaining(@Param("name") String name);

	// Find products by category containing keyword
	@Query("SELECT p FROM Product p WHERE p.isActive = true " +
			"AND LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Product> findByCategoryContaining(@Param("keyword") String keyword);
}
