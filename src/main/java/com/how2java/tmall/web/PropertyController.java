package com.how2java.tmall.web;

import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.service.PropertyService;
import com.how2java.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PropertyController {

    @Autowired
    private PropertyService propertyService;


    @GetMapping("/categories/{cid}/properties")
    public Page4Navigator<Property> list(@PathVariable("cid") int cid, @RequestParam(value = "start",defaultValue ="0")int start,@RequestParam(value = "size",defaultValue = "5") int size) throws  Exception{
        start = start<0?0:start;
        Page4Navigator<Property> page = this.propertyService.list(cid,start,size,5);
        return page;
    }


    @DeleteMapping("/properties/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request) throws  Exception{
        this.propertyService.delete(id);
        return null;
    }

    @PostMapping("/properties")
    public Property add(@RequestBody Property bean) throws  Exception{
        this.propertyService.add(bean);
        return bean;
    }

    @GetMapping("/properties/{id}")
    public Property get(@PathVariable("id") int id)throws Exception{
        Property property = this.propertyService.get(id);
        return property;
    }

    @PutMapping("/properties")
    public void update(@RequestBody Property bean){
        this.propertyService.update(bean);
    }


}
