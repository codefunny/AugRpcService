package com.yzf.domain;

public class ActionRequest {
    private String orgNo;
    private String merNo;
    private String action;
    private String data;
    private String encryptkey;
    private String version;
    private String sign;

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "ActionRequest{" +
                "orgNo='" + orgNo + '\'' +
                ", merNo='" + merNo + '\'' +
                ", action='" + action + '\'' +
                ", data='" + data + '\'' +
                ", encryptkey='" + encryptkey + '\'' +
                ", version='" + version + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
