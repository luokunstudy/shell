<html>
<#include "../common/header.ftl">
<body>

<div id="wrapper" class="toggled">

<#--边栏sidebar-->
<#include "../common/nav.ftl">

    <div id="page-content-wrapper">

<div class="container-fluid">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <table class="table">
                <thead>
                <tr>
                    <th>订单Id</th>
                    <th>订单总金额</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>${orderDTO.orderId}</td>
                    <td>${orderDTO.orderAmount}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <br/><br/>
           <#-- 订单详情表数据-->
        <div class="col-md-12 column">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>商品Id</th>
                    <th>商品名称</th>
                    <th>价格</th>
                    <th>数量</th>
                    <th>总额</th>
                </tr>
                </thead>
                <tbody>
                <#list orderDTO.orderDetailList as orderdetail>
                <tr>
                    <td>${orderdetail.productId}</td>
                    <td>${orderdetail.productName}</td>
                    <td>${orderdetail.productPrice}</td>
                    <td>${orderdetail.productQuantity}</td>
                    <td>${orderdetail.productQuantity * orderdetail.productPrice}</td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
            <#--操作-->

                <div class="col-md-12 column">
                  <#if orderDTO.getOrderStatusEnum().message =="新订单">
                    <a href="/sell/seller/order/finish?orderId=${orderDTO.orderId}" type="button" class="btn btn-primary btn-sm">完结订单</a>
                    <a href="/sell/seller/order/cancel?orderId=${orderDTO.orderId}" type="button" class="btn btn-sm btn-warning">取消订单</a>
                  </#if>
                </div>

    </div>
</div>
    </div>
</div>
</body>
</html>