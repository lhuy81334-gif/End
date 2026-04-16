package com.pdu.end;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;
    ListView lvSP;
    ArrayList<Sanpham> arraySanPham;
    SanPhamAdapter adapter;

    public static Database database; // Khai báo để sử dụng cho class khác nếu cần

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        Anhxa();

        // gọi phương thức load dữ liệu sản phẩm
        GetSanPhan();

        // sự kiện cho buttonADD (nút thêm sản phẩm)
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Add.class);
                startActivity(intent);
            }
        });
    }

    // Phương thức xóa sản phẩm
    public void XoaSanPham(final int id) {
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn xóa sản phẩm này không?");
        dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.QueryData("DELETE FROM SanPham WHERE Id = '" + id + "'");
                Toast.makeText(MainActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                GetSanPhan(); // Load lại dữ liệu
            }
        });
        dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Không làm gì cả
            }
        });
        dialogXoa.show();
    }

    public void GetSanPhan() {
        arraySanPham = new ArrayList<>();
        adapter = new SanPhamAdapter(this, R.layout.row_listview_sanpham, arraySanPham);
        lvSP.setAdapter(adapter);

        database = new Database(this, "QuanLySanPham.sqlite", null, 1);
        // de luu hinh anh SQLite su dung kieu du lieu BLOB
        database.QueryData("CREATE TABLE IF NOT EXISTS SanPham(Id INTEGER PRIMARY KEY AUTOINCREMENT, Tensp VARCHAR(200), Mota VARCHAR(255), Picture BLOB)");

        // lay du lieu
        Cursor cursor = database.GetData("SELECT * FROM SanPham");
        arraySanPham.clear();
        while (cursor.moveToNext()){
            arraySanPham.add(new Sanpham(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getBlob(3)
            ));
        }
        adapter.notifyDataSetChanged();
    }

    // viết phương thức ánh xạ
    private void Anhxa() {
        btnAdd = findViewById(R.id.buttonAdd);
        lvSP = findViewById(R.id.listviewSanPham);
    }
}