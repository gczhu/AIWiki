package com.zju.se.sem.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 祝广程
 * @version 1.0
 */
public class Message {
    private boolean flag;
    private String msg;
    private Integer code;
    private Map<String, Object> data = new HashMap<>();

    public Message() {
    }

    public Message(boolean flag, String msg, Integer code, Map<String, Object> data) {
        this.flag = flag;
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public Message(boolean flag, String msg, Integer code) {
        this.flag = flag;
        this.msg = msg;
        this.code = code;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Message data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public Message data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Message{" +
                "flag=" + flag +
                ", msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
