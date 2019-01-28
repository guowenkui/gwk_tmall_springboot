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


    /**
     * 导向到搜索页
     */
    @GetMapping(value = "/search")
    public String search(){
        return "fore/search";
    }

    /**
     * 导向到结算页
     */
    @GetMapping(value = "/buy")
    public String buy(){
        return "fore/buy";
    }


    /**
     * 导向到购物车界面
     */
    @GetMapping(value = "/cart")
    public String cart(){
        return "fore/cart";
    }


    /**
     * 导向到支付宝二维码界面
     */

    @GetMapping(value = "/alipay")
    public String alipay(){
        return "fore/alipay";
    }


    /**
     * 导向到支付成功界面
     */
    @GetMapping("/payed")
    public String payed(){
        return "fore/payed";
    }


    /**
     * 导向到我的订单界面
     */
    @GetMapping("/bought")
    public String bought(){
        return "fore/bought";
    }



    /**
     * 导向确认收货界面
     */
    @GetMapping("confirmPay")
    public String confirmPay(){
        return "fore/confirmPay";
    }

    /**
     * 导向到确认支付界面
     */
    @GetMapping("orderConfirmed")
    public String orderConfirmed(){
        return "fore/orderConfirmed";
    }

    /**
     * 导向到产品评价界面
     */
    @GetMapping("/review")
    public String review(){
        return "fore/review";
    }

}
