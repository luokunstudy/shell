package com.lk.shell.controller;

import com.lk.shell.dto.OrderDTO;
import com.lk.shell.enums.ResultEnum;
import com.lk.shell.exception.SellException;
import com.lk.shell.service.OrderService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/seller/order")
@Slf4j
public class SellerOrderController {
    @Autowired
    private OrderService orderService;

    /*订单列表*/
    // @RequestMapping(value = "/list",method = RequestMethod.GET)
    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "3") Integer size,
                             Map<String, Object> map) {
        PageRequest request = new PageRequest(page - 1, size);
        Page<OrderDTO> orderDTOPage = orderService.findList(request);
        map.put("orderDTOPage", orderDTOPage);
        map.put("currentPage", page);
        map.put("size", size);
        return new ModelAndView("order/list", map);
    }
    /*取消订单*/
    @GetMapping("/cancel")
    public ModelAndView cancel(@RequestParam("orderId") String orderId, Map<String, Object> map){
        try{
            OrderDTO orderDTO =orderService.findOne(orderId);
            orderDTO.getOrderStatusEnum().getMessage();
                orderService.cancel(orderDTO);
        }catch (SellException e){
            log.error("【卖家端取消订单】查询不到订单",e);
            map.put("msg", ResultEnum.ORDER_NOT_EXIT.getmessage());
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("common/error",map);
        }

        map.put("msg", ResultEnum.ORDER_CANCEL_SUCCESS.getmessage());
        map.put("url","/sell/seller/order/list");

        return new ModelAndView("common/success");
    }
    /*d订单详情*/
    @GetMapping("/detail")
    public ModelAndView detail(@RequestParam("orderId") String orderId, Map<String, Object> map){
        OrderDTO orderDTO = new OrderDTO();
        try {
          orderDTO =  orderService.findOne(orderId);
        }catch (SellException e){
            log.error("【卖家端订单详情】发生异常",e);
            map.put("msg", e.getMessage());
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("common/error",map);
        }
         map.put("orderDTO",orderDTO);
        return new ModelAndView("order/detail",map);
    }

    /*完结订单*/
    @GetMapping("/finish")
    public ModelAndView finish(@RequestParam("orderId") String orderId, Map<String, Object> map){
        try{
            OrderDTO orderDTO =orderService.findOne(orderId);
            orderDTO.getOrderStatusEnum().getMessage();
            orderService.finish(orderDTO);
        }catch (SellException e){
            log.error("【卖家端完结订单】查询不到订单",e);
            map.put("msg", ResultEnum.ORDER_NOT_EXIT.getmessage());
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("common/error",map);
        }

        map.put("msg", ResultEnum.ORDER_FINISH_SUCCESS.getmessage());
        map.put("url","/sell/seller/order/list");

        return new ModelAndView("common/success");
    }
}
