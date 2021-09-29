package com.bjpowernode.controller;

import com.bjpowernode.pojo.Admin;
import com.bjpowernode.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminAction {
    //切记：在所有的界面层一定会有业务逻辑层的对象
    @Autowired
    private AdminService adminService;

    //实现登录判断，并进行相应的跳转
 /*   @RequestMapping("/login.action")
    public String login(String name, String pwd, HttpServletRequest request) {

        Admin admin=adminService.login(name,pwd);
        if (admin!=null){
            //登录成功
            request.setAttribute("admin",admin);
            return "main";
        }else {
            //登录失败
            request.setAttribute("errmsg","用户名或密码不正确");
            return "login";
        }

    }*/
    @RequestMapping("/login.action")
    public ModelAndView login(String name, String pwd) {
        ModelAndView mv = new ModelAndView();
        Admin admin = adminService.login(name, pwd);
        if (admin != null) {
            //登录成功
            mv.addObject(admin);
            mv.setViewName("main");
        } else {
            mv.addObject("errmsg", "用户名或密码不正确");
            mv.setViewName("login");
        }
        return mv;
    }

}
