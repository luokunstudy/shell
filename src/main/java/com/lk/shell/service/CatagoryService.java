package com.lk.shell.service;

import com.lk.shell.dataobject.ProductCategory;

import java.util.List;

public interface CatagoryService {

    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> catagoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
