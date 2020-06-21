package com.dhu.mapper;

import com.dhu.pojo.User;

/**
 * @author zhou
 * @create 2020/5/28
 */
public interface UserMapper {
    User findByUsername(String username);
}
