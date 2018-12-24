package com.lk.shell.repository;

import com.lk.shell.dataobject.ProductCategory;
import com.sun.javafx.tk.CompletionListener;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
   private ProductCategoryRepository repository;
     @Test
    public void findoneTest(){
        List<ProductCategory> productCategories =repository.findAll();
        System.out.println("产品类目："+productCategories);
    }
    @Test
    public void  saveTest(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryId(2);//更新使用
        productCategory.setCategoryName("免费专柜");
        productCategory.setCategoryType(3);
        repository.save(productCategory);
    }
    @Test
    public void findByCategoryTypeInTest(){
        List<Integer> list = Arrays.asList(1,2);
        List<ProductCategory> result =repository.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0,result.size());
    }
}