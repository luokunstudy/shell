package com.lk.shell.controller;

import com.lk.shell.VO.ProductInfoVO;
import com.lk.shell.VO.ProductVO;
import com.lk.shell.VO.ResultVO;
import com.lk.shell.dataobject.ProductCategory;
import com.lk.shell.dataobject.ProductInfo;
import com.lk.shell.service.CatagoryService;
import com.lk.shell.service.ProductService;
import com.lk.shell.utils.ResultVOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CatagoryService catagoryService;

    @GetMapping("/list")
    @Cacheable(cacheNames = "product",key = "123")    //添加缓存
    public ResultVO list(){
        //查询所有的上架商品
        List<ProductInfo> productInfoList = productService.findUpAll();
        //查询类目（一次性查询）
        /*(java8,lambda)*/
        List<Integer> catagoryTypeList = productInfoList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());
        List<ProductCategory> productCategoryList = catagoryService.findByCategoryTypeIn(catagoryTypeList);
        //数据拼装
        List<ProductVO> productVOList = new ArrayList<>();
        for(ProductCategory productCategory:productCategoryList){
            ProductVO productVO = new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            List<ProductInfoVO> productInfoVOS =new ArrayList<>();
            for(ProductInfo productInfo:productInfoList){
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO =new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOS.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOS);
            productVOList.add(productVO);
        }
      /*  ResultVO resultVO = new ResultVO();
        resultVO.setData(productVOList);
        resultVO.setCode(0);
        resultVO.setMsg("success");*/
        return ResultVOUtils.success(productVOList);
    }
}
