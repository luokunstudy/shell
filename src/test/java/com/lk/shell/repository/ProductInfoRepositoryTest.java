package com.lk.shell.repository;

import com.lk.shell.dataobject.ProductInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoRepositoryTest {
    @Autowired
    private  ProductInfoRepository repository;
    @Test
    public void saveTest(){
        ProductInfo productInfo =new ProductInfo();
        productInfo.setProductId("123456");
        productInfo.setProductName("皮蛋瘦肉粥");
        productInfo.setProductPrice(new BigDecimal(3.4));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("经典早餐搭配！");
        productInfo.setProductIcon("http://xxxxx.jsp");
        productInfo.setProductStatus(0);
        productInfo.setCategoryType(2);
        ProductInfo result = repository.save(productInfo);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByProductStatus() throws Exception {
      List<ProductInfo> infoList = repository.findByProductStatus(0);
      Assert.assertNotEquals(0,infoList.size());
    }

}