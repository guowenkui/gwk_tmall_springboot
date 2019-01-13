package com.how2java.tmall.web;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.PropertyValue;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.service.PropertyValueService;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PropertyValueController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PropertyValueService propertyValueService;



    @GetMapping("products/{pid}/propertyValues")
    public List<PropertyValue> list(@PathVariable("pid") int pid){
        Product product = this.productService.get(pid);
        this.propertyValueService.init(product);
        List<PropertyValue> values = this.propertyValueService.list(product);
        return values;
    }

    @PutMapping("propertyValues")
    public Object update(@RequestBody PropertyValue value){
        this.propertyValueService.update(value);
        return value;
    }



}
