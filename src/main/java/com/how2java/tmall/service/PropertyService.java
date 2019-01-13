package com.how2java.tmall.service;

import com.how2java.tmall.dao.PropertyDAO;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PropertyService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PropertyDAO propertyDAO;


    public Page4Navigator<Property> list(int cid, int start, int size, int navigatePages) {
        Category category = this.categoryService.get(cid);

        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Property> pageFromJPA = this.propertyDAO.findByCategory(category,pageable);

        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    public void delete(int id) {
        this.propertyDAO.delete(id);
    }

    public void add(Property bean) {
        this.propertyDAO.save(bean);
    }

    public Property get(int id) {
        return this.propertyDAO.getOne(id);
    }

    public void update(Property bean) {
        this.propertyDAO.save(bean);
    }

    public List<Property> listByCategory(Category category){
        return this.propertyDAO.findByCategory(category);
    }
}
