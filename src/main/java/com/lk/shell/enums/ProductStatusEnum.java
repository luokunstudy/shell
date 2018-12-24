package com.lk.shell.enums;

public enum ProductStatusEnum implements CodeEnum{
    UP(0,"在架"),
    DOWN(1,"下架");

    private  Integer code;

    private  String message;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }



    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
