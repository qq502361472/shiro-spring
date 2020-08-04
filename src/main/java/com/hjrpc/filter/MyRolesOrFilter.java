package com.hjrpc.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MyRolesOrFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object o) throws Exception {
        Subject subject = this.getSubject(request, response);
        String[] roles = (String[]) o;
        if(roles==null||roles.length==0){
            return true;
        }
        for (String role : roles) {
            if(subject.hasRole(role)){
                return true;
            }
        }

        return false;
    }
}
