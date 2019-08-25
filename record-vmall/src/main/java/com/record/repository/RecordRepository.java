package com.record.repository;

import com.record.entity.ShoppingRecord;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecordRepository  extends CrudRepository<ShoppingRecord, Integer> {
    @Query("from ShoppingRecord where userId=:userId and productId=:productId and time=:time")
    public ShoppingRecord getShoppingRecord(@Param("userId")int userId, @Param("productId")int productId, @Param("time")String time);

    @Query("from ShoppingRecord where userId=:userId")
    public List<ShoppingRecord> getShoppingRecords(@Param("userId")int userId);

    @Query("from ShoppingRecord where orderStatus=:orderStatus")
    public List<ShoppingRecord> getShoppingRecordsByOrderStatus(@Param("orderStatus")int orderStatus);


    @Query("from ShoppingRecord where userId=:userId and productId=:productId")
    public List<ShoppingRecord> getUserProductRecord(@Param("userId")int userId, @Param("productId")int productId);

    @Modifying
    @Query("delete ShoppingRecord where userId=:userId")
    public void deleteShoppingRecordByUser(@Param("userId")int userId);

    @Modifying
    @Query("delete ShoppingRecord where productId=:productId")
    public void deleteShoppingRecordByProductId(@Param("productId")int productId);

}
