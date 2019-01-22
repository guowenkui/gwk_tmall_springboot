package com.how2java.tmall.web;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.service.UserService;
import com.how2java.tmall.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ForeRESTController {

    @Autowired
    private CategoryService  categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;



    @GetMapping("/forehome")
    public Object home(){
        List<Category> cs = this.categoryService.list();
        this.productService.fill(cs);
        this.productService.fillByRow(cs);
        this.categoryService.removeCategoryFromProduct(cs);
        return cs;
    }


    @PostMapping("/foreregister")
    public Object register(@RequestBody User user){
        String name = user.getName();
        String password = user.getPassword();
        name= HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean isExist = this.userService.isExist(user.getName());
        if (isExist){
            String message = "用户名已经被占用,不能使用";
            return Result.fail(message);
        }

        user.setPassword(password);
        this.userService.add(user);
        return Result.success();
    }


    /**
     * 登录
     */
    @PostMapping("/forelogin")
    public Object login(@RequestBody User user, HttpSession session){

        String name = user.getName();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        User tempUser = this.userService.get(user.getName(),user.getPassword());
        if (tempUser!=null){
            session.setAttribute("user",user);
            return Result.success();
        }else{
            return Result.fail("账号密码错误");
        }
    }

}
