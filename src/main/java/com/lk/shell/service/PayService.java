package com.lk.shell.service;

import com.lk.shell.dto.OrderDTO;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;

public interface PayService {

    PayResponse create(OrderDTO orderDTO);

    PayResponse notify(String notify);

    RefundRequest refund(OrderDTO orderDTO);
}
