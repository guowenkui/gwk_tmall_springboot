package com.how2java.tmall.web;

import com.google.common.collect.Ordering;
import com.how2java.tmall.comparator.*;
import com.how2java.tmall.pojo.*;
import com.how2java.tmall.service.*;
import com.how2java.tmall.util.Result;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.*;

@RestController
public class ForeRESTController {

    @Autowired
    private CategoryService  categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductImageSercice productImageSercice;

    @Autowired
    private PropertyValueService propertyValueService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderService orderService;





    @GetMapping("/forehome")
    public Object home(){
        List<Category> cs = this.categoryService.list();
        this.productService.fill(cs);
        this.productService.fillByRow(cs);
        this.categoryService.removeCategoryFromProduct(cs);
        return cs;
    }


    @PostMapping("/foreregister")
    public Object register(@RequestBody User user){
        String name = user.getName();
        String password = user.getPassword();
        name= HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean isExist = this.userService.isExist(user.getName());
        if (isExist){
            String message = "用户名已经被占用,不能使用";
            return Result.fail(message);
        }

        user.setPassword(password);
        this.userService.add(user);
        return Result.success();
    }


    /**
     * 登录
     */
    @PostMapping("/forelogin")
    public Object login(@RequestBody User user, HttpSession session){

        String name = user.getName();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        User tempUser = this.userService.get(user.getName(),user.getPassword());
        if (tempUser!=null){
            session.setAttribute("user",user);
            return Result.success();
        }else{
            return Result.fail("账号密码错误");
        }
    }



    @GetMapping("/foreproduct/{pid}")

    public Object product(@PathVariable int pid){

        Product product = this.productService.get(pid);
        List<ProductImage> singles = this.productImageSercice.listSingleProductImages(product);
        List<ProductImage> details = this.productImageSercice.listDetailProductImages(product);
        product.setProductSingleImages(singles);
        product.setProductDetailImages(details);

        List<PropertyValue> pvs = this.propertyValueService.list(product);
        List<Review> reviews = this.reviewService.list(product);
        this.productService.setSaleAndReviewNumber(product);
        this.productImageSercice.setFirstProductImage(product);


        Map map = new HashMap();
        map.put("product",product);
        map.put("pvs",pvs);
        map.put("reviews",reviews);
        return Result.success(map);
    }


