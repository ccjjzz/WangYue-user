package com.jiuyue.user.entity;

public class TokenEntity {
    private int id;
    private String token;
    private String timUserSig;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTimUserSig() {
        return timUserSig == null ? "" : timUserSig;
    }

    public void setTimUserSig(String timUserSig) {
        this.timUserSig = timUserSig;
    }
}
