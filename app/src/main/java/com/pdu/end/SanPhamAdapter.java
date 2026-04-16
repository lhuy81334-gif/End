package com.pdu.end;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SanPhamAdapter extends BaseAdapter {
    private MainActivity context;
    private int layout;
    private List<Sanpham> sanphamlist;

    public SanPhamAdapter(MainActivity context, int layout, List<Sanpham> sanphamlist) {
        this.context = context;
        this.layout = layout;
        this.sanphamlist = sanphamlist;
    }

    @Override
    public int getCount() {
        return sanphamlist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView txtTenSanPham, txtMoTa;
        ImageView imgePicture, imgDelete, imgEdit;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            //anh xa
            holder.txtTenSanPham = view.findViewById(R.id.textviewTenSanPhamCustom);
            holder.txtMoTa = view.findViewById(R.id.textviewMoTaCustom);
            holder.imgePicture = view.findViewById(R.id.imagePicture);
            holder.imgDelete = view.findViewById(R.id.imageviewDelete);
            holder.imgEdit = view.findViewById(R.id.imageviewEdit);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Sanpham sanpham = sanphamlist.get(i);
        holder.txtTenSanPham.setText(sanpham.getTenSP());
        holder.txtMoTa.setText(sanpham.getMota());

        //chuyen mang byte[] sang Bitmap
        byte[] picture = sanpham.getHinh();
        Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        holder.imgePicture.setImageBitmap(bitmap);

        // Bắt sự kiện xóa
        holder.imgDelete.setOnClickListener(v -> {
            context.XoaSanPham(sanpham.getId());
        });

        // Bắt sự kiện sửa
        holder.imgEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, SuaSanPham.class);
            // Truyền dữ liệu sang màn hình sửa
            intent.putExtra("id", sanpham.getId());
            intent.putExtra("ten", sanpham.getTenSP());
            intent.putExtra("mota", sanpham.getMota());
            intent.putExtra("hinh", sanpham.getHinh());
            context.startActivity(intent);
        });

        return view;
    }
}