package com.how2java.tmall.service;


import com.how2java.tmall.dao.ProductDAO;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.Review;
import com.how2java.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private ProductImageSercice productImageSercice;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ReviewService reviewService;





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


    public void fill(List<Category> cs) {
        for (Category category:cs){
            fill(category);
        }
    }

    public void  fill(Category category){
        List<Product> products = listByCategory(category);
        this.productImageSercice.setFirstProductImage(products);
        category.setProducts(products);
    }



    public void fillByRow(List<Category> cs) {

        int productNumberEachRow = 8;
        for (Category category:cs){
            List<Product> products = category.getProducts();
            List<List<Product>> productsByRow = new ArrayList<>();

            for (int i = 0; i <products.size() ; i+=productNumberEachRow) {
                int toIndex = i+productNumberEachRow;
                toIndex = toIndex>products.size()?products.size():toIndex;
                List<Product> subList = products.subList(i,toIndex);
                productsByRow.add(subList);
            }
            category.setProductsByRow(productsByRow);
        }
    }

    private List<Product> listByCategory(Category category) {
        return this.productDAO.findByCategoryOrderById(category);
    }


    public void setSaleAndReviewNumber(Product product) {
        int saleCount = this.orderItemService.getSaleCount(product);
        product.setSaleCount(saleCount);

        int reviewCount = this.reviewService.getReviewCount(product);
        product.setReviewCount(reviewCount);
    }

    public void setSaleAndReviewNumber(List<Product> products){
        for (Product product:products){
            setSaleAndReviewNumber(product);
        }
    }

    public List<Product> search(String keyword, int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        List<Product> products = this.productDAO.findByNameLike("%"+keyword+"%",pageable);
        return products;
    }
}
