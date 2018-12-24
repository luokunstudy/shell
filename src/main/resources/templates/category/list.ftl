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
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>类目id</th>
                            <th>名称</th>
                            <th>type</th>
                            <th>创建时间</th>
                            <th>修改时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list categoryPage as categoryinfo>
                        <tr>
                            <td>${categoryinfo.categoryId}</td>
                            <td>${categoryinfo.categoryName}</td>
                            <td>${categoryinfo.categoryType}</td>
                            <td>${categoryinfo.createTime}</td>
                            <td>${categoryinfo.updateTime}</td>
                            <td><a href="/sell/seller/category/index?categoryId=${categoryinfo.categoryId}">修改</a></td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>



</body>
</html>


