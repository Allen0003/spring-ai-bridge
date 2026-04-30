package com.transaction.service;

import com.transaction.domain.entity.UserEntity;

import java.util.Optional;

public interface UserService {
    public Optional<UserEntity> getUserByUid(String uid);

    public UserEntity saveUser(UserEntity user);

    public void deleteUser(String uid);

    UserEntity lockAccount(String uid);

    UserEntity unlockAccount(String uid);
}
