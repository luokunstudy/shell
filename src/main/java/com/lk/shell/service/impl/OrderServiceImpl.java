package com.lk.shell.service.impl;

import com.lk.shell.converter.OrderMaster2OrderDTOConverter;
import com.lk.shell.dataobject.OrderDetail;
import com.lk.shell.dataobject.OrderMaster;
import com.lk.shell.dataobject.ProductInfo;
import com.lk.shell.dto.CartDTO;
import com.lk.shell.dto.OrderDTO;
import com.lk.shell.enums.OrderStatusEnum;
import com.lk.shell.enums.PayStatusEnum;
import com.lk.shell.enums.ResultEnum;
import com.lk.shell.exception.SellException;
import com.lk.shell.repository.OrderDetailRepostiory;
import com.lk.shell.repository.OrderMasterRepository;
import com.lk.shell.service.OrderService;
import com.lk.shell.service.PayService;
import com.lk.shell.service.ProductService;
import com.lk.shell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailRepostiory orderDetailRepostiory;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
  //  @Autowired
//    private PayService payService;

    @Override
    @Transactional
    public OrderDTO crtate(OrderDTO orderDTO) {
        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmout = new BigDecimal(BigInteger.ZERO);
        /*1.查询商品*/
          for ( OrderDetail orderDetail: orderDTO.getOrderDetailList()) {
              ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
              if(productInfo==null){
                    throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);
              }
                /*2.计算订单总价*/
                  orderAmout =  productInfo.getProductPrice().
                                 multiply(new BigDecimal((orderDetail.getProductQuantity()))).
                                 add(orderAmout);
                /*订单详情入库*/
                 orderDetail.setDetailId(KeyUtil.genUniqueKey());
                 orderDetail.setOrderId(orderId);
                 BeanUtils.copyProperties(productInfo,orderDetail);
                 orderDetailRepostiory.save(orderDetail);

          }

        /*3.写入订单数据库*/
        OrderMaster orderMaster =new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderAmount(orderAmout);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);

        /*4.扣库存*/
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
                        new CartDTO(e.getProductId(),e.getProductQuantity()))
                      .collect(Collectors.toList());
         productService.decreaseStock(cartDTOList);
        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster  =orderMasterRepository.findById(orderId).get();
        if (orderMaster==null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIT);
        }
        List<OrderDetail> orderDetailList = orderDetailRepostiory.findByOrderId(orderId);
        if (orderDetailList==null){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIT);
        }
        OrderDTO orderDTO = new OrderDTO();
        /*　从orderDTO对象复制属性到orderMaster对象  */
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid,pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        return new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());

    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {

        OrderMaster orderMaster = new OrderMaster();

        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.info("【取消订单】订单状态不正确，orderId={}，orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
       //修改状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if (result == null) {
            log.info("【取消订单】更新失败，orderMaster={}",result);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        // 返还库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单】订单中无商品详情，orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartDTOList);
       // 如果已支付则退款
        if (orderDTO.getOrderStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            //TODO
         //   payService.refund(orderDTO);
        }
       return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态
        if(!orderDTO.getOrderStatus().equals((OrderStatusEnum.NEW.getCode()))){
            log.info("【订单支付完成】订单状态不正确，orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //判断支付状态
     /*   if (orderDTO.getPayStatus().equals(OrderStatusEnum.WAIN)){
            log.info("【订单支付完成】订单支付状态不正确，orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_EMPTY);
        }*/
        //修改状态
        orderDTO.setOrderStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster= new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updataResult =orderMasterRepository.save(orderMaster);
        if (updataResult==null){
            log.info("【订单支付完成】更新失败，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //判断订单状态
        if (orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【订单支付完成】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【订单支付完成】订单支付状态不正确, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_EMPTY);
        }
        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【完结订单】更新失败，orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //推送微信模版消息
     //   pushMessageService.orderStatus(orderDTO);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage =  orderMasterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        return new PageImpl<>(orderDTOList,pageable,orderMasterPage.getTotalElements());

    }
}
