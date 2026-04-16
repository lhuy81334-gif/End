package com.pdu.end;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

public class Add extends AppCompatActivity {
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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

        //sự kiện cho button folder
        ibtnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_FOLDER);
            }
        });

        //sự kiện cho Button btnAdd
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chuyen du lieu imageview sang mang byte
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgPicture.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArr);
                byte[] picture = byteArr.toByteArray();

                MainActivity.database.INSERT_SANPHAM(
                        edtTenSP.getText().toString().trim(),
                        edtMota.getText().toString().trim(),
                        picture);

                Toast.makeText(Add.this, "Add succesful...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Add.this, MainActivity.class));
            }
        });

        btnHuy.setOnClickListener(v -> finish());
    }

    //Sử dụng phương thức override onActivityResult() để đổ dữ liệu ảnh lên imgPicture
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgPicture.setImageBitmap(bitmap);
        }
        if(requestCode == REQUEST_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgPicture.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
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