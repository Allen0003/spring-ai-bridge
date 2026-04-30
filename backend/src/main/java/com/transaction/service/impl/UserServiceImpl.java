package com.transaction.service.impl;

import com.transaction.domain.entity.UserEntity;
import com.transaction.domain.repository.UserRepository;
import com.transaction.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    /**
     * 根據 UID 獲取用戶資訊，這在 TransactionTools 中會被 AI 頻繁觸發
     */
    @Transactional(readOnly = true)
    public Optional<UserEntity> getUserByUid(String uid) {
        return userRepository.findById(uid);
    }

    /**
     * 建立或更新用戶
     */
    @Transactional
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    /**
     * 刪除用戶
     */
    @Transactional
    public void deleteUser(String uid) {
        userRepository.deleteById(uid);
    }

    /**
     * 鎖定用戶帳號
     * @param uid 用戶唯一識別碼
     * @return 更新後的用戶實體
     */
    @Transactional
    public UserEntity lockAccount(String uid) {
        // 1. 檢查用戶是否存在
        UserEntity user = userRepository.findById(uid)
                .orElseThrow(() -> new RuntimeException("找不到該用戶: " + uid));

        // 2. 檢查是否已經是鎖定狀態 (status = 0 代表停用/鎖定)
        if (user.getStatus() != null && user.getStatus() == 0) {
            return user; // 已經鎖定了就直接回傳
        }

        // 3. 執行鎖定
        user.setStatus(0);

        // 4. 保存 (JPA 會自動執行 Dirty Check 並在 Transaction 結束時 Commit)
        return userRepository.save(user);
    }

    /**
     * 解鎖用戶帳號
     */
    @Transactional
    public UserEntity unlockAccount(String uid) {
        UserEntity user = userRepository.findById(uid)
                .orElseThrow(() -> new RuntimeException("找不到該用戶: " + uid));

        user.setStatus(1); // 1 代表啟用
        return userRepository.save(user);
    }

}
