package com.jiuyue.user.global;

public class EventKey {
    public static final String WX_PAY_RESULT = "WX_PAY_RESULT";
    public static final String UPDATE_PRODUCT_LIST = "UPDATE_PRODUCT_LIST";
    public static final String PRODUCT_DETAIL_DATA = "PRODUCT_DETAIL_DATA";
    public static final String REFRESH_ORDER_STATUS = "REFRESH_ORDER_STATUS";
    public static final String MODIFY_INFO = "MODIFY_INFO";
    public static final String UPDATE_FOLLOW_STATUS = "UPDATE_FOLLOW_STATUS";
    public static final String UPDATE_ORDER_NUM = "UPDATE_ORDER_NUM";
    public static final String REFRESH_ADDRESS = "REFRESH_ADDRESS";
    public static final String UPDATE_LOCATION_ADDRESS = "UPDATE_LOCATION_ADDRESS";
    public static final String REFRESH_DYNAMIC_STATUS = "REFRESH_DYNAMIC_STATUS";
    public static final String REFRESH_MINE_INFO = "REFRESH_MINE_INFO";

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
