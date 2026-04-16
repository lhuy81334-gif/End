package com.pdu.end;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class a extends AppCompatActivity {
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
        setContentView(R.layout.activity_them_san_pham_acitvity);
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
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });
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