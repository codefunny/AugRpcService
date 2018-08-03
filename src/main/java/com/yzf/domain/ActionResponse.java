package com.yzf.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ActionResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String encryptkey;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEncryptkey() {
        return encryptkey;
    }

    public void setEncryptkey(String encryptkey) {
        this.encryptkey = encryptkey;
    }

    @Override
    public String toString() {
        return "ActionResponse{" +
                "data='" + data + '\'' +
                ", encryptkey='" + encryptkey + '\'' +
                '}';
    }
}
