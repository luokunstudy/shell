package com.lk.shell.repository;

import com.lk.shell.dataobject.OrderDetail;
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
public class OrderDetailRepostioryTest {

    @Autowired
    private OrderDetailRepostiory repostiory;
    @Test
    public void saveTest(){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("00001111");
        orderDetail.setOrderId("1111111");
        orderDetail.setProductIcon("http://xxxxx.jsp");
        orderDetail.setProductId("110110");
        orderDetail.setProductName("蒸饺");
        orderDetail.setProductPrice(new BigDecimal(3.6));
        orderDetail.setProductQuantity(2);
        OrderDetail result =  repostiory.save(orderDetail);
        Assert.assertNotNull(result);

    }


    @Test
    public void findByOrderId() throws Exception {
      List<OrderDetail> orderDetailList = repostiory.findByOrderId("1111111");
      Assert.assertNotEquals(0,orderDetailList.size());
    }

}