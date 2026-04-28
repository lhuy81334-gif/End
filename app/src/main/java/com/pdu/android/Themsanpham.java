package com.pdu.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Themsanpham extends AppCompatActivity {
    Button btnSelect;
    Button btnAdd, btnHuy;
    EditText edtTenSP, edtMota;
    ImageButton ibtnCamera, ibtnFolder;
    ImageView imgPicture;
    int REQUEST_CAMERA = 123;
    int REQUEST_FOLDER = 321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Ánh xạ
        Anh_Xa();


        //sự kiện cho button camera
        ibtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra quyền Camera
                if (checkSelfPermission(android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    // Nếu đã có quyền -> Mở camera
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    // Nếu chưa có quyền -> Yêu cầu cấp quyền
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 123);
                }
            }
        });
        //sự kiện cho button folder
        ibtnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), REQUEST_FOLDER);
            }
        });

        //sự kiện cho Button btnAdd
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 1. Kiểm tra tên không được để trống
                    String ten = edtTenSP.getText().toString().trim();
                    if (ten.isEmpty()) {
                        Toast.makeText(Themsanpham.this, "Vui lòng nhập tên sản phẩm!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 2. Kiểm tra và lấy ảnh từ ImageView
                    if (imgPicture.getDrawable() == null) {
                        Toast.makeText(Themsanpham.this, "Vui lòng chọn ảnh!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Chuyển đổi an toàn: Lấy bitmap từ ImageView
                    Bitmap bitmap;
                    try {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgPicture.getDrawable();
                        bitmap = bitmapDrawable.getBitmap();
                    } catch (ClassCastException e) {
                        // Nếu ảnh là Vector (xml), xử lý khác để tránh văng
                        Drawable drawable = imgPicture.getDrawable();
                        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                    }

                    // 3. NÉN ẢNH (BẮT BUỘC dùng JPEG 50 để tránh lỗi văng app)
                    ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
// Sửa PNG thành JPEG và 100 thành 50
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArr);
                    byte[] picture = byteArr.toByteArray();

                    // 4. Lưu vào Database
                    MainActivity.database.INSERT_SANPHAM(
                            ten,
                            edtMota.getText().toString().trim(),
                            picture);

                    Toast.makeText(Themsanpham.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay về MainActivity

                } catch (Exception e) {
                    Toast.makeText(Themsanpham.this, "Lỗi: Ảnh quá lớn hoặc không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnHuy.setOnClickListener(v -> finish());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123 && grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        } else {
            Toast.makeText(this, "Bạn cần cấp quyền Camera để chụp ảnh!", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //Sử dụng phương thức override onActivityResult() để đổ dữ liệu ảnh lên imgPicture
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgPicture.setImageBitmap(bitmap);
        }

        if (requestCode == REQUEST_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgPicture.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void Anh_Xa() {
        btnAdd = findViewById(R.id.buttonAdd);
        btnHuy = findViewById(R.id.buttonHuy);
        edtTenSP = findViewById(R.id.edittextTenSP);
        edtMota = findViewById(R.id.edittextMota);
        ibtnCamera = findViewById(R.id.imagebutton_camera);
        ibtnFolder = findViewById(R.id.imagebutton_folder);
        imgPicture = findViewById(R.id.imageview_no_imge);
    }
}