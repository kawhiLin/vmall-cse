package com.user.repository;


import com.user.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("from User where name=:nameOrEmail or email=:nameOrEmail")
    public User getUserByNameOrEmail(@Param("nameOrEmail") String nameOrEmail);

}
