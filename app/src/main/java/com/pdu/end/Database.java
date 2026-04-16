package com.pdu.end;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Truy vấn không trả kết quả (CREATE, INSERT, UPDATE, DELETE...)
    public void QueryData(String query) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(query);
    }

    // Truy vấn trả kết quả (SELECT)
    public Cursor GetData(String query) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(query, null);
    }

    // Phương thức xử lý insert dữ liệu có hình ảnh
    public void INSERT_SANPHAM(String tensp, String mota, byte[] picture) {
        SQLiteDatabase database = getWritableDatabase();
        String query = "INSERT INTO SanPham VALUES(null, ?, ?, ?)";
        SQLiteStatement stm = database.compileStatement(query);

        stm.clearBindings();
        stm.bindString(1, tensp);
        stm.bindString(2, mota);
        stm.bindBlob(3, picture);

        stm.executeInsert();
    }

    // sửa sản phẩm
    public void UPDATE_SANPHAM(int id, String tensp, String mota, byte[] picture) {
        SQLiteDatabase database = getWritableDatabase();
        String query = "UPDATE SanPham SET Tensp = ?, Mota = ?, Picture = ? WHERE Id = ?";
        SQLiteStatement stm = database.compileStatement(query);

        stm.clearBindings();
        stm.bindString(1, tensp);
        stm.bindString(2, mota);
        stm.bindBlob(3, picture);
        stm.bindLong(4, id);

        stm.executeUpdateDelete();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}