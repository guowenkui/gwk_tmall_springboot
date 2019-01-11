package com.how2java.tmall.web;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.service.ProductImageSercice;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageSercice productImageSercice;



    @GetMapping("categories/{cid}/products")
    public Page4Navigator<Product> list(@PathVariable("cid") int cid, @RequestParam(value = "start",defaultValue = "0") int start, @RequestParam(value = "size",defaultValue = "5")int size) throws Exception{

        start = start<0?0:start;
        Page4Navigator<Product> page4Navigator = this.productService.list(cid,start,size,5);
        this.productImageSercice.setFirstProductImage(page4Navigator.getContent());
        return page4Navigator;
    }

    @PostMapping("products")
    public void add(@RequestBody Product bean) throws Exception{
        bean.setCreateDate(new Date());
        this.productService.add(bean);
    }


    @DeleteMapping("products/{id}")
    public String delete(@PathVariable("id") int id) throws Exception{
        this.productService.delete(id);
        return null;
    }

    @GetMapping("products/{id}")
    public Product get(@PathVariable("id") int id) throws Exception{
        Product product = this.productService.get(id);
        return product;
    }

    @PutMapping("products")
    public void update(@RequestBody Product bean) throws Exception{
        this.productService.update(bean);
    }


}
