package com.hjrpc.dao.impl;

import com.hjrpc.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public String getPasswordByUsername(String username){
        return jdbcTemplate.queryForObject("select password from users where username = ?", String.class, username);
    }

    public Set<String> getRolesByUsername(String username) {
        String sql = "select role_name from user_roles where username = ?";
        List<String> strings = jdbcTemplate.queryForList(sql, String.class, username);
        return new HashSet<String>(strings);
    }

    public Set<String> getPermissionsByRole(String role) {
        String sql = "select permission from roles_permissions where role_name = ?";
        List<String> strings = jdbcTemplate.queryForList(sql, String.class, role);
        return new HashSet<String>(strings);
    }

    public Set<String> getPermissionsByRoles(Set<String> roles) {
        String sql = "select permission from roles_permissions where role_name in (:roles)";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roles",roles);
        List<String> strings = namedParameterJdbcTemplate.queryForList(sql,params,String.class);
        return new HashSet<String>(strings);
    }
}
