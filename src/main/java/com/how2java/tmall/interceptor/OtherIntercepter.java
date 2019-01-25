package com.how2java.tmall.interceptor;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class OtherIntercepter  implements HandlerInterceptor {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private CategoryService categoryService;



    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

        /**
         * 购物车件数
         */
        HttpSession session = httpServletRequest.getSession();
        User user = (User) session.getAttribute("user");
        if (user!=null){
            List<OrderItem> items = this.orderItemService.listByUser(user);
            int cartTotalItemNumber = 0;
            for (OrderItem item:items){
                cartTotalItemNumber +=item.getNumber();
            }
            session.setAttribute("cartTotalItemNumber",cartTotalItemNumber);
        }


        /**
         * 搜索框下面的
         */
        List<Category> categories = this.categoryService.list();
        String contextPath=httpServletRequest.getServletContext().getContextPath();
        httpServletRequest.getServletContext().setAttribute("categories_below_search", categories);
        httpServletRequest.getServletContext().setAttribute("contextPath", contextPath);

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
