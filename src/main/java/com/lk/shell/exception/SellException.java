package com.lk.shell.exception;

import com.lk.shell.enums.ResultEnum;

public class SellException extends RuntimeException{
    private Integer code;

    public SellException(ResultEnum resultEnum){
        super(resultEnum.getmessage());
        this.code=resultEnum.getCode();
    }
    public SellException(Integer code,String message){
        super(message);
        this.code=code;
    }
}
