package com.how2java.tmall.service;


import com.how2java.tmall.dao.ProductDAO;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductDAO productDAO;



    public Page4Navigator<Product> list(int cid, int start, int size, int nagigatePages) {

        Category category = this.categoryService.get(cid);

        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);

        Page<Product> pageFromJPA = this.productDAO.findByCategory(category,pageable);

        return new Page4Navigator<>(pageFromJPA,nagigatePages);
    }

    public void add(Product bean) {
        this.productDAO.save(bean);
    }

    public void delete(int id) {
        this.productDAO.delete(id);
    }

    public Product get(int id) {
        return this.productDAO.findOne(id);
    }

    public void update(Product bean) {
        this.productDAO.save(bean);
    }
}
