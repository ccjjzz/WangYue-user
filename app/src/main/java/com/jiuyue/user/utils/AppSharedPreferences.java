package com.jiuyue.user.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class AppSharedPreferences {
    private SharedPreferences settings; // static

    public AppSharedPreferences(Context context) {
        super();
        /*
         * 载入配置文件
         */
        String sharedPreferencesFileName = "sp_data";
        settings = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE);
    }

    /**
     * 存入一个String类型的数据
     *
     * @param optName 存入数据的名称
     * @param value   存入数据的值
     */
    public void putString(String optName, String value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(optName, value);
        editor.apply();
    }

    /**
     * 取出一个String类型的数据
     *
     * @param optName 取出数据的名称
     * @return 取出的数据的值(默认 " ")
     */
    public String getString(String optName) {
        return settings.getString(optName, "");
    }

    /**
     * 取出一个String类型的数据
     *
     * @param optName      取出数据的名称
     * @param defaultValue 默认值
     */
    public String getString(String optName, String defaultValue) {
        return settings.getString(optName, defaultValue);
    }

    /**
     * 存入一个boolean类型的数据
     *
     * @param optName 存入数据的名称
     * @param value   存入数据的值
     */
    public void putBoolean(String optName, boolean value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(optName, value);
        editor.apply();
    }

    /**
     * 取出一个boolean类型的数据
     *
     * @param optName 取出数据的名称
     * @return 取出的数据的值(默认false)
     */
    public boolean getBoolean(String optName) {
        return settings.getBoolean(optName, false);
    }

    /**
     * 取出一个boolean类型的数据
     *
     * @param optName 取出数据的名称
     * @param value   默认值
     * @return 取出的数据的值
     */
    public boolean getBoolean(String optName, boolean value) {
        return settings.getBoolean(optName, value);
    }

    /**
     * 存入一个int类型的数据
     *
     * @param optName 存入数据的名称
     * @param value   存入数据的值
     */
    public void putInt(String optName, int value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(optName, value);
        editor.apply();
    }

    /**
     * 取出一个int类型的数据
     *
     * @param optName 取出数据的名称
     * @return 取出的数据的值(默认0)
     */
    public int getInt(String optName) {
        return settings.getInt(optName, 0);
    }

    /**
     * 取出一个int类型的数据
     *
     * @param optName  取出数据的名称
     * @param defValue 取出数据的默认值
     * @return 取出的数据的值
     */
    public int getInt(String optName, int defValue) {
        return settings.getInt(optName, defValue);
    }

    /**
     * 存入一个Double类型的数据
     *
     * @param optName 存入数据的名称
     * @param value   存入数据的值
     */
    public void putDouble(String optName, double value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(optName, Double.doubleToRawLongBits(value));
        editor.apply();
    }

    /**
     * 取出一个Double类型的数据
     *
     * @param optName 取出数据的名称
     * @return 取出的数据的值(默认 " ")
     */
    public Double getDouble(String optName) {
        return Double.longBitsToDouble(settings.getLong(optName, 0));
    }

    /**
     * 存入一个List<T>类型的数据
     *
     * @param optName 存入数据的名称
     * @param value   存入数据的值
     */
    public <T> void putList(String optName, List<T> value) {
        SharedPreferences.Editor editor = settings.edit();
        String json = new Gson().toJson(value);
        editor.putString(optName, json);
        editor.apply();
    }

    /**
     * 取出一个Lis<T>类型的数据
     *
     * @param optName 取出数据的名称
     * @return 取出的数据的值(默认0)
     */
    public <T> List<T> getList(String optName, Class<T> cls) {
        List<T> listTemp = new ArrayList<>();
        try {
            String json = settings.getString(optName, "");
            if (json != null) {
                if (json.isEmpty()) {
                    return listTemp;
                }
                JsonArray array = new JsonParser().parse(json).getAsJsonArray();
                for (final JsonElement elem : array) {
                    listTemp.add(new Gson().fromJson(elem, cls));
                }
            }
//            listTemp = new Gson().fromJson(json, new TypeToken<ArrayList<T>>() {
//            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listTemp;
    }

    /**
     * 存入一个Object类型的数据
     *
     * @param optName 存入数据的名称
     * @param value   存入数据的值
     */
    public void putObject(String optName, Object value) {
        SharedPreferences.Editor editor = settings.edit();
        String json = new Gson().toJson(value);
        editor.putString(optName, json);
        editor.apply();
    }

    /**
     * 取出一个Object类型的数据
     *
     * @param optName 取出数据的名称
     * @return 取出的数据的值(默认0)
     */
    public <T> T getObject(String optName, Class<T> tClass) {
        T temp = null;
        try {
            String json = settings.getString(optName, "");
            if (json != null && json.isEmpty()) {
                return null;
            }
            temp = new Gson().fromJson(json, tClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }



}
