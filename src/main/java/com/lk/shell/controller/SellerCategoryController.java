package com.lk.shell.controller;

import com.lk.shell.dataobject.ProductCategory;
import com.lk.shell.exception.SellException;
import com.lk.shell.form.CategoryForm;
import com.lk.shell.service.CatagoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/seller/category")
public class SellerCategoryController {
    @Autowired
    private  CatagoryService catagoryService;

    @GetMapping("/list")
    public ModelAndView list(Map<String,Object> map){
           List<ProductCategory> productCategoryPage =catagoryService.findAll();
           map.put("categoryPage",productCategoryPage);
         return new ModelAndView("category/list",map);
    }
    @GetMapping("/index")
    public ModelAndView save(@RequestParam (value = "categoryId",required = false) Integer categoryId,
                             Map<String,Object> map){
        if (categoryId!=null){
           ProductCategory productCategory = catagoryService.findOne(categoryId);
           map.put("categoryinfo",productCategory);
        }
         return new ModelAndView("category/index",map);
    }
    /*新增/修改*/
    @PostMapping("/save")
    public ModelAndView save(@Valid CategoryForm categoryForm, BindingResult bindingResult,Map<String,Object> map){
        if(bindingResult.hasErrors()){
            map.put("msg",bindingResult.getFieldError().getDefaultMessage());
            map.put("url","/sell/seller/category/index");
            return new ModelAndView("common/error",map);
        }
        ProductCategory productCategory =new ProductCategory();
        try {
           if (categoryForm.getCategoryId()!=null){
               productCategory=catagoryService.findOne(categoryForm.getCategoryId());
           }
            BeanUtils.copyProperties(categoryForm,productCategory);
           catagoryService.save(productCategory);
        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/category/index");
            return new ModelAndView("common/error",map);

        }
           map.put("url","/sell/seller/category/list");
           return new ModelAndView("common/success",map);
    }
}
