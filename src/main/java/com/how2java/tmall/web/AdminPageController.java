package com.how2java.tmall.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {
    @GetMapping(value = "/admin")
    public String admin(){
        return "redirect:admin_category_list";
    }

    /**
     *导向到分类管理界面
     */
    @GetMapping(value = "/admin_category_list")
    public String listCategory(){
        return "admin/listCategory";
    }


    /**
     * 导向到分类的编辑界面
     */
    @GetMapping(value = "/admin_category_edit")
    public String edit(){
        return "admin/editCategory";
    }


    /**
     * 导向到属性管理界面
     */
    @GetMapping(value = "/admin_property_list")
    public String propertyList(){
        return "admin/listProperty";
    }

    /**
     * 导向到属性管理编辑界面
     */
    @GetMapping(value = "/admin_property_edit")
    public String propertyEdit(){
        return "admin/editProperty";
    }

    /**
     * 导向到产品管理界面
     */

    @GetMapping("/admin_product_list")
    public String productList(){
        return "admin/listProduct";
    }

    /**
     * 导向到产品编辑界面
     */
    @GetMapping("/admin_product_edit")
    public String productEdit(){
        return "admin/editProduct";
    }


    /**
     * 导向到产品的图片管理界面
     */
    @GetMapping("admin_productImage_list")
    public String productImageList(){
        return "admin/listProductImage";
    }

    /**
     * 导向到产品的"设置属性"界面
     */
    @GetMapping("admin_propertyValue_edit")
    public String propertyValueEdit(){
        return "admin/editPropertyValue";
    }

}
