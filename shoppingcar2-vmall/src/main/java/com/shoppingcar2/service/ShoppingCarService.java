package com.shoppingcar2.service;

import com.shoppingcar2.entity.ShoppingCar;
import com.shoppingcar2.repository.ShoppingcatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class ShoppingCarService {

    @Autowired
    ShoppingcatRepository shoppingcatRepository;

    public ShoppingCar getShoppingCar(int userId, int productId){
        return shoppingcatRepository.getShoppingCar(userId,productId);
    }

    @Transactional
    public void addShoppingCar(ShoppingCar shoppingCar){
        shoppingcatRepository.save(shoppingCar);
    }

    @Transactional
    public void deleteShoppingCar(int userId,int productId){
        shoppingcatRepository.deleteShoppingCar(userId,productId);
    }

    @Transactional
    public void updateShoppingCar(ShoppingCar shoppingCar){
        shoppingcatRepository.save(shoppingCar);
    }

    public List<ShoppingCar> getShoppingCars(int userId){
        return shoppingcatRepository.getShoppingCars(userId);
    }

    @Transactional
    public boolean deleteShoppingCarByUser(int userId){
        shoppingcatRepository.deleteShoppingCarByUser(userId);
        return true;
    }

    @Transactional
    public boolean deleteShoppingCarByProduct(int productId){
        shoppingcatRepository.deleteShoppingCarByProduct(productId);
        return true;
    }
}
