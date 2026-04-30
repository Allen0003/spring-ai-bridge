package com.transaction.domain.repository;

import com.transaction.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> getUserByUid(String uid);

    @Modifying
    @Query("UPDATE UserEntity u SET u.status = :status WHERE u.uid = :uid")
    int updateStatus(@Param("uid") String uid, @Param("status") Integer status);

}
