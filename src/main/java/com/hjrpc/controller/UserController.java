package com.hjrpc.controller;

import com.hjrpc.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = "/subLogin",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String subLogin(User user){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        String result = "";
        try {
            subject.login(token);
            result+="登录成功\n";
        } catch (AuthenticationException e) {
            e.printStackTrace();
            result+= e.getMessage();
            return result;
        }


        try {
            subject.checkRoles("admin");
            result+="有:admin权限\n";
        } catch (AuthorizationException e) {
            e.printStackTrace();
            result+="沒有:admin权限:"+e.getMessage();
            return result;
        }
        try {
            subject.checkPermissions("user:add");
            result+="有:user:add权限\n";
        } catch (AuthorizationException e) {
            e.printStackTrace();
            result+="沒有:user:add权限:"+e.getMessage();
            return result;
        }

        return result;
    }

    @RequiresRoles("admin")
    @RequiresPermissions("user:add")
    @RequestMapping(value = "/testRoles",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testRoles(){
        return "测试权限";
    }

    @RequiresRoles("user")
    @RequiresPermissions("user:add")
    @RequestMapping(value = "/testRoles1",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testRoles1(){
        return "测试权限";
    }

    @RequiresRoles("user")
    @RequiresPermissions("user:delete")
    @RequestMapping(value = "/testRoles2",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testRoles2(){
        return "测试权限";
    }

    @RequestMapping(value = "/testConfRoles",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testConfRoles(){
        return "测试权限";
    }

    @RequestMapping(value = "/testConfRoles1",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testConfRoles1(){
        return "测试权限";
    }

    @RequestMapping(value = "/testConfRoles2",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testConfRoles2(){
        return "测试权限";
    }

    @RequestMapping(value = "/testMyRolesOr",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testMyRolesOr(){
        return "测试权限";
    }

}
