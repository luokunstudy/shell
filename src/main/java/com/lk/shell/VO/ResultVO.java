package com.lk.shell.VO;

import lombok.Data;

import java.io.Serializable;

/*Http请求返回的最外层对象*/
@Data
public class ResultVO<T> implements Serializable{
    private static  final long serialVersionUID =3068837394742385883L;

    /*错误码*/
    private Integer code;
    /*提示信息*/
    private String msg;
    /*返回具体内容*/
    private T data;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }
}
