package com.lk.shell.service;

import com.lk.shell.dataobject.OrderMaster;
import com.lk.shell.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    /*创建订单*/
      OrderDTO crtate(OrderDTO orderDTO);
    /*查询单个订单*/
      OrderDTO findOne(String orderId);
    /*查询订单列表*/
      Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);
    /*取消订单*/
      OrderDTO cancel(OrderDTO orderDTO);
    /*完结订单*/
      OrderDTO finish(OrderDTO orderDTO);
    /*支付订单*/
      OrderDTO paid(OrderDTO orderDTO);
    /*查询订单列表（所有人）*/
    Page<OrderDTO> findList( Pageable pageable);

}
