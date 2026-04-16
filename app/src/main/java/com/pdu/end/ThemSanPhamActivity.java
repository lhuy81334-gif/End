package com.pdu.end;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class ThemSanPhamActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_them_san_pham);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Anh_Xa();

        ibtnCamera.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        });

        ibtnFolder.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_FOLDER);
        });

        btnAdd.setOnClickListener(v -> {
            // KIỂM TRA CHỐNG VĂNG: Nếu chưa có ảnh thì không cho lưu
            if (imgPicture.getDrawable() == null) {
                Toast.makeText(this, "Vui lòng chọn hoặc chụp ảnh sản phẩm!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgPicture.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArr);
                byte[] picture = byteArr.toByteArray();

                MainActivity.database.INSERT_SANPHAM(
                        edtTenSP.getText().toString().trim(),
                        edtMota.getText().toString().trim(),
                        picture);

                Toast.makeText(ThemSanPhamActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khi lưu ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnHuy.setOnClickListener(v -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && data != null){
            if(requestCode == REQUEST_CAMERA){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgPicture.setImageBitmap(bitmap);
            } else if(requestCode == REQUEST_FOLDER){
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
        super.onActivityResult(requestCode, resultCode, data);
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