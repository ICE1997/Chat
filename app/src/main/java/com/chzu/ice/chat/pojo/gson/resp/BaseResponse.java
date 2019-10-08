package com.chzu.ice.chat.pojo.gson.resp;

public class BaseResponse<T> {
    public String code;
    public String msg;
    public T data;
}
