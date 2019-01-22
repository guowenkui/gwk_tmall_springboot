package com.how2java.tmall.service;

import com.how2java.tmall.dao.CategoryDAO;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private  CategoryDAO categoryDAO;

    public List<Category> list(){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return this.categoryDAO.findAll(sort);
    }

    public Page4Navigator<Category> list(int start,int size,int navigatePages){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page pageFromJPA = categoryDAO.findAll(pageable);
        return  new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    public void add(Category category){
        this.categoryDAO.save(category);
    }

    public void delete(int id){
        this.categoryDAO.delete(id);
    }

    public Category get(int id){
        return  this.categoryDAO.findOne(id);
    }

    public void update(Category category){
        this.categoryDAO.save(category);
    }

    public void removeCategoryFromProduct(List<Category> cs) {
        for (Category category:cs){
            removeCategoryFromProduct(category);
        }
    }

    public void removeCategoryFromProduct(Category category){

        List<Product> products = category.getProducts();
        if (products!=null){
            for (Product product:products){
                product.setCategory(null);
            }
        }


        List<List<Product>> list = category.getProductsByRow();
        if (list!=null){
            for (List<Product> list1:list){
                for (Product product:list1){
                    product.setCategory(null);
                }
            }
        }

    }




}
