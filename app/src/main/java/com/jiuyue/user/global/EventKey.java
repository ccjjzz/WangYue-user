package com.jiuyue.user.global;

public class EventKey {
    public static final String AUTH_SUCCESS = "AUTH_SUCCESS";

    public static class EventMsg {
        private int code;
        private String content;

        public EventMsg(int code, String content) {
            this.code = code;
            this.content = content;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
