package com.how2java.tmall.web;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ForeRESTController {

    @Autowired
    private CategoryService  categoryService;

    @Autowired
    private ProductService productService;



    @GetMapping("/forehome")
    public Object home(){
        List<Category> cs = this.categoryService.list();
        this.productService.fill(cs);
        this.productService.fillByRow(cs);
        this.categoryService.removeCategoryFromProduct(cs);
        return cs;
    }

}
