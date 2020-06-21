package com.dhu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dhu.mapper.PermissionMapper;
import com.dhu.mapper.RoleMapper;
import com.dhu.mapper.UserMapper;
import com.dhu.pojo.Permission;
import com.dhu.pojo.Role;
import com.dhu.pojo.User;
import com.dhu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author zhou
 * @create 2020/5/28
 */
@Service(interfaceClass = UserService.class) //暴露服务
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    //根据用户名查询数据库获取用户信息、角色信息、权限信息
    public User findByUsername(String username) {
        User user = userMapper.findByUsername(username); //查询用户基本信息
        if(user == null) {
            return null;
        }
        Integer userId = user.getId();
        //根据id查询对应角色
        Set<Role> roles = roleMapper.findByUserId(userId);
        //根据角色查询对应权限
        for (Role role : roles) {
            Integer roleId = role.getId();
            Set<Permission> permissions = permissionMapper.findByRoleId(roleId);
            role.setPermissions(permissions);
        }
        user.setRoles(roles);
        return user;
    }
}
