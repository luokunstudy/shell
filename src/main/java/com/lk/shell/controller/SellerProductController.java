package com.lk.shell.controller;

import com.lk.shell.dataobject.ProductCategory;
import com.lk.shell.dataobject.ProductInfo;
import com.lk.shell.dto.OrderDTO;
import com.lk.shell.exception.SellException;
import com.lk.shell.from.productFrom;
import com.lk.shell.service.CatagoryService;
import com.lk.shell.service.ProductService;
import com.lk.shell.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seller/product")
public class SellerProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CatagoryService catagoryService;

    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "3") Integer size,
                             Map<String, Object> map){
        PageRequest request = new PageRequest(page - 1, size);
        Page<ProductInfo> productInfoPage = productService.findAll(request);
        map.put("productInfoPage", productInfoPage);
        map.put("currentPage", page);
        map.put("size", size);
        return new ModelAndView("product/list", map);
    }
    /*商品上架*/
    @RequestMapping("/on_sale")
    public  ModelAndView onSale(@RequestParam(value = "productId") String productId,
                                Map<String,Object> map){
        try {
            productService.onSale(productId);
        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/product/list");
            return new ModelAndView("common/error",map);
        }
        map.put("url","/sell/seller/product/list");
        return new ModelAndView("common/success",map);
    }

    /*商品下架*/
    @RequestMapping("/off_sale")
    public  ModelAndView offSale(@RequestParam(value = "productId") String productId,
                                Map<String,Object> map){
        try {
            productService.offSale(productId);
        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/product/list");
            return new ModelAndView("common/error",map);
        }
        map.put("url","/sell/seller/product/list");
        return new ModelAndView("common/success",map);
    }

    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "productId",required = false) String productId,
                              Map<String,Object> map){
        if (!StringUtils.isEmpty(productId)){
           ProductInfo productInfo = productService.findOne(productId);
           map.put("productInfo",productInfo);
        }
        //查询类目
        List<ProductCategory> productCategoryList= catagoryService.findAll();
        map.put("categoryList",productCategoryList);
        return new ModelAndView("product/index",map);
    }
    /*新增/修改*/
    @PostMapping("/save")
  //  @CachePut(cacheNames = "product",key = "123")
    @CacheEvict(cacheNames = "product",key = "123")
    public ModelAndView save(@Valid productFrom productFrom, BindingResult bindingResult,Map<String,Object> map){
        if (bindingResult.hasErrors()){
            map.put("msg",bindingResult.getFieldError().getDefaultMessage());
            map.put("url","/sell/seller/product/index");
            return new ModelAndView("common/error",map);
        }
            ProductInfo productInfo = new ProductInfo();
        try {
            //如果productId为空，则新增
            if (!StringUtils.isEmpty(productFrom.getProductId())){
                   productInfo =productService.findOne(productFrom.getProductId());
            }else{
                productFrom.setProductId(KeyUtil.genUniqueKey());
            }

            BeanUtils.copyProperties(productFrom,productInfo);
            productService.save(productInfo);
        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/product/index");
            return new ModelAndView("common/error",map);
        }
        map.put("url","/sell/seller/product/list");
        return new ModelAndView("common/success",map);
    }
}
