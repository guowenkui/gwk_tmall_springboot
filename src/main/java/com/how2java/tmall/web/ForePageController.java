package com.how2java.tmall.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForePageController {

    @GetMapping(value = "/")
    public String index(){
        return "redirect:home";
    }

    @GetMapping(value = "/home")
    public String home(){
        return "fore/home";
    }

    /**
     * 导向到注册界面
     */
    @GetMapping(value = "/register")
    public String register(){
        return "fore/register";
    }


    /**
     *导向到注册成功界面
     */
    @GetMapping(value = "/registerSuccess")
    public String registerSuccess(){
        return "fore/registerSuccess";
    }


    /**
     * 导向到登录界面
     */
    @GetMapping(value = "/login")
    public String login(){
        return "fore/login";
    }
}
