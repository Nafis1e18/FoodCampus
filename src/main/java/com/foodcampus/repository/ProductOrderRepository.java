package com.foodcampus.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.foodcampus.dto.TopProductDTO;
import com.foodcampus.dto.TopUserDTO;
import com.foodcampus.model.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {

	List<ProductOrder> findByUserId(Integer userId);

	ProductOrder findByOrderId(String orderId);

	// Top products by total quantity sold
	@Query("SELECT new com.foodcampus.dto.TopProductDTO(p.id, p.title, SUM(o.quantity)) " +
		"FROM ProductOrder o JOIN o.product p " +
		"GROUP BY p.id, p.title " +
		"ORDER BY SUM(o.quantity) DESC")
	List<TopProductDTO> findTopProducts(Pageable pageable);

	// Top users by total spent (price * quantity)
	@Query("SELECT new com.foodcampus.dto.TopUserDTO(u.id, u.name, SUM(COALESCE(o.price,0) * COALESCE(o.quantity,0))) " +
		"FROM ProductOrder o JOIN o.user u " +
		"GROUP BY u.id, u.name " +
		"ORDER BY SUM(COALESCE(o.price,0) * COALESCE(o.quantity,0)) DESC")
	List<TopUserDTO> findTopUsers(Pageable pageable);

}
