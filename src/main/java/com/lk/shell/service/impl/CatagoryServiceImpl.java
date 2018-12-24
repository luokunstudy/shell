package com.lk.shell.service.impl;

import com.lk.shell.dataobject.ProductCategory;
import com.lk.shell.repository.ProductCategoryRepository;
import com.lk.shell.service.CatagoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class CatagoryServiceImpl implements CatagoryService{

    @Autowired
    private ProductCategoryRepository repository;


 /*//构通Example查找
    @GetMapping(value = "/productCategory/{categoryId}")
    public Optional<ProductCategory> girlFindOne(@PathVariable("categoryId") Integer categoryId){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryId(categoryId);
        Example<ProductCategory> example = Example.of(productCategory);
        return repository.findOne(example);
    }*/

    @Override
    public ProductCategory findOne(Integer categoryId) {
        return repository.findById(categoryId).get();
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> catagoryTypeList) {
        return repository.findByCategoryTypeIn(catagoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}

