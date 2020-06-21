package com.dhu.service;

import com.dhu.pojo.User;

/**
 * @author zhou
 * @create 2020/5/28
 */
public interface UserService {
    User findByUsername(String username);
}
