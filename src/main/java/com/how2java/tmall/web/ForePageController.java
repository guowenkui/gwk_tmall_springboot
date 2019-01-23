package com.how2java.tmall.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

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

    /**
     * 退出登录
     */
    @GetMapping("/forelogout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:home";
    }

    /**
     * 导向到产品页
     */
    @GetMapping("/product")
    public String product(){
        return "fore/product";
    }

    /**
     * 导向到分类页
     */
    @GetMapping("/category")
    public String category(){
        return "fore/category";
    }
}