    /**
     *检查是否登录,未登录弹出模态登录窗口
     */
    @GetMapping("/forecheckLogin")
    public Object checkLogin(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user==null){
            return Result.fail("未登录");
        }else {
            return Result.success();
        }
    }


    @GetMapping("/forecategory/{cid}")
    public Object category(@PathVariable int cid,String sort){
        Category category = this.categoryService.get(cid);
        this.productService.fill(category);
        this.productService.setSaleAndReviewNumber(category.getProducts());
        this.categoryService.removeCategoryFromProduct(category);
        if (sort!=null){
            switch (sort){
                case "review":
                    Collections.sort(category.getProducts(),new ProductReviewComparator());
                    break;
                case "date":
                    Collections.sort(category.getProducts(),new ProductDateComparator());
                    break;
                case "all":
                    Collections.sort(category.getProducts(),new ProductAllComparator());
                    break;
                case "price":
                    Collections.sort(category.getProducts(),new ProductPriceComparator());
                    break;
                case "saleCount":
                    Collections.sort(category.getProducts(),new ProductSaleCountComparator());
                    break;

            }
        }
        return category;
    }

    /**
     * 搜索结果
     */
    @PostMapping("/foresearch")
    public Object search(String keyword){
        if (keyword==null){
            keyword ="";
        }

        List<Product> ps = this.productService.search(keyword,0,20);
        this.productImageSercice.setFirstProductImage(ps);
        this.productService.setSaleAndReviewNumber(ps);
        return ps;
    }

    /**
     * 立即购买
     */
    @GetMapping("/forebuyone")
    public Object buyone(int pid,int num,HttpSession session){
        return buyoneAndAddCart(pid,num,session);
    }

    private int buyoneAndAddCart(int pid, int num, HttpSession session) {
        Product product = this.productService.get(pid);
        int oiid = 0;

        User user = (User) session.getAttribute("user");

        List<OrderItem> orderItems = this.orderItemService.listByUser(user);

        boolean isFound = false;
        for (OrderItem item:orderItems){
            if (item.getProduct().getId()==product.getId()){
                item.setNumber(item.getNumber()+num);
                this.orderItemService.update(item);
                isFound = true;
                oiid = item.getId();
                break;
            }
        }

        if (!isFound){
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setUser(user);
            item.setNumber(num);
            this.orderItemService.add(item);
            oiid = item.getId();
        }
        return oiid;
    }

    /**
     *结算订单页数据
     */
    @GetMapping("forebuy")
    public Object buy(String[] oiid,HttpSession session){
        List<OrderItem> orderItems = new ArrayList<>();
        float total = 0;

        for (String stringId:oiid){

            int id = Integer.parseInt(stringId);
            OrderItem item = this.orderItemService.get(id);
            total+=item.getNumber()*item.getProduct().getPromotePrice();
            orderItems.add(item);
        }


        this.productImageSercice.setFirstProductImageOnOrderItems(orderItems);

        session.setAttribute("ois",orderItems);

        Map map = new HashMap();
        map.put("total",total);
        map.put("orderItems",orderItems);
        return Result.success(map);
    }


    /**
     * 加入购物车
     */
    @GetMapping("foreaddCart")
    public Object addCart(int pid,int num,HttpSession session){
        buyoneAndAddCart(pid,num,session);
        return Result.success();
    }


    /**
     * 购物车列表
     */
    @GetMapping("/forecart")
    public Object cartList(HttpSession session){

        User user = (User) session.getAttribute("user");

        List<OrderItem> orderItems = this.orderItemService.listByUser(user);
        this.productImageSercice.setFirstProductImageOnOrderItems(orderItems);
        return  orderItems;
    }


    /**
     * 购物车订单删除
     */
    @GetMapping("/foredeleteOrderItem")
    public Object deleteOrderItem(int oiid,HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user==null){
            return Result.fail("未登录");
        }
        this.orderItemService.delete(oiid);
        return Result.success();
    }


    /**
     * 购物车订单改变数量
     */
    @GetMapping("forechangeOrderItem")
    public Object changeOrderItem(int pid,int num,HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user==null){
            return Result.fail("未登录");
        }

        List<OrderItem> items = this.orderItemService.listByUser(user);
        for (OrderItem item:items){
            if (item.getProduct().getId()==pid){
                item.setNumber(num);
                this.orderItemService.update(item);
                break;
            }
        }
        return Result.success();
    }


    /**
     *提交订单
     */
    @PostMapping("/forecreateOrder")
    public Object createOrder(@RequestBody Order order,HttpSession session){

        User user = (User) session.getAttribute("user");
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setStatus(OrderService.waitPay);
        order.setUser(user);
        order.setCreateDate(new Date());
        List<OrderItem> items = (List<OrderItem>) session.getAttribute("ois");

        float total = this.orderService.add(order,items);

        Map map = new HashMap();
        map.put("total",total);
        map.put("oid",order.getId());
        return  Result.success(map);

    }

    @GetMapping("/forepayed")
    public Object payed(int oid){
        Order order = this.orderService.get(oid);
        order.setPayDate(new Date());
        order.setStatus(OrderService.waitDelivery);
        this.orderService.update(order);
        return order;
    }

    @GetMapping("/forebought")
    public Object bought(HttpSession session){
        User user = (User) session.getAttribute("user");

        List<Order> orders = this.orderService.listByUserWithoutDelete(user);
        this.orderService.removeOrderFromOrderItem(orders);
        return  orders;
    }

    /**
     *我的订单--删除订单
     */
    @PutMapping("foredeleteOrder")
    public Object deleteOrder(int oid){
        Order order = this.orderService.get(oid);
        order.setStatus(OrderService.delete);
        this.orderService.update(order);
        return Result.success();
    }

    /**
     *
     */
    @GetMapping("foreconfirmPay")
    public Object confirmPay(int oid){
        Order order = this.orderService.get(oid);
        this.orderItemService.fill(order);
        this.orderService.calc(order);
        this.orderService.removeOrderFromOrderItem(order);
        return order;
    }


    /**
     *确认支付
     */
    @GetMapping("foreorderConfirmed")
    public void orderConfirmed(int oid){
        Order order = this.orderService.get(oid);
        order.setStatus(OrderService.waitReview);
        order.setConfirmDate(new Date());
        this.orderService.update(order);
    }

    /**
     *评价
     */
    @GetMapping("forereview")
    public Object review(int oid){

        Order order = this.orderService.get(oid);
        this.orderItemService.fill(order);
        this.orderService.removeOrderFromOrderItem(order);

        Product product = order.getOrderItems().get(0).getProduct();

        List<Review> reviews = this.reviewService.list(product);
        this.productService.setSaleAndReviewNumber(product);

        Map map = new HashMap();
        map.put("o",order);
        map.put("p",this.productService.get(88));
        map.put("reviews",reviews);

        return  Result.success(map);
    }


    /**
     *做评价
     */
    @PostMapping("foredoreview")
    public Object doReview(int oid,int pid,String content,HttpSession session){

        Order order = this.orderService.get(oid);
        order.setStatus(OrderService.finish);
        this.orderService.update(order);


        User user = (User) session.getAttribute("user");
        Product product = this.productService.get(pid);


        content = HtmlUtils.htmlEscape(content);
        Review review = new Review();
        review.setContent(content);
        review.setUser(user);
        review.setProduct(product);
        review.setCreateDate(new Date());
        this.reviewService.add(review);

        return Result.success();
    }
}
