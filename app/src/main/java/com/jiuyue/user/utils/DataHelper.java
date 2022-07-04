package com.jiuyue.user.utils;

import android.text.SpannableStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataHelper {
    //生成八位随机数（含有大小写字母和数字）
    public static String getRandomStr(){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(i+"");
        for (char c = 'a'; c <= 'z'; c++)
            list.add(c+"");
        for (char c = 'A'; c <= 'Z'; c++)
            list.add(c+"");

        StringBuilder randomStr = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int mathInt;
            mathInt = (int) (Math.random() * list.size());
            randomStr.append(list.get(mathInt));
            list.remove(mathInt);
        }
        return randomStr.toString();
    }

    public static SpannableStringBuilder getVipDetailRandomStr() {
        SpannableStringBuilder str = new SpannableStringBuilder();
        Random random = new Random();
        for (int i = 0; i <= 20; i++) {
            int r = random.nextInt(20);
            str.append(getRandomStr());
            if (r < 8) {
                str.append("开启尊享礼包获得")
                        .append("<font color=\"#FF9800\">")
                        .append("终身会员")
                        .append("</font>")
                        .append("&nbsp;&nbsp;");
            } else if (r > 8 && r < 13) {
                str.append("开启尊享礼包获得")
                        .append("+<font color=\"#FF0000\">")
                        .append("华为荣耀9X")
                        .append("</font>")
                        .append("&nbsp;&nbsp;");
            } else {
                str.append("开启尊享礼包获得")
                        .append("+<font color=\"#FF0000\">")
                        .append("华为荣耀9X")
                        .append("</font>")
                        .append("+<font color=\"#FF0000\">")
                        .append("100元话费")
                        .append("</font>")
                        .append("&nbsp;&nbsp;");
            }
        }
        return str;
    }

}
