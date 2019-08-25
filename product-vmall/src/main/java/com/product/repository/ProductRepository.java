package com.product.repository;

import com.product.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query("from Product where name like  %:searchKeyWord% or key_word like %:searchKeyWord%")
    public List<Product> getProductsByKeyWord(@Param("searchKeyWord") String searchKeyWord);

    @Query("from Product where name=:name")
    public Product getProduct(@Param("name") String name);
}
