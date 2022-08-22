package com.jiuyue.user.utils;

import java.util.ArrayList;
import java.util.List;

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

}
