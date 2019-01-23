package com.how2java.tmall.web;

import com.how2java.tmall.comparator.*;
import com.how2java.tmall.pojo.*;
import com.how2java.tmall.service.*;
import com.how2java.tmall.util.Result;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ForeRESTController {

    @Autowired
    private CategoryService  categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductImageSercice productImageSercice;

    @Autowired
    private PropertyValueService propertyValueService;

    @Autowired
    private ReviewService reviewService;





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



    @GetMapping("/foreproduct/{pid}")

    public Object product(@PathVariable int pid){

        Product product = this.productService.get(pid);
        List<ProductImage> singles = this.productImageSercice.listSingleProductImages(product);
        List<ProductImage> details = this.productImageSercice.listDetailProductImages(product);
        product.setProductSingleImages(singles);
        product.setProductDetailImages(details);

        List<PropertyValue> pvs = this.propertyValueService.list(product);
        List<Review> reviews = this.reviewService.list(product);
        this.productService.setSaleAndReviewNumber(product);
        this.productImageSercice.setFirstProductImage(product);


        Map map = new HashMap();
        map.put("product",product);
        map.put("pvs",pvs);
        map.put("reviews",reviews);
        return Result.success(map);
    }


    /**
     *检查是否登录,未登录弹出模态登录窗口
     */
    @GetMapping("/forecheckLogin")
    public Object checkLogin(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user==null){
            return Result.fail("未登录");
        }else {
            return Result.success();
        }
    }


    @GetMapping("/forecategory/{cid}")
    public Object category(@PathVariable int cid,String sort){
        Category category = this.categoryService.get(cid);
        this.productService.fill(category);
        this.productService.setSaleAndReviewNumber(category.getProducts());
        this.categoryService.removeCategoryFromProduct(category);
        if (sort!=null){
            switch (sort){
                case "review":
                    Collections.sort(category.getProducts(),new ProductReviewComparator());
                    break;
                case "date":
                    Collections.sort(category.getProducts(),new ProductDateComparator());
                    break;
                case "all":
                    Collections.sort(category.getProducts(),new ProductAllComparator());
                    break;
                case "price":
                    Collections.sort(category.getProducts(),new ProductPriceComparator());
                    break;
                case "saleCount":
                    Collections.sort(category.getProducts(),new ProductSaleCountComparator());
                    break;

            }
        }
        return category;
    }


}
