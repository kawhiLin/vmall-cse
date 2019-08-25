package com.user.repository;

import com.user.entity.UserDetail;
import org.springframework.data.repository.CrudRepository;

public interface UserDetailRepository extends CrudRepository<UserDetail, Integer> {
}
