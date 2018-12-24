package com.lk.shell.service.impl;

import com.lk.shell.dataobject.ProductInfo;
import com.lk.shell.enums.ProductStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {
    @Autowired
    private  ProductServiceImpl productService;

    @Test
    public void findOne() throws Exception {
       ProductInfo productInfo = productService.findOne("123456");
        Assert.assertEquals("123456",productInfo.getProductId());
    }

    @Test
    public void findUpAll() throws Exception {
        List<ProductInfo> productInfoList=  productService.findUpAll();
        Assert.assertNotEquals(0,productInfoList.size());
    }

    @Test
    public void findAll() throws Exception {
        PageRequest request = new PageRequest(0,2);
        Page<ProductInfo> productInfoPage = productService.findAll(request);
        System.out.println(productInfoPage.getTotalElements());

    }

    @Test
    public void save() throws Exception {
        ProductInfo productInfo =new ProductInfo();
        productInfo.setProductId("123457");
        productInfo.setProductName("皮皮虾");
        productInfo.setProductPrice(new BigDecimal(3.4));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("很皮的虾！");
        productInfo.setProductIcon("https://ss0.baidu.com/73t1bjeh1BF3odCf/it/u=3825292109,492064218&fm=85&s=0B40CF14153570230E715961030070E2");
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        productInfo.setCategoryType(2);
        ProductInfo result=productService.save(productInfo);
        Assert.assertNotNull(result);
    }
    @Test
    public void onSale(){
            ProductInfo result = productService.onSale("123457");
            Assert.assertEquals(ProductStatusEnum.UP,result.getProductStatusEnum());
    }
    @Test
    public void offSale(){
        ProductInfo result = productService.onSale("123456");
        Assert.assertEquals(ProductStatusEnum.UP,result.getProductStatusEnum());
    }
}