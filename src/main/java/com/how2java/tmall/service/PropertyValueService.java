package com.how2java.tmall.service;


import com.how2java.tmall.dao.PropertyValueDAO;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.pojo.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyValueService {

    @Autowired
    private PropertyValueDAO propertyValueDAO;

    @Autowired
    private PropertyService propertyService;



    public void init(Product product) {
        List<Property> properties = this.propertyService.listByCategory(product.getCategory());
        for (Property property:properties){
            PropertyValue  propertyValue = getByPropertyAndProduct(product,property);
            if (null==propertyValue){
                propertyValue = new PropertyValue();
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                this.propertyValueDAO.save(propertyValue);
            }
        }
    }

    public List<PropertyValue> list(Product product) {
        return this.propertyValueDAO.findByProductOrderByIdDesc(product);
    }

    public PropertyValue getByPropertyAndProduct(Product product,Property property){
        return this.propertyValueDAO.getByPropertyAndProduct(property,product);
    }

    public void update(PropertyValue value) {
        this.propertyValueDAO.save(value);
    }
}
