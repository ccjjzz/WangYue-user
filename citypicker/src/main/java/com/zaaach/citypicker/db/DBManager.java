package com.zaaach.citypicker.db;

import static com.zaaach.citypicker.db.DBConfig.COLUMN_C_CODE;
import static com.zaaach.citypicker.db.DBConfig.COLUMN_C_LATITUDE;
import static com.zaaach.citypicker.db.DBConfig.COLUMN_C_LONGITUDE;
import static com.zaaach.citypicker.db.DBConfig.COLUMN_C_NAME;
import static com.zaaach.citypicker.db.DBConfig.COLUMN_C_PINYIN;
import static com.zaaach.citypicker.db.DBConfig.COLUMN_C_PROVINCE;
import static com.zaaach.citypicker.db.DBConfig.DB_NAME_V1;
import static com.zaaach.citypicker.db.DBConfig.LATEST_DB_NAME;
import static com.zaaach.citypicker.db.DBConfig.TABLE_NAME;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.zaaach.citypicker.model.City;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Author Bro0cL on 2016/1/26.
 */
public class DBManager {
    private static final int BUFFER_SIZE = 1024;

    private String DB_PATH;
    private Context mContext;

    public DBManager(Context context) {
        this.mContext = context;
        DB_PATH = File.separator + "data"
                + Environment.getDataDirectory().getAbsolutePath() + File.separator
                + context.getPackageName() + File.separator + "databases" + File.separator;

//        File dir = new File(DB_PATH);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
//        //创建表SQL语句
//        String stu_table = "create table " + TABLE_NAME + "(_id integer primary key autoincrement,"
//                + COLUMN_C_NAME + " text," + COLUMN_C_PINYIN + " text,"
//                + COLUMN_C_CODE + " varchar," + COLUMN_C_PROVINCE + " text,"
//                + COLUMN_C_LONGITUDE + " text," + COLUMN_C_LATITUDE + " text)";
//        //执行SQL语句
//        db.execSQL(stu_table);

        copyDBFile();
    }

    private void copyDBFile() {
        File dir = new File(DB_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //如果旧版数据库存在，则删除
        File dbV1 = new File(DB_PATH + DB_NAME_V1);
        if (dbV1.exists()) {
            dbV1.delete();
        }
        //创建新版本数据库
        File dbFile = new File(DB_PATH + LATEST_DB_NAME);
        if (!dbFile.exists()) {
            InputStream is;
            OutputStream os;
            try {
                is = mContext.getResources().getAssets().open(LATEST_DB_NAME);
                os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = is.read(buffer, 0, buffer.length)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveCities(List<City> cities) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
        //先清空数据
        db.execSQL("delete from " + TABLE_NAME);
        //插入数据
        for (City city : cities) {
            String sql = "insert into " + TABLE_NAME + "("
                    + COLUMN_C_NAME + "," + COLUMN_C_PINYIN + "," + COLUMN_C_CODE + ","
                    + COLUMN_C_PROVINCE + "," + COLUMN_C_LONGITUDE + "," + COLUMN_C_LATITUDE
                    + ") values(?,?,?,?,?,?)";
            Object[] args = {city.getName(), city.getPinyin(), city.getCode(), city.getProvince(), city.getLongitude(), city.getLatitude()};
            db.execSQL(sql, args);
        }
        db.close();
    }


    public List<City> getAllCities() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        List<City> result = new ArrayList<>();
        City city;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_C_NAME));
            String province = cursor.getString(cursor.getColumnIndex(COLUMN_C_PROVINCE));
            String pinyin = cursor.getString(cursor.getColumnIndex(COLUMN_C_PINYIN));
            String code = cursor.getString(cursor.getColumnIndex(COLUMN_C_CODE));
            String longitude = cursor.getString(cursor.getColumnIndex(COLUMN_C_LONGITUDE));
            String latitude = cursor.getString(cursor.getColumnIndex(COLUMN_C_LATITUDE));
            city = new City(name, province, pinyin, code, longitude, latitude);
            result.add(city);
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }

    public List<City> searchCity(final String keyword) {
        String sql = "select * from " + TABLE_NAME + " where "
                + COLUMN_C_NAME + " like ? " + "or "
                + COLUMN_C_PINYIN + " like ? ";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
        Cursor cursor = db.rawQuery(sql, new String[]{"%" + keyword + "%", keyword + "%"});

        List<City> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_C_NAME));
            String province = cursor.getString(cursor.getColumnIndex(COLUMN_C_PROVINCE));
            String pinyin = cursor.getString(cursor.getColumnIndex(COLUMN_C_PINYIN));
            String code = cursor.getString(cursor.getColumnIndex(COLUMN_C_CODE));
            String longitude = cursor.getString(cursor.getColumnIndex(COLUMN_C_LONGITUDE));
            String latitude = cursor.getString(cursor.getColumnIndex(COLUMN_C_LATITUDE));
            City city = new City(name, province, pinyin, code, longitude, latitude);
            result.add(city);
        }
        cursor.close();
        db.close();
        CityComparator comparator = new CityComparator();
        Collections.sort(result, comparator);
        return result;
    }

    /**
     * sort by a-z
     */
    private class CityComparator implements Comparator<City> {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            return a.compareTo(b);
        }
    }
}
