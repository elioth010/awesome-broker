package com.elioth.awesome.message.repository;


import com.elioth.awesome.message.repository.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

}
