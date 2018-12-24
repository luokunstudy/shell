package com.lk.shell.service.impl;

import com.lk.shell.dataobject.OrderDetail;
import com.lk.shell.dto.OrderDTO;
import com.lk.shell.enums.OrderStatusEnum;
import com.lk.shell.enums.PayStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {
    @Autowired
    private OrderServiceImpl orderService;

    private final String BUYER_OPENID="1101110";

    private final String ORDER_ID="1543571521550468877";

    @Test
    public void crtate() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("爆豪胜己");
        orderDTO.setBuyerAddress("雄英高中");
        orderDTO.setBuyerPhone("18808038325");
        orderDTO.setBuyerOpenid(BUYER_OPENID);
        //购物车
        List<OrderDetail> orderDetailList = new ArrayList<>();

        OrderDetail ol=new OrderDetail();
        ol.setProductId("123456");
        ol.setProductQuantity(1);
        orderDetailList.add(ol);
        orderDTO.setOrderDetailList(orderDetailList);

        OrderDTO result =orderService.crtate(orderDTO);
        System.out.println(result);

    }

    @Test
    public void findOne() throws Exception {
      OrderDTO orderDTO =  orderService.findOne(ORDER_ID);
        Assert.assertEquals(ORDER_ID,orderDTO.getOrderId());
    }

    @Test
    public void findList() throws Exception {
        PageRequest request = new PageRequest(0,2);
        Page<OrderDTO> orderDTOPage = orderService.findList(BUYER_OPENID,request);
        Assert.assertNotEquals(0,orderDTOPage.getTotalElements());
    }

    @Test
    public void cancel() throws Exception {
        OrderDTO orderDTO =  orderService.findOne(ORDER_ID);
        OrderDTO result =  orderService.cancel(orderDTO);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(),result.getOrderStatus());
    }

    @Test
    public void finish() throws Exception {
        OrderDTO orderDTO =  orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.finish(orderDTO);
        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(),result.getOrderStatus());

    }

    @Test
    public void paid() throws Exception {
        OrderDTO orderDTO =  orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.paid(orderDTO);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(),result.getOrderStatus());
    }
    @Test
    public void list(){
          PageRequest request = new PageRequest(0,2);
          Page<OrderDTO> orderDTOPage =orderService.findList(request);
      //    Assert.assertNotEquals(0,orderDTOPage.getTotalElements());
          Assert.assertTrue("查询订单列表",orderDTOPage.getTotalElements()>0);
    }
}