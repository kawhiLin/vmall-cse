package com.shoppingcar.repository;

import com.shoppingcar.entity.ShoppingCar;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShoppingcatRepository extends CrudRepository<ShoppingCar, Integer>  {

    @Query("from ShoppingCar where userId=:userId and productId=:productId")
    public ShoppingCar getShoppingCar(@Param("userId")int userId, @Param("productId")int productId);

    @Query("from ShoppingCar where userId=:userId")
    public List<ShoppingCar> getShoppingCars(@Param("userId")int userId);

    @Modifying
    @Query("delete ShoppingCar where userId=:userId and productId=:productId")
    public void deleteShoppingCar(@Param("userId")int userId, @Param("productId")int productId);

    @Modifying
    @Query("delete ShoppingCar where userId=:userId")
    public void deleteShoppingCarByUser(@Param("userId")int userId);

    @Modifying
    @Query("delete ShoppingCar where productId=:productId")
    public void deleteShoppingCarByProduct(@Param("productId")int productId);
}
