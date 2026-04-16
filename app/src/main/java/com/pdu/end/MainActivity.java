package com.pdu.end;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

    public static Database database;

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

        Anhxa();

        database = new Database(this, "QuanLySanPhamFinal1.sqlite", null, 1);        database.QueryData("CREATE TABLE IF NOT EXISTS SanPham(Id INTEGER PRIMARY KEY AUTOINCREMENT, Tensp VARCHAR(200), Mota VARCHAR(255), Picture BLOB)");

        GetSanPhan();

        btnAdd.setOnClickListener(view -> {
            // Sửa ThemSanPhamActivity thành Add
            Intent intent = new Intent(MainActivity.this, Add.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetSanPhan();
    }

    // Phải để public để Adapter gọi được
    public void XoaSanPham(final int id) {
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn xóa sản phẩm này không?");
        dialogXoa.setPositiveButton("Có", (dialogInterface, i) -> {
            database.QueryData("DELETE FROM SanPham WHERE Id = " + id);
            Toast.makeText(MainActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
            GetSanPhan();
        });
        dialogXoa.setNegativeButton("Không", null);
        dialogXoa.show();
    }

    public void GetSanPhan() {
        if (arraySanPham == null) {
            arraySanPham = new ArrayList<>();
        }
        
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
        
        if (adapter == null) {
            adapter = new SanPhamAdapter(this, R.layout.row_listview_sanpham, arraySanPham);
            lvSP.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void Anhxa() {
        btnAdd = findViewById(R.id.buttonAdd);
        lvSP = findViewById(R.id.listviewSanPham);
    }
}