package com.dhu.mapper;

import com.dhu.pojo.Permission;

import java.util.Set;

/**
 * @author zhou
 * @create 2020/5/28
 */
public interface PermissionMapper {
    Set<Permission> findByRoleId(Integer roleId);
}
