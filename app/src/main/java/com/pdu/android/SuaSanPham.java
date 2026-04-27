package com.pdu.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SuaSanPham extends AppCompatActivity {
    Button btnUpdate, btnHuy_update;
    ImageButton img_folder_update, img_camera_update;
    ImageView imgview_no_imge_update;
    EditText edtUpdateMota, edtUpdateTenSP;
    int REQUEST_CAMERA = 234;
    int REQUEST_FOLDER = 432;

    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sua_san_pham);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Ánh xạ
        Anh_Xa();

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        String ten = intent.getStringExtra("ten");
        String mota = intent.getStringExtra("mota");
        byte[] hinh = intent.getByteArrayExtra("hinh");

        // Đổ dữ liệu vào các view
        edtUpdateTenSP.setText(ten);
        edtUpdateMota.setText(mota);
        if (hinh != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(hinh, 0, hinh.length);
            imgview_no_imge_update.setImageBitmap(bitmap);
        }

        // Sự kiện Cập nhật
        // Sự kiện Cập nhật
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // 1. Kiểm tra ảnh có tồn tại không
                    if (imgview_no_imge_update.getDrawable() == null) {
                        Toast.makeText(SuaSanPham.this, "Vui lòng chọn ảnh!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 2. Chuyển dữ liệu imageview sang mang byte (Cách an toàn)
                    imgview_no_imge_update.setDrawingCacheEnabled(true);
                    imgview_no_imge_update.buildDrawingCache();
                    Bitmap bitmap = imgview_no_imge_update.getDrawingCache();

                    ByteArrayOutputStream byteArr = new ByteArrayOutputStream();

                    // QUAN TRỌNG: Sửa thành JPEG và chất lượng 50 để không bị văng
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArr);
                    byte[] picture = byteArr.toByteArray();

                    // 3. Gọi database để update
                    MainActivity.database.UPDATE_SANPHAM(
                            id,
                            edtUpdateTenSP.getText().toString().trim(),
                            edtUpdateMota.getText().toString().trim(),
                            picture
                    );

                    Toast.makeText(SuaSanPham.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

                    // 4. Dùng finish() thay vì khởi tạo lại MainActivity để tiết kiệm bộ nhớ
                    finish();

                } catch (Exception e) {
                    Toast.makeText(SuaSanPham.this, "Lỗi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Sự kiện cho button camera
        img_camera_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

        // Sự kiện cho button folder
        img_folder_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_FOLDER);
            }
        });

        btnHuy_update.setOnClickListener(v -> finish());
    }

    private void Anh_Xa() {
        btnUpdate = findViewById(R.id.buttonUpdate);
        btnHuy_update = findViewById(R.id.buttonHuy_update);
        img_folder_update = findViewById(R.id.imagebutton_folder_update);
        img_camera_update = findViewById(R.id.imagebutton_camera_update);
        imgview_no_imge_update = findViewById(R.id.imageview_no_imge_update);
        edtUpdateMota = findViewById(R.id.edittextUpdateMota);
        edtUpdateTenSP = findViewById(R.id.edittextUpdateTenSP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgview_no_imge_update.setImageBitmap(bitmap);
        }
        if(requestCode == REQUEST_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgview_no_imge_update.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}