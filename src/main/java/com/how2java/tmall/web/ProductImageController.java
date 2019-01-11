package com.how2java.tmall.web;


import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductImage;
import com.how2java.tmall.service.ProductImageSercice;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductImageController {


    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageSercice productImageSercice;


    @GetMapping("products/{pid}/productImages")
    public List<ProductImage> list(@PathVariable("pid") int pid, @RequestParam(value = "type") String type){

        Product product = this.productService.get(pid);

        if (ProductImageSercice.type_single.equals(type)){

            List<ProductImage> singles = this.productImageSercice.listSingleProductImages(product);
            return singles;
        }else if (ProductImageSercice.type_detail.equals(type)){
            List<ProductImage> details = this.productImageSercice.listDetailProductImages(product);
            return details;
        }else {
            return new ArrayList<>();
        }

    }


    @DeleteMapping("productImages/{id}")
    public void delete(@PathVariable("id") int id, HttpServletRequest request) throws Exception{
        //先获取后删除
        //删除productImage对象
        ProductImage productImage = this.productImageSercice.get(id);
        this.productImageSercice.delete(id);
        String type = productImage.getType();

        //删除图片
        if (type.equals(ProductImageSercice.type_single)){
            String singlePath = request.getServletContext().getRealPath("/img/productSingle");
            File file = new File(singlePath,id+".jpg");
            file.delete();

            String middlePath = request.getServletContext().getRealPath("/img/productSingle_middle");
            File middleFile = new File(middlePath,id+".jpg");
            middleFile.delete();

            String smallPath = request.getServletContext().getRealPath("/img/productSingle_small");
            File smallFile = new File(smallPath,id+".jpg");
            smallFile.delete();

        }else if (type.equals(ProductImageSercice.type_detail)){
            String detailPath = request.getServletContext().getRealPath("/img/productDetail");
            File detailFile = new File(detailPath,id+".jpg");
            detailFile.delete();
        }
    }


    @PostMapping("productImages")
    public void add(@RequestParam(value = "type") String type, @RequestParam(value = "pid") int pid, MultipartFile image,HttpServletRequest request){

        //对ProductImage对象的处理
       Product product = this.productService.get(pid);

       ProductImage productImage = new ProductImage();
       productImage.setType(type);
       productImage.setProduct(product);
       this.productImageSercice.add(productImage);

       //对图片的处理
       if (type.equals(ProductImageSercice.type_single)){
           String singlePath = request.getServletContext().getRealPath("/img/productSingle");
           File singleFile = new File(singlePath,productImage.getId()+".jpg");

           if (!singleFile.getParentFile().exists()){
               singleFile.getParentFile().mkdirs();
           }

           try {
               image.transferTo(singleFile);
               BufferedImage bufferedImage =  ImageUtil.change2jpg(singleFile);
               ImageIO.write(bufferedImage,"jpg", singleFile);


               String middlePath = request.getServletContext().getRealPath("/img/productSingle_middle");
               File middleFile = new File(middlePath,productImage.getId()+".jpg");
               middleFile.getParentFile().mkdirs();
               ImageUtil.resizeImage(singleFile,217,190,middleFile);

               String smallPath = request.getServletContext().getRealPath("/img/productSingle_small");
               File smallFile = new File(smallPath,productImage.getId()+".jpg");
               smallFile.getParentFile().mkdirs();
               ImageUtil.resizeImage(singleFile,56,56,smallFile);

           } catch (IOException e) {
               e.printStackTrace();
           }

       }else if (type.equals(ProductImageSercice.type_detail)){
           String detailPath = request.getServletContext().getRealPath("/img/productDetail");
           File detailFile = new File(detailPath,productImage.getId()+".jpg");
           try {
               image.transferTo(detailFile);
               BufferedImage bufferedImage = ImageUtil.change2jpg(detailFile);
               ImageIO.write(bufferedImage,"jpg",detailFile);
           } catch (IOException e) {
               e.printStackTrace();
           }
       }

    }

}
