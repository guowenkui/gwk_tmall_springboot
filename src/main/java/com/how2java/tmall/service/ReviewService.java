package com.how2java.tmall.service;

import com.how2java.tmall.dao.ReviewDAO;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.PropertyValue;
import com.how2java.tmall.pojo.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {


    @Autowired
    private ReviewDAO reviewDAO;


    public List<Review> list(Product product) {
        List<Review> list = this.reviewDAO.findByProductOrderByIdDesc(product);
        return list;
    }

    public int getReviewCount(Product product) {
        return this.reviewDAO.countByProduct(product);
    }
}
