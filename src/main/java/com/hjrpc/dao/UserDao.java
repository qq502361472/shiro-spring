package com.hjrpc.dao;

import java.util.Set;

public interface UserDao {
    String getPasswordByUsername(String username);
    Set<String> getRolesByUsername(String username);
    Set<String> getPermissionsByRole(String role);

    Set<String> getPermissionsByRoles(Set<String> roles);
}
