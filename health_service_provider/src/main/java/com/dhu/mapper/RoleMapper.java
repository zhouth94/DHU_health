package com.dhu.mapper;

import com.dhu.pojo.Role;

import java.util.Set;

/**
 * @author zhou
 * @create 2020/5/28
 */
public interface RoleMapper {
    Set<Role> findByUserId(Integer userId);
}
